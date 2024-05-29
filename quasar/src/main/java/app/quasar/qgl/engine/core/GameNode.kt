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

    private var parent: ReadableGameNode? = null

    protected abstract fun onCreate(argument: NodeArgument): D
    protected open fun onDestroy() {}
    protected open fun onSimulate(self: SelfContext, context: SimContext, data: D) {}
    protected open fun onDraw(context: DrawContext, data: D) {}

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
        }

        override fun destroy() {
            engine.destroyNode(this@GameNode)
        }
    }

    internal fun create(factories: List<NodeFactoryCallback>) {
        NodeFactory(factories).also { result ->
            val data = onCreate(result.argument)
            parent = result.parent

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

    internal fun doDestroy() {
        isDestroyed = true
        reference = null
        onDestroy()
    }

    internal fun simulate(context: SimContext) {
        if(parent != null && !parent!!.isAlive) {
            parent = null
            return selfContext.destroy()
        }
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