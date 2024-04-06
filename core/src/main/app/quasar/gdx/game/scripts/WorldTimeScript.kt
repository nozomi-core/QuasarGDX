package app.quasar.gdx.game.scripts

import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.engine.EngineRef
import app.quasar.qgl.engine.EngineScript
import app.quasar.qgl.entity.NodeApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger
import org.joda.time.MutableDateTime

interface WorldTime {
    fun getGameMillis(): Long
    fun getTimeStamp(): String
}

@EngineScript(4)
class WorldTimeScript: RootNode<WorldTimeData, WorldTimeArg>(), WorldTime {
    //Nodes
    @EngineRef(BinTypes.ID_LOGGER)
    private lateinit var logger: EngineLogger

    //Interface
    override fun getGameMillis() = requireDataForInterface.gameTime.millis
    override fun getTimeStamp() = requireDataForInterface.getTimeStamp()

    override fun onSetup(engine: EngineApi, data: WorldTimeData?) {
        super.onSetup(engine, data)
        logger = engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onCreate(argument: WorldTimeArg?): WorldTimeData {
        return WorldTimeData(
            gameSpeed = argument?.gameSpeed ?: DEFAULT_SPEED,
            gameTime = if(argument?.startTime != null) {
                MutableDateTime(argument.startTime)
            } else {
                MutableDateTime.now()
            }
        )
    }

    override fun onSimulate(node: NodeApi, deltaTime: Float, data: WorldTimeData) {
        data.gameTime.addSeconds((deltaTime * data.gameSpeed).toInt())
    }

    companion object {
        const val DEFAULT_SPEED = 1000000f
    }

    private object BinTypes {
        const val ID_LOGGER = 1
    }
}

data class WorldTimeArg(
    val gameSpeed: Float,
    val startTime: Long
)

data class WorldTimeData(
    val gameTime: MutableDateTime,
    var gameSpeed: Float = WorldTimeScript.DEFAULT_SPEED
) {
    fun getTimeStamp() = "${gameTime.year}-${gameTime.monthOfYear}-${gameTime.dayOfMonth}"
}




