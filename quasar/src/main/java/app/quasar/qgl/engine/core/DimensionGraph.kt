package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.StaticPosition
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import com.badlogic.gdx.math.Vector3

class DimensionGraph(node: NodeGraph): GraphListener {
    private val dimensionNodes = mutableListOf<GameNode<*>>()
    private var currentDimension = EngineDimension.default()

    private val zComparator = ZIndexComparator(dimensionNodes)

    init {
        node.addListener(this)
    }

    override fun onAdded(node: GameNode<*>) {
        if(node.selfDimension.id == currentDimension.id) {
            dimensionNodes.add(node)
            node.enter()
        }
    }

    override fun onRemoved(node: GameNode<*>) {
        if(node.selfDimension.id == currentDimension.id) {
            dimensionNodes.remove(node)
            node.exit()
        }
    }

    internal fun notifyDimensionChanged(node: GameNode<*>) {
        if(node.selfDimension.id == currentDimension.id) {
            node.enter()
            dimensionNodes.add(node)
        } else {
            val wasRemoved = dimensionNodes.remove(node)
            if(wasRemoved) {
                node.exit()
            }
        }
    }

    internal fun draw(context: DrawContext) {
        sortZIndex()
        dimensionNodes.forEach {
            it.draw(context)
        }
    }

    internal fun setDimension(dimen: EngineDimension, graph: NodeGraph) {
        currentDimension = dimen
        dimensionNodes.forEach { it.exit() }
        dimensionNodes.clear()
        dimensionNodes.addAll(graph.getNodesWithDimension(currentDimension))
        dimensionNodes.forEach { it.enter() }
    }

    //TODO: test optimisation so minimise sort calls per frame
    internal fun sortZIndex() {
        dimensionNodes.sortWith(zComparator)
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