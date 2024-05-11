package app.quasar.gdx.tools.game.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core1.*
import app.quasar.qgl.engine.core1.interfaces.WorldPosition1
import com.badlogic.gdx.math.Vector3

interface Proximity: WorldPosition1

class ProximityScript: GameNodeUnit1(), Proximity {

    private val position = Vector3(64f, 64f, 0f)
    private var isActive = false
    
    private lateinit var player: Player

    override fun onSetup(context: SetupContext1, data: Unit) {
        player = context.engine.requireFindByInterface(Player::class)
    }

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: Unit) {
        if(context.clock.tick32) {
            val playerPosition = player.query(Vector3())
            isActive = playerPosition.dst(position) < 32f
        }
    }

    override fun onDraw(context: DrawContext1, data: Unit) {
        val tile = if(isActive) CoreTiles.SIGNAL_CLOSE else CoreTiles.SIGNAL_REGULAR
        context.draw.tilePx(tile, position)
    }

    override fun query(input: Vector3): Vector3 = input.set(position)
}