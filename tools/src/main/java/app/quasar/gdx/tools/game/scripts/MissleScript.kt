package app.quasar.gdx.tools.game.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core1.*
import app.quasar.qgl.engine.core1.interfaces.WorldBounded1
import app.quasar.qgl.engine.core1.interfaces.WorldPosition1
import com.badlogic.gdx.math.Vector3

interface Missile: WorldPosition1, WorldBounded1

class MissileScript: GameNode1<MissileData>(), Missile {

    override fun onCreate(input: NodeInput1): MissileData {
        return input.map<MissileInput, MissileData> { arg ->
            MissileData(arg.start.cpy(), arg.speed)
        }
    }

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: MissileData) {
        data.position.x += context.clock.multiply(data.speed)
    }

    override fun onDraw(context: DrawContext1, data: MissileData) {
        context.draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y)
    }

    companion object {
        fun create(engineApi: EngineApi1, argument: MissileInput) = engineApi.createNode(MissileScript::class, argument)
    }

    override fun onBoundExceeded() {
        simulationTask { context, self, data ->
            self.destroyNode()
        }
    }

    override fun query(input: Vector3): Vector3 = input.set(dataForInterface.position)

}

data class MissileData(
    val position: Vector3,
    val speed: Float
)
data class MissileInput(
    val start: Vector3,
    val speed: Float
)