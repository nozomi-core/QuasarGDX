package app.quasar.qgl.engine.core

typealias NodeFactoryCallback =(NodeFactory) -> Unit

class NodeFactory(factories: List<NodeFactoryCallback>) {
    var tag: String? = null
    var argument: NodeArgument = NullArgument

    internal var nodeId: Long? = null
    internal var engine: QuasarEngine? = null

    init {
        factories.forEach {
            it(this)
        }
    }
}