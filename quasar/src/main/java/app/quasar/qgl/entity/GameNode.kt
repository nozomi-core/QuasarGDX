package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

abstract class GameNode<D, A> {
    var runtimeId: Long = -1L
        private set

    private var _data: D? = null
    val requireDataForInterface: D get() {
        engineApiAdmin?.checkNodeNotCurrentlyExecuting(this)
        return _data!!
    }

    private var parentNode: GameNode<*, *>? = null

    val isAlive get() = !isDestroyed
    private val engineApi: EngineApi get() = engineApiAdmin!!

    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false

    private var engineApiAdmin: EngineApiAdmin? = null

    private val childGraph = NodeGraph()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*, *>>, Any?>>()

    protected open fun onCreateData(argument: A?): D? { return null }
    protected open fun onCreateChildren(nodeApi: NodeApi){}
    protected open fun onSetupEngine(engine: EngineApi) {}
    protected open fun onSetupData(data: D?) {}
    protected open fun onSimulate(nodeApi: NodeApi, deltaTime: Float, data: D?) {}
    protected open fun onDraw(draw: DrawableApi){}
    protected open fun onDestroy() {}

    private val nodeApi = object: NodeApi {
        override fun destroyNode() {
            isObjectedMarkedForDestruction = true
        }

        override fun <T : GameNode<*, *>> createChild(node: KClass<T>, argument: Any?) {
            creationQueue.add(Pair(node, argument))
        }

        override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
            return childGraph.requireFindByInterface(nodeInterface)
        }

        override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
            return childGraph.findById(id, nodeInterface)
        }
    }

    internal fun create(engineApiAdmin: EngineApiAdmin, argument: Any?) {
        this.engineApiAdmin = engineApiAdmin
        this.runtimeId = engineApiAdmin.generateId()
        this.engineApiAdmin?.setCurrentNodeExecuting(this)
        onSetupEngine(engineApi)
        _data = onCreateData(argument as A)
        onSetupData(_data)
        onCreateChildren(nodeApi)
        doCreationStep()
    }

    internal fun simulate(deltaTime: Float) {
        this.engineApiAdmin?.setCurrentNodeExecuting(this)
        doSimulationStep(deltaTime)
        checkObjectIsBeingDestroyed()
        doCreationStep()
    }

    internal fun draw(drawableApi: DrawableApi) {
        this.engineApiAdmin?.setCurrentNodeExecuting(this)
        onDraw(drawableApi)
    }

    private fun destroy() {
        onDestroy()
        childGraph.gameNodes.forEach {
            it.destroy()
        }
        //The engine only needs to remove the root node, child nodes will auto remove with parent dies
        if(parentNode == null) {
            engineApiAdmin?.destroyNode(this)
        }
        isDestroyed = true
    }

    private fun doSimulationStep(deltaTime: Float) {
        onSimulate(nodeApi, deltaTime, _data)
        if(!isObjectedMarkedForDestruction) {
            childGraph.gameNodes.forEach {
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
            childGraph.gameNodes.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun checkObjectIsBeingDestroyed() {
        if(isObjectedMarkedForDestruction && !isDestroyed) {
           destroy()
        }
    }

    //Java Interface
    override fun equals(other: Any?): Boolean {
        return when(other is GameNode<*, *>) {
            true -> other.runtimeId == this.runtimeId
            false -> false
        }
    }

    override fun toString(): String {
        return "GameNode@$runtimeId"
    }
}