package app.quasar.qgl.engine.core

import kotlin.reflect.KClass

abstract class GameNode<D>: ReadableGameNode {

    override val isAlive: Boolean
        get() = !isDestroyed
    override val nodeId: Long
        get() = record.nodeId
    override val tag: String
        get() = record.tag
    override val selfDimension: EngineDimension
        get() = record.dimension

    protected val requireForInterface: D
        get() = record.data

    internal var reference: NodeReference<ReadableGameNode>? = NodeReference(this)

    internal lateinit  var record: NodeRecord<D>

    private lateinit var engine: QuasarEngine
    private var isDestroyed = false

    private var parent: GameNode<*>? = null

    protected abstract fun onCreate(argument: NodeArgument): D
    protected open fun onDestroy() {}
    protected open fun onSimulate(self: SelfContext, context: SimContext, data: D) {}
    protected open fun onDraw(context: DrawContext, data: D) {}

    private val childList = mutableListOf<GameNode<*>>()

    private val selfContext = object : SelfContext {
        override fun <T : GameNode<*>> spawnChild(
            dimension: EngineDimension,
            script: KClass<T>,
            factory: (NodeFactory) -> Unit
        ) {
            engine.createNode(dimension, script) {
                it.parent = this@GameNode
                factory(it)
            }
        }

        override fun setDimension(dimension: EngineDimension) {
            record.dimension = dimension
            engine.notifyDimensionChanged(this@GameNode)
        }

        override fun destroy() {
            engine.destroyNode(this@GameNode)
        }
    }

    internal fun create(factories: List<NodeFactoryCallback>) {
        NodeFactory(factories).also { result ->
            val data = onCreate(result.argument)
            parent = result.parent as? GameNode<*>

            record = NodeRecord(
                nodeId = result.nodeId!!,
                tag = result.tag,
                data = data,
                dimension = result.dimension!!
            )
            //call attach child to parent if this child was created from a parent node
            parent?.attachChild(this)
        }
    }

    internal fun attachEngine(engine: QuasarEngine) {
        this.engine = engine
    }

    private fun attachChild(childNode: GameNode<*>) {
        childList.add(childNode)
    }

    private fun detachChild(childNode: GameNode<*>) {
        childList.remove(childNode)
    }

    internal fun doDestroy() {
        isDestroyed = true
        reference = null
        onDestroy()
        childList.forEach {
            it.selfContext.destroy()
        }
        childList.clear()

        parent?.detachChild(this)
        parent = null
    }

    internal fun simulate(context: SimContext) {
        onSimulate(selfContext, context, record.data!!)
    }
    internal open fun draw(context: DrawContext) {
        onDraw(context, record.data!!)
    }

    fun creationException(): Exception {
        return Exception()
    }

    internal fun nodeException(): Exception {
        return Exception()
    }

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}