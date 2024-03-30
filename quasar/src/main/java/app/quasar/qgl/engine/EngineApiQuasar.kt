package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.render.DrawableApi
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
    override fun <T : GameNode> createRootScripts(scripts: List<KClass<T>>) {
        checkUniqueRootScripts(scripts)
        scripts.forEach {
            createGameNode(it)
        }
        doCreationStep()
        rootScripts = mutableListOf()
        rootScripts.addAll(scripts)
    }

    override fun destroyNode(node: GameNode) {
        checkNodeIsRootScriptThenThrow(node)
        destructionQueue.add(node)
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

private fun <T : GameNode> checkUniqueRootScripts(scripts: List<KClass<T>>) {
    val checkDuplicates = HashSet<KClass<T>>()
    scripts.forEach {
        if(checkDuplicates.contains(it)) {
            throw IllegalArgumentException("Cannot have duplicate classes in root scripts, only 1 instance shall be spawned")
        }
        checkDuplicates.add(it)
    }
}