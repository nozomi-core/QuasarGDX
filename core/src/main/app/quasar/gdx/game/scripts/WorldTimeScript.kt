package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.ScriptTypes
import app.quasar.gdx.game.logic.doWorldTime
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.serialize.EngineRef
import app.quasar.qgl.engine.serialize.EngineScript
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper
import org.joda.time.MutableDateTime

interface WorldTime {
    fun getGameMillis(): Long
    fun getTimeStamp(): String
}

@EngineScript(ScriptTypes.WORLD_TIME)
class WorldTimeScript: GameNode<WorldTimeData>(), WorldTime {

    //Nodes
    @EngineRef(NodeTypes.ENGINE_LOGGER)
    lateinit var logger: EngineLogger private set

    //Interface
    override fun getGameMillis() = dataForInterface.gameTime.millis
    override fun getTimeStamp() = ""

    override fun onSetup(context: SetupContext, data: WorldTimeData) {
        logger = context.engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onCreate(input: NodeInput): WorldTimeData {
        return input.map<WorldTimeInput, WorldTimeData> { arg ->
            WorldTimeData(
                gameSpeed = arg.gameSpeed,
                gameTime = MutableDateTime(arg.startTime)
            )
        }
    }

    override fun onSimulate(context: SimContext, self: SelfContext, data: WorldTimeData) {
        doWorldTime(
            this,  context.clock.deltaTime, data
        )
    }

    private object NodeTypes {
        const val ENGINE_LOGGER = 1
    }
}

//DATA

data class WorldTimeData(
    val gameTime: MutableDateTime,
    var gameSpeed: Float = WorldTimeInput.DEFAULT_SPEED
)

//ARGUMENT

data class WorldTimeInput(
    val gameSpeed: Float,
    val startTime: Long
) {
    companion object {
        const val DEFAULT_SPEED = 1000000f
    }
}

//MAPPER

class WorldTimeMapper: QGLMapper<WorldTimeData> {
    override fun toBinary(data: WorldTimeData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_GAME_TIME, data.gameTime.millis),
            BinaryRecord(ID_GAME_SPEED, data.gameSpeed)
        )
    }

    override fun toEntity(bin: BinaryObject): WorldTimeData {
        return WorldTimeData(
            gameTime = MutableDateTime(bin.value<Long>(ID_GAME_TIME)),
            gameSpeed = bin.value(ID_GAME_SPEED)
        )
    }

    companion object {
        const val ID_GAME_TIME = 0
        const val ID_GAME_SPEED = 1
    }
}