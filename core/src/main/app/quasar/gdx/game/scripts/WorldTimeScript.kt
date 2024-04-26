package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.ScriptTypes
import app.quasar.gdx.game.data.WorldTimeData
import app.quasar.gdx.game.logic.doWorldTime
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.serialize.EngineRef
import app.quasar.qgl.engine.serialize.EngineScript
import app.quasar.qgl.scripts.ConsoleLog
import org.joda.time.MutableDateTime

interface WorldTime {
    fun getGameMillis(): Long
    fun getTimeStamp(): String
}

@EngineScript(ScriptTypes.WORLD_TIME)
class WorldTimeScript: GameNode<WorldTimeData>(), WorldTime {

    //Nodes
    @EngineRef(NodeTypes.ENGINE_LOGGER)
    lateinit var console: ConsoleLog private set

    //Interface
    override fun getGameMillis() = dataForInterface.gameTime.millis
    override fun getTimeStamp() = ""

    override fun onSetup(context: SetupContext, data: WorldTimeData) {
        console = context.engine.requireFindByInterface(ConsoleLog::class)
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
            this,
            context,
            data
        )
    }

    private object NodeTypes {
        const val ENGINE_LOGGER = 1
    }
}

//ARGUMENT

data class WorldTimeInput(
    val gameSpeed: Float,
    val startTime: Long
) {
    companion object {
        const val DEFAULT_SPEED = 1000000f
    }
}