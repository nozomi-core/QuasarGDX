package app.quasar.qgl.engine

import app.quasar.qgl.entity.*
import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.scripts.QuasarRootScripts
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngineApi(private val drawableApi: DrawableApi): EngineApiAdmin, NodeSearchable {
    private var currentRuntimeId = 0L
    private val graph = NodeGraph()

    private val destructionQueue = mutableListOf<GameNode<*,*>>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*,*>>, Any?>>()

    private var rootScripts = mutableListOf<KClass<*>>()

    private var currentNodeExecuting = -1L

    override fun generateId() = currentRuntimeId++

    fun simulate(deltaTime: Float) {
        doDestructionStep()
        doCreationStep()
        doSimulationStep(deltaTime)
    }

    fun draw() {
        graph.gameNodes.forEach {
            it.draw(drawableApi)
        }
    }

    override fun setCurrentNodeExecuting(node: GameNode<*, *>) {
        this.currentNodeExecuting = node.runtimeId
    }

    override fun checkNodeNotCurrentlyExecuting(node: GameNode<*, *>) {
        if(currentNodeExecuting == node.runtimeId) {
            throw IllegalAccessException("Can not perform this operation while current node executing")
        }
    }

    private fun doDestructionStep() {
        destructionQueue.forEach {
            graph.gameNodes.remove(it)
        }
        destructionQueue.clear()
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.create(this, argument)
            graph.gameNodes.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun doSimulationStep(deltaTime: Float) {
        graph.gameNodes.forEach {
            it.simulate(deltaTime)
        }
    }

    override fun <T : GameNode<*,*>> createGameNode(nodeScript: KClass<T>, argument: Any?) {
        checkNodeIsRootScriptThenThrow(nodeScript)
        creationQueue.add(Pair(nodeScript, argument))
    }

    //Admin Functions
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

        //Iterate through nodes and if they are root script, call the post setup method so they can init dependancy on tree
        graph.gameNodes.forEach {
            if(it is RootNode) {
                it.doRootCreated()
            }
        }
    }

    override fun destroyNode(node: GameNode<*,*>) {
        checkNodeIsRootScriptThenThrow(node)
        destructionQueue.add(node)
    }

    private fun checkScriptOrderIntegrity() {
        graph.gameNodes.forEach { currentNode ->
            if(currentNode is RootNode) {
                val shouldBeBefore = currentNode.shouldRunBefore()
                val thisNodeIndex = graph.gameNodes.indexOf(currentNode)
                shouldBeBefore.forEach { dependClass ->
                    val dependIndex = graph.gameNodes.indexOf(graph.gameNodes.first { it::class == dependClass })
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

    //Node Searchable API

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        return graph.requireFindByInterface(nodeInterface)
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        return graph.findById(id, nodeInterface)
    }
}

private fun checkUniqueRootScripts(scripts: List<KClass<*>>) {
    val checkDuplicates = HashSet<KClass<*>>()
    scripts.forEach {
        if(checkDuplicates.contains(it)) {
            throw IllegalArgumentException("Cannot have duplicate classes in root scripts, only 1 instance shall be spawned")
        }
        checkDuplicates.add(it)
    }
}