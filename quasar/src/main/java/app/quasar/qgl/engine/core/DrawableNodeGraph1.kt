package app.quasar.qgl.engine.core

/**
 * Mirrors a [NodeGraph1]
 * TODO: find a better data structure for more optimal sorting on render priority
 */
class DrawableNodeGraph1(nodeGraph: NodeGraph1): GraphChangedListener1 {

    private val comparable = compareBy<NodeReference1<GameNode1<*>>> { it.get()?.zDrawIndex }
    private var markNodeGraphChanged = false

    init {
        nodeGraph.addListener(this)
    }

    private val drawableNodes = NodeCollection1<GameNode1<*>>()

    override fun onAdded(node: GameNode1<*>) {
        drawableNodes.add(node)
        notifyNodeChanged()
    }

    override fun onRemoved(node: GameNode1<*>) {
        drawableNodes.remove(node)
        notifyNodeChanged()
    }

    fun notifyNodeChanged() {
        markNodeGraphChanged = true
    }

    fun draw(context: DrawContext1) {
        if(markNodeGraphChanged) {
            drawableNodes.sortWith(comparable)
            markNodeGraphChanged = false
        }
        drawableNodes.forEach {
            it.get()?.draw(context)
        }
    }
}