package app.quasar.qgl.engine.core

class NodeFactory(factory: (NodeFactory) -> Unit) {

    init {
        factory(this)
    }

    var tag: String? = null
    var argument: NodeArgument = NullArgument
}