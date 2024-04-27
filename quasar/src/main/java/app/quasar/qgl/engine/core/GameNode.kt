package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

typealias SimulationTask<D> = (context: SimContext, self: SelfContext, data: D) -> Unit

interface ReadableGameNode {
    val isAlive: Boolean
    val id: Long
    val parent: ReadableGameNode?
}

abstract class GameNode<D>: ReadableGameNode {
    private var runtimeId: Long = -1L
    private var parentNode: GameNode<*>? = null

    //Data
    private var _data: D? = null
    protected val dataForInterface: D get() {
        return _data!!
    }

    var zDrawIndex: Int = 0
        protected set(value) {
            _engineApi?.notifyNodeChanged()
            field = value
        }

    //Meta data
    private var isObjectedMarkedForDestruction = false
    private var isDestroyed = false

    //NodeReadable
    override val isAlive get() = !isDestroyed
    override val parent: ReadableGameNode? get() = parentNode
    override val id: Long get() = runtimeId

    //Node management
    private val childGraph = NodeGraph()
    private val drawableNodes = DrawableNodeGraph(childGraph)
    private val childOverlays = DrawableOverlayGraph(childGraph)

    private val creationQueue = mutableListOf<Pair<KClass<out GameNode<*>>, Any?>>()
    private val simulationTasks = mutableListOf<SimulationTask<D>>()

    //Engine
    private val engineApi: EngineApi get() = _engineApi!!
    private var _engineApi: QuasarEngine? = null

    //Hooks
    protected abstract fun onCreate(input: NodeInput): D
    protected open fun onSetup(context: SetupContext, data: D) {}
    protected open fun onSimulate(context: SimContext, self: SelfContext, data: D) {}
    protected open fun onDraw(context: DrawContext, data: D){}
    protected open fun onDestroy() {}

    private val selfContext = object: SelfContext {
        override fun getParent(): GameNode<*> = this@GameNode

        override fun destroyNode() {
            isObjectedMarkedForDestruction = true
        }

        override fun <T : GameNode<*>> createChild(node: KClass<T>, argument: Any?) {
            creationQueue.add(Pair(node, argument))
        }

        override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
            return childGraph.requireFindByInterface(nodeInterface)
        }

        override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
            return childGraph.findById(id, nodeInterface)
        }

        override fun <T : Any> forEachInterface(nodeInterface: KClass<T>, callback: (T) -> Unit) {
            return childGraph.forEachInterface(nodeInterface, callback)
        }

        override fun <T : Any> getNearby(target: WorldPosition, distance: Float, nodeInterface: KClass<T>): List<T> {
            return childGraph.getNearby(target, distance, nodeInterface)
        }
    }

    protected fun simulationTask(callback: SimulationTask<D>) {
        simulationTasks.add(callback)
    }

    /** Engine Module */

    internal fun create(engineApiAdmin: QuasarEngine, argument: Any?) {
        this._engineApi = engineApiAdmin
        this.runtimeId = engineApiAdmin.generateId()
        _data = onCreate(NodeInput(argument))
        onSetup(SetupContext(engine = engineApi), _data!!)
        doCreationStep()
    }

    internal fun simulate(context: SimContext) {
        doSimulationStep(context)
        checkObjectIsBeingDestroyed()
        doCreationStep()
    }

    internal fun draw(context: DrawContext) {
        onDraw(context, _data!!)
        drawableNodes.draw(context)
    }

    internal fun getDataForBinary() = _data
    internal fun getChildOverlays() = childOverlays
    internal fun getChildNodes() = childGraph

    /** Engine Steps */

    private fun doSimulationStep(context: SimContext) {
        simulationTasks.forEach { it(context, selfContext, _data!!) }
        simulationTasks.clear()

        onSimulate(context, selfContext, _data!!)
        if(!isObjectedMarkedForDestruction) {
            childGraph.forEach {
                it.simulate(context)
            }
        }
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.parentNode = this
            newEntity.create(_engineApi!!, argument)
            childGraph.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun destroy() {
        onDestroy()
        childGraph.forEach {
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
        return when(other is GameNode<*>) {
            true -> other.runtimeId == this.runtimeId
            false -> false
        }
    }

    override fun hashCode(): Int {
        return runtimeId.hashCode()
    }

    override fun toString(): String {
        return "GameNode@$runtimeId"
    }
}

class NodeInput internal constructor(val value: Any?) {
    fun <I,O> map(mapper: (I) -> O): O {
        return mapper(value as I)
    }
}