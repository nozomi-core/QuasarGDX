package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import com.badlogic.gdx.math.Vector3

class ProximityScript: GameNodeUnit(), WorldPosition {

    private val position = Vector3(64f, 64f, 0f)
    private var isActive = false
    
    private lateinit var player: Player

    override fun onSetup(context: SetupContext, data: Unit) {
        player = context.engine.requireFindByInterface(Player::class)
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: Unit) {
        val playerPosition = player.query(Vector3())
        isActive = playerPosition.dst(position) < 32f
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        val tile = if(isActive) CoreTiles.SIGNAL_CLOSE else CoreTiles.SIGNAL_REGULAR
        context.draw.tilePx(tile, position)
    }

    override fun query(input: Vector3): Vector3 = input.set(position)
}