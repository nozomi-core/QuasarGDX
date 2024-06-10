package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.StaticPosition
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import com.badlogic.gdx.math.Vector3

class DimensionGraph(node: NodeGraph): GraphListener {
    private val drawableNodes = mutableListOf<GameNode<*>>()
    private var currentDrawDimension = EngineDimension.default()

    private val zComparator = ZIndexComparator(drawableNodes)

    init {
        node.addListener(this)
    }

    override fun onAdded(node: GameNode<*>) {
        if(node.selfDimension.id == currentDrawDimension.id) {
            drawableNodes.add(node)
            node.enter()
        }
    }

    override fun onRemoved(node: GameNode<*>) {
        if(node.selfDimension.id == currentDrawDimension.id) {
            drawableNodes.remove(node)
            node.exit()
        }
    }

    internal fun notifyDimensionChanged(node: GameNode<*>) {
        if(node.selfDimension.id == currentDrawDimension.id) {
            node.exit()
            drawableNodes.add(node)
        } else {
            val wasRemoved = drawableNodes.remove(node)
            if(wasRemoved) {
                node.exit()
            }
        }
    }

    internal fun draw(context: DrawContext) {
        sortZIndex()
        drawableNodes.forEach {
            it.draw(context)
        }
    }

    internal fun setDimension(dimen: EngineDimension, graph: NodeGraph) {
        currentDrawDimension = dimen
        drawableNodes.forEach { it.exit() }
        drawableNodes.clear()
        drawableNodes.addAll(graph.getNodesWithDimension(currentDrawDimension))
        drawableNodes.forEach { it.enter() }
    }

    //TODO: test optimisation so minimise sort calls per frame
    internal fun sortZIndex() {
        drawableNodes.sortWith(zComparator)
    }
}

//TODO: Consider m oving this and adding a proper test to ensure z index are ordered in a stable manner
class ZIndexComparator(private val drawableNodes: List<GameNode<*>>): Comparator<GameNode<*>> {
    private val query = Vector3()

    override fun compare(first: GameNode<*>?, second: GameNode<*>?): Int {
        val isFirstVector = isVector(first)
        val isSecondVector = isVector(second)

        return if(isFirstVector && isSecondVector) {
            val firstZ = getZIndex(first)
            val secondZ = getZIndex(second)

            if(firstZ > secondZ) {
                -1
            } else {
                1
            }
        } else if (isFirstVector) {
            1
        } else if(isSecondVector) {
            -1
        }
        else {
            val firstIndex = drawableNodes.indexOf(first)
            val secondIndex = drawableNodes.indexOf(second)

            firstIndex - secondIndex
        }
    }

    private fun getZIndex(node: GameNode<*>?): Float {
        return when(node) {
            is StaticPosition -> node.getPosition().z
            is WorldPosition -> {
                node.queryPosition(query)
                query.z
            }
            else -> 0f
        }
    }

    private fun isVector(node: GameNode<*>?): Boolean {
        val isStatic = node is StaticPosition
        if(isStatic) {
            return true
        }

        val isPosition = node is WorldPosition
        if(isPosition) {
            return true
        }

        return false
    }
}