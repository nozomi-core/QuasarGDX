package app.quasar.qgl.engine.core.model

import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import app.quasar.qgl.serialize.BinProp
import com.badlogic.gdx.math.Vector3

abstract class VectorNode<D>: GameNode<D>(), WorldPosition {
    @BinProp(100000000) var position = Vector3()

    override fun queryPosition(input: Vector3): Vector3 = input.set(position)

    override fun draw(context: DrawContext) {
        if(context.inside(position)) {
            super.draw(context)
        }
    }
}