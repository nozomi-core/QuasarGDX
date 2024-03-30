package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.scripts.QuasarRootScripts
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngineApi(private val drawableApi: DrawableApi): EngineApiAdmin {
    private var currentRuntimeId = 0L
    private val engineNodeGraph = mutableListOf<GameNode>()

    private val destructionQueue = mutableListOf<GameNode>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode>, Any?>>()

    private var rootScripts = mutableListOf<KClass<*>>()

    override fun generateId() = currentRuntimeId++

    fun simulate(deltaTime: Float) {
        doDestructionStep()
        doCreationStep()
        doSimulationStep(deltaTime)
    }

    fun draw() {
        engineNodeGraph.forEach {
            it.draw(drawableApi)
        }
    }

    private fun doDestructionStep() {
        destructionQueue.forEach {
            engineNodeGraph.remove(it)
        }
        destructionQueue.clear()
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.create(this, argument)
            engineNodeGraph.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun doSimulationStep(deltaTime: Float) {
        engineNodeGraph.forEach {
            it.simulate(deltaTime)
        }
    }

    override fun <T : GameNode> createGameNode(nodeScript: KClass<T>, argument: Any?) {
        checkNodeIsRootScriptThenThrow(nodeScript)
        creationQueue.add(Pair(nodeScript, argument))
    }

    override fun <T: Any> requireFindByInterface(typeInterface: KClass<T>): T {
        checkCastIsInterface(typeInterface)
        val first = engineNodeGraph.firstOrNull { typeInterface.java.isAssignableFrom(it.javaClass)}
        return first as T!!
    }

    override fun <T : Any> findById(id: Long, typeInterface: KClass<T>): T? {
        checkCastIsInterface(typeInterface)
        val first = engineNodeGraph.first { it.runtimeId == id && it.isAlive }
        return first as T?
    }

    //Admin Functions
    override fun <T : GameNode> createRootScripts(gameScripts: List<KClass<T>>) {
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
        engineNodeGraph.forEach {
            if(it is RootNode) {
                it.doRootCreated()
            }
        }
    }

    override fun destroyNode(node: GameNode) {
        checkNodeIsRootScriptThenThrow(node)
        destructionQueue.add(node)
    }

    private fun checkScriptOrderIntegrity() {
        engineNodeGraph.forEach { currentNode ->
            if(currentNode is RootNode) {
                val shouldBeBefore = currentNode.shouldRunBefore()
                val thisNodeIndex = engineNodeGraph.indexOf(currentNode)
                shouldBeBefore.forEach { dependClass ->
                    val dependIndex = engineNodeGraph.indexOf(engineNodeGraph.first { it::class == dependClass })
                    if(dependIndex > thisNodeIndex) {
                        throw SecurityException("${dependClass.simpleName} should be spawned before ${currentNode::class.simpleName}")
                    }
                }
            }
        }
    }

    private fun checkNodeIsRootScriptThenThrow(node: GameNode) {
        checkNodeIsRootScriptThenThrow(node::class)
    }

    private fun checkNodeIsRootScriptThenThrow(script: KClass<*>) {
        if(rootScripts.contains(script)) {
            throw SecurityException("Root scripts are immutable, you can not add or remove them")
        }
    }
}

private fun checkCastIsInterface(kClass: KClass<*>) {
    if(!kClass.java.isInterface) {
        throw SecurityException("Can only search for interfaces when finding a node in the engine")
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