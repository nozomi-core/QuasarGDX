package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

private var currentRuntimeId = 0L

//TODO: PAUSE ALL ENGINE API UNTIL WE TEST THE EXISTING FRAMEWORK
abstract class GameNode {
    val runtimeId: Long = currentRuntimeId++
    val isAlive get() = !isDestroyed
    val engineApi: EngineApi? get() = engineApiAdmin

    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false

    private var parentNode: GameNode? = null
    private var engineApiAdmin: EngineApiAdmin? = null

    private val childNodes = mutableListOf<GameNode>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode>, Any?>>()

    abstract fun onCreate(engineApi: EngineApi, argument: Any?)
    abstract fun onSimulate(deltaTime: Float)
    abstract fun onDraw(drawableApi: DrawableApi)
    open fun onDestroy() {}

    internal fun simulate(deltaTime: Float) {
        doSimulationStep(deltaTime)
        checkObjectIsBeingDestroyed()
        doCreationStep()
    }

    internal fun draw(drawableApi: DrawableApi) {
        onDraw(drawableApi)
    }

    internal fun attachToEngine(engineApi: EngineApiAdmin) {
        this.engineApiAdmin = engineApi
    }

    private fun destroy() {
        onDestroy()
        childNodes.forEach {
            it.destroy()
        }
        //The engine only needs to remove the root node, child nodes will auto remove with parent dies
        if(parentNode == null) {
            engineApiAdmin?.destroyNode(this)
        }
        isDestroyed = true
    }

    private fun doSimulationStep(deltaTime: Float) {
        onSimulate(deltaTime)
        if(!isObjectedMarkedForDestruction) {
            childNodes.forEach {
                it.simulate(deltaTime)
            }
        }
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.attachToEngine(engineApiAdmin!!)
            newEntity.onCreate(engineApi!!, argument)
            childNodes.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun checkObjectIsBeingDestroyed() {
        if(isObjectedMarkedForDestruction && !isDestroyed) {
           destroy()
        }
    }

    protected fun <T: GameNode> createChild(node: KClass<T>, argument: Any?) {
        creationQueue.add(Pair(node, argument))
    }

    protected fun destroyNode() {
        isObjectedMarkedForDestruction = true
    }
}