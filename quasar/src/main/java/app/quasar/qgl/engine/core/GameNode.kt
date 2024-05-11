package app.quasar.qgl.engine.core

abstract class GameNode<D>: ReadableGameNode {
    private val record = NodeRecord<D>()

    internal val tag: String?
        get() = record.tag

    protected abstract fun onCreate(argument: NodeArgument): D
    protected open fun onSimulate(self: SelfContext, context: SimContext, data: D) {}
    protected open fun onDraw(context: DrawContext, data: D) {}

    private val selfContext = object : SelfContext {}

    internal fun create(factory: NodeFactory) {
        record.tag = factory.tag
        record.data = onCreate(factory.argument)
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