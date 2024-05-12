package app.quasar.qgl.engine.core

class NodeFactory(factory: (NodeFactory) -> Unit) {
    var tag: String? = null
    var argument: NodeArgument = NullArgument

    init {
        factory(this)
    }
}