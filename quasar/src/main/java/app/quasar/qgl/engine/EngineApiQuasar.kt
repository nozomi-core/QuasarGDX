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

    override fun <T : GameNode> createGameNode(node: KClass<T>, argument: Any?) {
        creationQueue.add(Pair(node, argument))
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

    override fun <T : GameNode> createRootScripts(scripts: List<KClass<T>>) {
        scripts.forEach {
            createGameNode(it)
        }
        doCreationStep()
    }

    //Admin Functions
    override fun destroyNode(node: GameNode) {
        destructionQueue.add(node)
    }
}

private fun checkCastIsInterface(kClass: KClass<*>) {
    if(!kClass.java.isInterface) {
        throw SecurityException("Can only search for interfaces when finding a node in the engine")
    }
}