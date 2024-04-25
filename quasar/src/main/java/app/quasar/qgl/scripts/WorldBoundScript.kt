package app.quasar.qgl.scripts

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldBounded
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class WorldBoundScript: GameNodeUnit() {

    private val bounds = Rectangle(-100f, -100f, 512f, 512f)

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        val engine = context.engine

        val cursor = Vector3()

        engine.forEachInterface(WorldBounded::class) { position ->
            position.query(cursor)

            if(!bounds.contains(cursor.x, cursor.y)) {
                position.onBoundExceeded()
            }
        }
    }
}