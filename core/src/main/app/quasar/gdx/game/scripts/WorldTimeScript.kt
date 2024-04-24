package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.ScriptTypes
import app.quasar.gdx.game.logic.doWorldTime
import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.engine.core.EngineClock
import app.quasar.qgl.engine.serialize.EngineRef
import app.quasar.qgl.engine.serialize.EngineScript
import app.quasar.qgl.engine.core.GameNodeApi
import app.quasar.qgl.engine.core.RootNode
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
class WorldTimeScript: RootNode<WorldTimeData, WorldTimeArg>(), WorldTime {

    //Nodes
    @EngineRef(NodeTypes.ENGINE_LOGGER)
    lateinit var logger: EngineLogger private set

    //Interface
    override fun getGameMillis() = requireDataForInterface.gameTime.millis
    override fun getTimeStamp() = ""

    override fun onSetup(engine: EngineApi, data: WorldTimeData?) {
        super.onSetup(engine, data)
        logger = engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onCreate(argument: WorldTimeArg?): WorldTimeData {
        return WorldTimeData(
            gameSpeed = argument?.gameSpeed ?: WorldTimeArg.DEFAULT_SPEED,
            gameTime = if(argument?.startTime != null) {
                MutableDateTime(argument.startTime)
            } else {
                MutableDateTime.now()
            }
        )
    }

    override fun onSimulate(engine: EngineApi, node: GameNodeApi, clock: EngineClock, data: WorldTimeData) {
        doWorldTime(
            this, clock.deltaTime, data
        )
    }

    private object NodeTypes {
        const val ENGINE_LOGGER = 1
    }
}

//DATA

data class WorldTimeData(
    val gameTime: MutableDateTime,
    var gameSpeed: Float = WorldTimeArg.DEFAULT_SPEED
)

//ARGUMENT

data class WorldTimeArg(
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