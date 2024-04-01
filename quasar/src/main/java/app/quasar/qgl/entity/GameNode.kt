package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.language.GameData
import app.quasar.qgl.language.ProviderStack
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

abstract class GameNode<D, A>: NodeSearchable {
    var runtimeId: Long = -1L
        private set

    private var _data: D? = null
    val requireDataForInterface: D get() {
        engineApiAdmin?.checkNodeNotCurrentlyExecuting(this)
        return _data!!
    }

    var parentNode: GameNode<*, *>? = null
        private set

    val isAlive get() = !isDestroyed
    private val engineApi: EngineApi get() = engineApiAdmin!!

    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false

    private var engineApiAdmin: EngineApiAdmin? = null

    private val childGraph = NodeGraph()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*, *>>, Any?>>()

    private val providerList = mutableListOf<ProviderStack<*>>()

    protected open fun onCreate(argument: A?): D? { return null }
    protected open fun onSetupEngine(engine: EngineApi) {}
    protected open fun onSetupData(data: D?) {}
    protected open fun onSimulate(deltaTime: Float, data: D?) {}
    protected open fun onDraw(draw: DrawableApi){}
    protected open fun onDestroy() {}

    internal fun create(engineApiAdmin: EngineApiAdmin, argument: Any?) {
        this.engineApiAdmin = engineApiAdmin
        this.runtimeId = engineApiAdmin.generateId()
        this.engineApiAdmin?.setCurrentNodeExecuting(this)
        onSetupEngine(engineApi)
        _data = onCreate(argument as A)
        onSetupData(_data)
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
        providerList.forEach {
            it.remove(this)
        }
        providerList.clear()
        //The engine only needs to remove the root node, child nodes will auto remove with parent dies
        if(parentNode == null) {
            engineApiAdmin?.destroyNode(this)
        }
        isDestroyed = true
    }

    private fun doSimulationStep(deltaTime: Float) {
        onSimulate(deltaTime, _data)
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

    internal fun <T: GameData> providesInto(provider: ProviderStack<T>, value: T) {
        if(!providerList.contains(provider)) {
            providerList.add(provider)
            provider.push(this, value)
        }
    }

    protected fun <T: GameNode<*, *>> createChild(node: KClass<T>, argument: Any?) {
        creationQueue.add(Pair(node, argument))
    }

    protected fun destroyNode() {
        isObjectedMarkedForDestruction = true
    }

    //Searchable Interface
    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        return childGraph.requireFindByInterface(nodeInterface)
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        return childGraph.findById(id, nodeInterface)
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