package app.quasar.qgl.engine.core

abstract class GameNode<D>: ReadableGameNode {
    private val record = NodeRecord<D>()

    internal val tag: String?
        get() = record.tag

    protected abstract fun onCreate(argument: NodeArgument): D
    protected open fun onSimulate() {}

    internal fun create(factory: NodeFactory) {
        record.tag = factory.tag
        onCreate(factory.argument)
    }

    internal fun simulate() {
        onSimulate()
    }

    internal fun nodeException(): Exception {
        return Exception()
    }
}