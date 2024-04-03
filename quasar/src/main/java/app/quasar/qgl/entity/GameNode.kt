package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

abstract class GameNode<D, A> {
    var runtimeId: Long = -1L
        private set

    private var parentNode: GameNode<*, *>? = null

    //Data
    private var _data: D? = null
    val requireDataForInterface: D get() {
        _engineApi?.checkNodeIsNotRunning(this)
        return _data!!
    }

    //Meta data
    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false
    val isAlive get() = !isDestroyed

    //Node management
    private val childGraph = NodeGraph()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*, *>>, Any?>>()

    //Engine
    private val engineApi: EngineApi get() = _engineApi!!
    private var _engineApi: EngineApiAdmin? = null

    //Hooks
    protected open fun onCreateData(argument: A?): D? { return null }
    protected open fun onCreateChildren(node: NodeApi){}
    protected open fun onSetupEngine(engine: EngineApi) {}
    protected open fun onSetupData(data: D?) {}
    protected open fun onSimulate(node: NodeApi, deltaTime: Float, data: D?) {}
    protected open fun onDraw(draw: DrawableApi){}
    protected open fun onDestroy() {}

    private val nodeApi = object: NodeApi {
        override fun getParent(): GameNode<*, *> = this@GameNode

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

    /** Engine Module */

    internal fun create(engineApiAdmin: EngineApiAdmin, argument: Any?) {
        this._engineApi = engineApiAdmin
        this.runtimeId = engineApiAdmin.generateId()
        this._engineApi?.setCurrentNodeRunning(this)
        onSetupEngine(engineApi)
        _data = onCreateData(argument as A)
        onSetupData(_data)
        onCreateChildren(nodeApi)
        doCreationStep()
    }

    internal fun simulate(deltaTime: Float) {
        this._engineApi?.setCurrentNodeRunning(this)
        doSimulationStep(deltaTime)
        checkObjectIsBeingDestroyed()
        doCreationStep()
    }

    internal fun draw(drawableApi: DrawableApi) {
        this._engineApi?.setCurrentNodeRunning(this)
        onDraw(drawableApi)
    }

    /** Engine Steps */

    private fun doSimulationStep(deltaTime: Float) {
        onSimulate(nodeApi, deltaTime, _data)
        if(!isObjectedMarkedForDestruction) {
            childGraph.nodes.forEach {
                it.simulate(deltaTime)
            }
        }
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.parentNode = this
            newEntity.create(_engineApi!!, argument)
            childGraph.nodes.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun destroy() {
        onDestroy()
        childGraph.nodes.forEach {
            it.destroy()
        }
        //The engine only needs to remove the root node, child nodes will auto remove with parent dies
        if(parentNode == null) {
            _engineApi?.destroyNode(this)
        }
        isDestroyed = true
    }

    /** Utility and Helper Methods */

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