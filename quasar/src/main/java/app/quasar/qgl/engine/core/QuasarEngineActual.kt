package app.quasar.qgl.engine.core

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngineActual(
        private val drawableApi: DrawableApi,
        private val rootScripts: List<KClass<*>>,
        private val onExit: (EngineDeserialized) -> Unit,
        data: EngineDeserialized?
): QuasarEngine, NodeSearchable {
    private val data = data?.toEngineData() ?: EngineData.createDefault()

    //Engine accounting variables
    private val destructionQueue = mutableListOf<GameNode<*, *>>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*, *>>, Any?>>()
    private var currentNodeIdRunning = -1L

    private var isRunning = true
    private var engineMarkedToExit = false

    /** Interface :: (QuasarEngine) */
    override fun simulate(deltaTime: Float) {
        if(!isRunning) {
            return
        }

        doDestructionStep()
        doCreationStep()
        doSimulationStep(deltaTime)

        if(engineMarkedToExit && isRunning) {
            doExit()
        }
    }

    override fun draw() {
        data.graph.nodes.forEach {
            it.draw(drawableApi)
        }
    }

    override fun exit() {
        engineMarkedToExit = true
    }

    override fun destroyNode(node: GameNode<*, *>) {
        checkNodeIsRootScriptThenThrow(node)
        destructionQueue.add(node)
    }

    override fun setCurrentNodeRunning(node: GameNode<*, *>) {
        this.currentNodeIdRunning = node.runtimeId
    }

    override fun checkNodeIsNotRunning(node: GameNode<*, *>) {
        if (isRunning && currentNodeIdRunning == node.runtimeId) {
            throw IllegalAccessException("Can not perform this operation while current node running")
        }
    }

    private fun doExit() {
        isRunning = false
        onExit(
            EngineDeserialized(
                currentRuntimeId = data.currentRuntimeId,
                rootScripts = data.rootScripts,
                graph = data.graph
            )
        )
    }

    //:: Engine steps
    private fun doDestructionStep() {
        destructionQueue.forEach {
            data.graph.nodes.remove(it)
        }
        destructionQueue.clear()
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.create(this, argument)
            data.graph.nodes.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun doSimulationStep(deltaTime: Float) {
        data.graph.nodes.forEach {
            it.simulate(deltaTime)
        }
    }

    //Interface :: (EngineApiAdmin)
    override fun generateId() = data.currentRuntimeId++

    override fun <T : GameNode<*, *>> createGameNode(nodeScript: KClass<T>, argument: Any?) {
        checkNodeIsRootScriptThenThrow(nodeScript)
        creationQueue.add(Pair(nodeScript, argument))
    }

    override fun <T : GameNode<*, *>> createRootScripts(gameScripts: List<KClass<T>>) {
        //Add quasars own root scripts that will be spawned alongside the game scripts by developer
        val mergeAllScripts = mutableListOf<KClass<*>>().apply {
            addAll(rootScripts)
            addAll(gameScripts)
        }.toList()

        checkUniqueRootScripts(mergeAllScripts)
        mergeAllScripts.filterIsInstance<KClass<T>>().forEach {
            createGameNode(it)
        }
        doCreationStep()
        checkScriptOrderIntegrity()

        data.rootScripts = mutableListOf()
        data.rootScripts.addAll(mergeAllScripts)

        //Check if the script is a root class and call rootCreated in ca
        data.graph.nodes.forEach {
            if (it is RootNode) {
                it.doRootCreated()
            }
        }
    }
    //Interface :: (NodeSearchable)

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        return data.graph.requireFindByInterface(nodeInterface)
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        return data.graph.findById(id, nodeInterface)
    }

    /** Utility and Helper Methods */

    private fun checkScriptOrderIntegrity() {
        data.graph.nodes.forEach { currentNode ->
            if(currentNode is RootNode) {
                val shouldBeBefore = currentNode.shouldRunBefore()
                val thisNodeIndex = data.graph.nodes.indexOf(currentNode)
                shouldBeBefore.forEach { dependClass ->
                    val dependIndex = data.graph.nodes.indexOf(data.graph.nodes.first { it::class == dependClass })
                    if(dependIndex > thisNodeIndex) {
                        throw SecurityException("${dependClass.simpleName} should be spawned before ${currentNode::class.simpleName}")
                    }
                }
            }
        }
    }

    private fun checkNodeIsRootScriptThenThrow(node: GameNode<*, *>) {
        checkNodeIsRootScriptThenThrow(node::class)
    }

    private fun checkNodeIsRootScriptThenThrow(script: KClass<*>) {
        if(data.rootScripts.contains(script)) {
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