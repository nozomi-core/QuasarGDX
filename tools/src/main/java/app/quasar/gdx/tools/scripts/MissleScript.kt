package app.quasar.gdx.tools.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.WorldBounded
import app.quasar.qgl.engine.core.interfaces.WorldPosition
import com.badlogic.gdx.math.Vector3

class MissileScript: GameNode<MissileData>(), WorldBounded, WorldPosition {

    override fun onCreate(input: NodeInput): MissileData {
        return input.map<MissileInput, MissileData> { arg ->
            MissileData(arg.start.cpy(), arg.speed)
        }
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: MissileData) {
        data.position.x += context.clock.multiply(data.speed)
    }

    override fun onDraw(context: DrawContext, data: MissileData) {
        context.draw.tilePx(CoreTiles.SMILE, data.position.x, data.position.y)
    }

    companion object {
        fun create(engineApi: EngineApi, argument: MissileInput) = engineApi.createGameNode(MissileScript::class, argument)
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