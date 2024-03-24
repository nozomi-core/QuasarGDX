package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngineApi(private val drawableApi: DrawableApi): EngineApiAdmin {

    private var isSimulating = false

    private val gameNodes = mutableListOf<GameNode>()

    //List of game nodes that need to be destroyed on the next frame
    private val willBeDestroyed = mutableListOf<GameNode>()
    private val willBeCreated = mutableListOf<GameNode>()

    fun simulate(deltaTime: Float) {
        isSimulating = true
        willBeDestroyed.forEach { gameNodes.remove(it) }
        willBeCreated.forEach {
            gameNodes.add(it)
                it.onCreate(this)
            }

        gameNodes.forEach {
            it.onSimulate(deltaTime)
        }
        isSimulating = false
    }

    fun draw() {
        gameNodes.forEach {
            it.onDraw(drawableApi)
        }
    }

    override fun <T : GameNode> createGameNode(node: KClass<T>): T {
        val newEntity = node.createInstance()
        newEntity.attachToEngine(this)

        if(isSimulating) {
            willBeCreated.add(newEntity)
        } else {
            newEntity.onCreate(this)
            gameNodes.add(newEntity)
        }
        return newEntity
    }

    override fun <T: Any> requireFindByInterface(typeInterface: KClass<T>): T {
        checkCastIsInterface(typeInterface)
        val first = gameNodes.firstOrNull { typeInterface.java.isAssignableFrom(it.javaClass)}
        return first as T!!
    }

    override fun <T : Any> findById(id: Long, typeInterface: KClass<T>): T? {
        checkCastIsInterface(typeInterface)
        val first = gameNodes.first { it.runtimeId == id && it.isAlive }
        return first as T?
    }

    //Admin Functions
    override fun destroyNode(node: GameNode) {
        willBeDestroyed.add(node)
    }
}

private fun checkCastIsInterface(kClass: KClass<*>) {
    if(!kClass.java.isInterface) {
        throw SecurityException("Can only search for interfaces when finding a node in the engine")
    }
}