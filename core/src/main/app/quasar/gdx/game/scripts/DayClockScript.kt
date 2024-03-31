package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.TimeOfDay
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger

interface DayClock {
    val timeOfDay: TimeOfDay
}

class DayClockScript: RootNode(), DayClock {
    //Nodes
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime

    //Data
    private lateinit var data: DayClockData

    //Delegates
    override val timeOfDay: TimeOfDay get() = data.timeOfDay

    override fun onCreate(argument: Any?) {
        super.onCreate(argument)
        data = DayClockData(TimeOfDay.findTimeOfDay(worldTime.getGameTime().hourOfDay))
        onTimeOfDayChanged()
    }

    override fun onSetup(engine: EngineApi) {
        super.onSetup(engine)
        worldTime = engine.requireFindByInterface(WorldTime::class)
        logger = engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        val findTimeOfDay = TimeOfDay.findTimeOfDay(worldTime.getGameTime().hourOfDay)
        if(data.timeOfDay != findTimeOfDay) {
            data.timeOfDay = findTimeOfDay
            onTimeOfDayChanged()
        }
    }

    private fun onTimeOfDayChanged() {
        //logger.message(this, "time_changed: ${_timeOfDay.name}")
    }

    override fun shouldRunBefore() = listOf(WorldTimeScript::class)
}

data class DayClockData(var timeOfDay: TimeOfDay)