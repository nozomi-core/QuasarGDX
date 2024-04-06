package app.quasar.gdx.game.scripts

import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.NodeApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger
import org.joda.time.MutableDateTime

interface WorldTime {
    fun getGameMillis(): Long
    fun getTimeStamp(): String
}

class WorldTimeScript: RootNode<WorldTimeData, WorldTimeArg>(), WorldTime {
    //Nodes
    private lateinit var logger: EngineLogger

    //Interface
    override fun getGameMillis() = requireDataForInterface.gameTime.millis
    override fun getTimeStamp() = requireDataForInterface.getTimeStamp()

    override fun onSetupEngine(engine: EngineApi) {
        logger = engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onCreateData(argument: WorldTimeArg?): WorldTimeData {
        super.onCreateData(argument)

        return WorldTimeData(
            gameSpeed = argument?.gameSpeed ?: DEFAULT_SPEED,
            gameTime = if(argument?.startTime != null) {
                MutableDateTime(argument.startTime)
            } else {
                MutableDateTime.now()
            }
        )
    }

    override fun onSimulate(node: NodeApi, deltaTime: Float, data: WorldTimeData?) {
        if(data == null) return

        data.gameTime.addSeconds((deltaTime * data.gameSpeed).toInt())

        data.counter += deltaTime
        if(data.counter > 5) {
            logger.message(this, "onTime: ${data.getTimeStamp()}")
            data.counter = 0.0f
        }
    }

    companion object {
        const val DEFAULT_SPEED = 1000000f
    }
}
//TODO: remove counter, only test
data class WorldTimeArg(
    val gameSpeed: Float,
    val startTime: Long
)

data class WorldTimeData(
    val gameTime: MutableDateTime,
    var gameSpeed: Float = WorldTimeScript.DEFAULT_SPEED,
    var counter: Float = 0.0f
) {
    fun getTimeStamp() = "${gameTime.year}-${gameTime.monthOfYear}-${gameTime.dayOfMonth}"
}




