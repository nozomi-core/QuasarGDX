package app.quasar.qgl.engine.core

/**
 * Mirrors a [NodeGraph]
 * TODO: find a better data structure for more optimal sorting on render priority
 */
class DrawableNodeGraph(nodeGraph: NodeGraph): GraphChangedListener {

    private val comparable = compareBy<GameNode<*>> { it.renderPriority }
    private var markNodeGraphChanged = false

    init {
        nodeGraph.addListener(this)
    }

    private val drawableNodes = mutableListOf<GameNode<*>>()

    override fun onAdded(node: GameNode<*>) {
        drawableNodes.add(node)
        notifyNodeChanged()
    }

    override fun onRemoved(node: GameNode<*>) {
        drawableNodes.remove(node)
        notifyNodeChanged()
    }

    fun forEach(callback: (GameNode<*>) -> Unit) {
        drawableNodes.forEach(callback)
    }

    fun notifyNodeChanged() {
        markNodeGraphChanged = true
    }

    fun draw(context: DrawContext) {
        if(markNodeGraphChanged) {
            drawableNodes.sortWith(comparable)
            markNodeGraphChanged = false
        }
        drawableNodes.forEach {
            it.draw(context)
        }
    }
}