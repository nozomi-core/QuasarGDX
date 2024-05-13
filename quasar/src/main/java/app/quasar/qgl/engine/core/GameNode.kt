package app.quasar.qgl.engine.core

abstract class GameNode<D>: ReadableGameNode {

    override val isAlive: Boolean
        get() = !isDestroyed

    internal var reference: NodeReference<ReadableGameNode>? = NodeReference(this)

    internal var record = NodeRecord<D>()
        private set

    private lateinit var engine: QuasarEngine
    private var isDestroyed = false

    protected abstract fun onCreate(argument: NodeArgument): D
    protected open fun onDestroy() {}
    protected open fun onSimulate(self: SelfContext, context: SimContext, data: D) {}
    protected open fun onDraw(context: DrawContext, data: D) {}

    private val selfContext = object : SelfContext {
        override fun destroy() {
            engine.destroyNode(this@GameNode)
        }
    }

    internal fun create(factories: List<NodeFactoryCallback>) {
        NodeFactory(factories).apply {
            this.engine = engine!!
            record.nodeId = nodeId
            record.tag = tag
            record.data = onCreate(argument)
        }
    }

    internal fun destroy() {
        isDestroyed = true
        reference = null
        onDestroy()
    }

    internal fun simulate(context: SimContext) {
        onSimulate(selfContext, context, record.data!!)
    }
    internal fun draw(context: DrawContext) {
        onDraw(context, record.data!!)
    }

    internal fun nodeException(): Exception {
        return Exception()
    }
}