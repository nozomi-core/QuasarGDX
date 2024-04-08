package app.quasar.gdx.game.scripts

//TODO: redo clock strict with new API

/*
interface GetDayClock {
    val dayClock: DayClock
}

interface DayClock {
    val timeOfDay: TimeOfDay
}

class DayClockScript: RootNode<DayClockData>(), GetDayClock {
    //Nodes
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime

    private lateinit var _dayClock: DayClock
    override val dayClock get() = _dayClock

    override fun onCreate(argument: Any?): DayClockData {
        return DayClockData(TimeOfDay.findTimeOfDay(worldTime.getGameTime().hourOfDay))
    }

    override fun onSetupData(data: DayClockData) {
        _dayClock = object: DayClock{
            override val timeOfDay: TimeOfDay = data.timeOfDay
        }
    }

    override fun onSetupEngine(engine: EngineApi) {
        worldTime = engine.requireFindByInterface(WorldTime::class)
        logger = engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onSimulate(deltaTime: Float) {
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
*/