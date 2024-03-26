package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

//TODO: PAUSE ALL ENGINE API UNTIL WE TEST THE EXISTING FRAMEWORK
abstract class GameNode {
    var runtimeId: Long = -1L
        private set

    val isAlive get() = !isDestroyed
    val engineApi: EngineApi get() = engineApiAdmin!!

    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false

    var parentNode: GameNode? = null
        private set
    private var engineApiAdmin: EngineApiAdmin? = null

    private val childNodes = mutableListOf<GameNode>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode>, Any?>>()

    protected open fun onCreate(engineApi: EngineApi, argument: Any?) {}
    protected open fun onSimulate(deltaTime: Float) {}
    protected open fun onDraw(drawableApi: DrawableApi){}
    protected open fun onDestroy() {}

    /*
    * TODO:: Should we have these, can be used to query nearby nodes
    *   var x: Float
    *   var y: Float
    *   var geoHash = ....
    * */

    internal fun create(engineApiAdmin: EngineApiAdmin, argument: Any?) {
        this.engineApiAdmin = engineApiAdmin
        this.runtimeId = engineApiAdmin.generateId()
        onCreate(engineApi, argument)
    }

    internal fun simulate(deltaTime: Float) {
        doSimulationStep(deltaTime)
        checkObjectIsBeingDestroyed()
        doCreationStep()
    }

    internal fun draw(drawableApi: DrawableApi) {
        onDraw(drawableApi)
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
            newEntity.parentNode = this
            newEntity.create(engineApiAdmin!!, argument)
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