package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNodeUnit
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import com.badlogic.gdx.math.Vector3

class ProximityScript: GameNodeUnit(), WorldPosition {

    private val position = Vector3(64f, 64f, 0f)
    private var isActive = false

    override fun onDraw(context: DrawContext, data: Unit) {
        val tile = if(isActive) CoreTiles.SIGNAL_CLOSE else CoreTiles.SIGNAL_REGULAR
        context.draw.tilePx(tile, position)
    }

    override fun query(input: Vector3) {
        input.set(position)
    }
}