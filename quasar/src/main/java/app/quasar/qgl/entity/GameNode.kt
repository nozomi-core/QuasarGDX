package app.quasar.qgl.entity

import app.quasar.qgl.engine.EngineApiAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.language.ProviderStack
import app.quasar.qgl.render.DrawableApi
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

abstract class GameNode: NodeSearchable {
    var runtimeId: Long = -1L
        private set

    var parentNode: GameNode? = null
        private set

    val isAlive get() = !isDestroyed
    val engineApi: EngineApi get() = engineApiAdmin!!

    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false

    private var engineApiAdmin: EngineApiAdmin? = null

    private val childGraph = NodeGraph()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode>, Any?>>()

    private val providerList = mutableListOf<ProviderStack<*>>()

    protected open fun onCreate(engine: EngineApi, argument: Any?) {}
    protected open fun onSimulate(deltaTime: Float) {}
    protected open fun onDraw(draw: DrawableApi){}
    protected open fun onDestroy() {}

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
        onSimulate(deltaTime)
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

    internal fun <T> providesInto(provider: ProviderStack<T>, value: T) {
        if(!providerList.contains(provider)) {
            providerList.add(provider)
            provider.push(this, value)
        }
    }

    protected fun <T: GameNode> createChild(node: KClass<T>, argument: Any?) {
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
        return when(other is GameNode) {
            true -> other.runtimeId == this.runtimeId
            false -> false
        }
    }

    override fun toString(): String {
        return "GameNode@$runtimeId"
    }
}