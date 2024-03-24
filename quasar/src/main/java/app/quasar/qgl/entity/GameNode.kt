package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

private var currentRuntimeId = 0L

abstract class GameNode {
    val runtimeId: Long = currentRuntimeId++
    val isAlive get() = !isObjectDestroyedInEngine
    val engineApi: EngineApi? get() = engineApiAdmin

    private var isObjectDestroyedInEngine = false


    private var parentNode: GameNode? = null
    private var engineApiAdmin: EngineApiAdmin? = null


    private val childNodes = mutableListOf<GameNode>()

    abstract fun onCreate(engineApi: EngineApi)
    abstract fun onSimulate(deltaTime: Float)
    abstract fun onDraw(drawableApi: DrawableApi)
    open fun onDestroy() {}

    internal fun attachToEngine(engineApi: EngineApiAdmin) {
        this.engineApiAdmin = engineApi
    }

    protected fun <T: GameNode> createChild(node: KClass<T>) {
        val newChild = node.createInstance()
        newChild.parentNode = this
        childNodes.add(newChild)
        newChild.onCreate(engineApiAdmin!!)
    }

    protected fun drawChildren(drawableApi: DrawableApi) {
        childNodes.forEach {
            it.onDraw(drawableApi)
        }
    }

    protected fun destroyNode() {
        isObjectDestroyedInEngine = true
        onDestroy()
        engineApiAdmin?.destroyNode(this)
    }
}