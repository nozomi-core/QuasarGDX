package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.SpinnerData
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.math.Vector3

@QGLEntity("spinner")
class SpinnerScript: GameNode<SpinnerData>() {
    private var position = Vector3(64f, 64f, 0f)
    private var rotation: Float = 0f

    override fun onCreate(argument: NodeArgument): SpinnerData {
        return SpinnerData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: SpinnerData) {
        val allNodes = context.engine.queryAll()

        allNodes.forEach { ref ->
            val nearQuery = Vector3()

            ref.get()?.let {  node ->
                if(node is WorldPosition) {
                    node.queryPosition(nearQuery)
                    if(nearQuery.dst(position) < 64) {
                        rotation += 15
                    }
                }
            }
        }
    }

    override fun onDraw(context: DrawContext, data: SpinnerData) {
        context.draw.tilePx(CoreTiles.SMILE, position, 1f, rotation)
    }
}