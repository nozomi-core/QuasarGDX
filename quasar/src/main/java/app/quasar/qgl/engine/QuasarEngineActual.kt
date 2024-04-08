@file:JvmName("QuasarEngineKt")

package app.quasar.qgl.engine

import app.quasar.qgl.entity.*
import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.scripts.QuasarRootScripts
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngineActual(
    private val drawableApi: DrawableApi,
    private val onExit: (EngineDeserialized) -> Unit,
): QuasarEngine, NodeSearchable {
    //Engine serialized members
    private var currentRuntimeId = 1L
    private var graph = NodeGraph()
    private var rootScripts = mutableListOf<KClass<*>>()

    //Queues and Engine metadata
    private val destructionQueue = mutableListOf<GameNode<*,*>>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*,*>>, Any?>>()
    private var currentNodeIdRunning = -1L

    private var _isRunning = true
    val isRunning: Boolean get() = _isRunning
    private var engineMarkedToExit = false

    fun setEngineData(deserialized: EngineDeserialized?) {
        deserialized?.let { data ->
            this.currentRuntimeId = data.currentRuntimeId
            this.graph = data.graph
            this.rootScripts = mutableListOf<KClass<*>>().apply {
                addAll(data.rootScripts)
            }
        }
    }

    /** Core Methods */
     override fun simulate(deltaTime: Float) {
        if(isRunning) {
            doDestructionStep()
            doCreationStep()
            doSimulationStep(deltaTime)

            if(engineMarkedToExit && isRunning) {
                doExit()
            }
        }
    }

     override fun draw() {
        graph.nodes.forEach {
            it.draw(drawableApi)
        }
    }

    fun exit() {
        engineMarkedToExit = true
    }

    private fun doExit() {
        _isRunning = false
        onExit(
            EngineDeserialized(
                currentRuntimeId = currentRuntimeId,
                graph = graph,
                rootScripts = rootScripts
            )
        )
    }

    //:: Engine steps
    private fun doDestructionStep() {
        destructionQueue.forEach {
            graph.nodes.remove(it)
        }
        destructionQueue.clear()
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.create(this, argument)
            graph.nodes.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun doSimulationStep(deltaTime: Float) {
        graph.nodes.forEach {
            it.simulate(deltaTime)
        }
    }

    /** Interfaces */

    //Interface :: (EngineApiAdmin)
    override fun generateId() = currentRuntimeId++

    override fun <T : GameNode<*,*>> createGameNode(nodeScript: KClass<T>, argument: Any?) {
        checkNodeIsRootScriptThenThrow(nodeScript)
        creationQueue.add(Pair(nodeScript, argument))
    }


    override fun <T : GameNode<*,*>> createRootScripts(gameScripts: List<KClass<T>>) {
        //Add quasars own root scripts that will be spawned alongside the game scripts by developer
        val mergeAllScripts = mutableListOf<KClass<*>>().apply {
            addAll(QuasarRootScripts.scripts)
            addAll(gameScripts)
        }.toList()

        checkUniqueRootScripts(mergeAllScripts)
        mergeAllScripts.filterIsInstance<KClass<T>>().forEach {
            createGameNode(it)
        }
        doCreationStep()
        checkScriptOrderIntegrity()

        rootScripts = mutableListOf()
        rootScripts.addAll(mergeAllScripts)

        //Check if the script is a root class and call rootCreated in ca
        graph.nodes.forEach {
            if(it is RootNode) {
                it.doRootCreated()
            }
        }
    }

    override fun setCurrentNodeRunning(node: GameNode<*, *>) {
        this.currentNodeIdRunning = node.runtimeId
    }

    override fun checkNodeIsNotRunning(node: GameNode<*, *>) {
        if(isRunning && currentNodeIdRunning == node.runtimeId) {
            throw IllegalAccessException("Can not perform this operation while current node running")
        }
    }

    override fun destroyNode(node: GameNode<*,*>) {
        checkNodeIsRootScriptThenThrow(node)
        destructionQueue.add(node)
    }

    //:: Interface (NodeSearchable)

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        return graph.requireFindByInterface(nodeInterface)
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        return graph.findById(id, nodeInterface)
    }

    /** Utility and Helper Methods */

    private fun checkScriptOrderIntegrity() {
        graph.nodes.forEach { currentNode ->
            if(currentNode is RootNode) {
                val shouldBeBefore = currentNode.shouldRunBefore()
                val thisNodeIndex = graph.nodes.indexOf(currentNode)
                shouldBeBefore.forEach { dependClass ->
                    val dependIndex = graph.nodes.indexOf(graph.nodes.first { it::class == dependClass })
                    if(dependIndex > thisNodeIndex) {
                        throw SecurityException("${dependClass.simpleName} should be spawned before ${currentNode::class.simpleName}")
                    }
                }
            }
        }
    }

    private fun checkNodeIsRootScriptThenThrow(node: GameNode<*,*>) {
        checkNodeIsRootScriptThenThrow(node::class)
    }

    private fun checkNodeIsRootScriptThenThrow(script: KClass<*>) {
        if(rootScripts.contains(script)) {
            throw SecurityException("Root scripts are immutable, you can not add or remove them")
        }
    }
}


/** Utils that don't require class members */

private fun checkUniqueRootScripts(scripts: List<KClass<*>>) {
    val checkDuplicates = HashSet<KClass<*>>()
    scripts.forEach {
        if(checkDuplicates.contains(it)) {
            throw IllegalArgumentException("Can not have duplicate classes in root scripts, only 1 instance shall be spawned")
        }
        checkDuplicates.add(it)
    }
}