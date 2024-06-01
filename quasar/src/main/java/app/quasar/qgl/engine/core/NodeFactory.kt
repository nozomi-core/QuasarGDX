package app.quasar.qgl.engine.core

typealias NodeFactoryCallback = (NodeFactory) -> Unit

class NodeFactory(factories: List<NodeFactoryCallback>) {
    var tag: String = ""
    var argument: NodeArgument = NullArgument
    var dimension: EngineDimension? = null
    var parent: ReadableGameNode? = null

    internal var nodeId: Long? = null
    internal var engine: QuasarEngine? = null

    init {
        factories.forEach {
            it(this)
        }
    }
}