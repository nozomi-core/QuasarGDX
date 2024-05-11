package app.quasar.qgl.scripts

import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldBounded1
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class WorldBoundScript: GameNodeUnit1() {

    private val bounds = Rectangle(-100f, -100f, 512f, 512f)

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: Unit) {
        val engine = context.engine

        val cursor = Vector3()

        engine.forEachInterface(WorldBounded1::class) { position ->
            position.query(cursor)

            if(!bounds.contains(cursor.x, cursor.y)) {
                position.onBoundExceeded()
            }
        }
    }
}