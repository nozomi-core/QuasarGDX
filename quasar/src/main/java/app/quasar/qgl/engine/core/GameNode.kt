package app.quasar.qgl.engine.core

abstract class GameNode<D>: ReadableGameNode {

    override val isAlive: Boolean
        get() = !isDestroyed
    override val nodeId: Long
        get() = record.nodeId
    override val tag: String
        get() = record.tag
    override val selfDimension: EngineDimension
        get() = record.dimension

    internal var reference: NodeReference<ReadableGameNode>? = NodeReference(this)

    internal lateinit  var record: NodeRecord<D>

    private lateinit var engine: QuasarEngine
    private var isDestroyed = false

    protected abstract fun onCreate(argument: NodeArgument): D
    protected open fun onDestroy() {}
    protected open fun onSimulate(self: SelfContext, context: SimContext, data: D) {}
    protected open fun onDraw(context: DrawContext, data: D) {}

    private val selfContext = object : SelfContext {
        override fun setDimension(dimension: EngineDimension) {
            record.dimension = dimension
        }

        override fun destroy() {
            engine.destroyNode(this@GameNode)
        }
    }

    internal fun create(factories: List<NodeFactoryCallback>) {
        NodeFactory(factories).also { result ->
            val data = onCreate(result.argument)

            record = NodeRecord(
                nodeId = result.nodeId!!,
                tag = result.tag,
                data = data,
                dimension = result.dimension!!
            )
        }
    }

    internal fun attachEngine(engine: QuasarEngine) {
        this.engine = engine
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