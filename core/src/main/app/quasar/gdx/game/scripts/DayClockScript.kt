package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.TimeOfDay
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger

interface DayClock {
    val timeOfDay: TimeOfDay
}

class DayClockScript: RootNode(), DayClock {
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime

    private lateinit var _timeOfDay: TimeOfDay
    override val timeOfDay: TimeOfDay get() = _timeOfDay

    override fun onCreate(engine: EngineApi, argument: Any?) {
        super.onCreate(engine, argument)
        worldTime = engine.requireFindByInterface(WorldTime::class)
        logger = engine.requireFindByInterface(EngineLogger::class)
        _timeOfDay = TimeOfDay.findTimeOfDay(worldTime.gameTime.hourOfDay)
        onTimeOfDayChanged()
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        val findTimeOfDay = TimeOfDay.findTimeOfDay(worldTime.gameTime.hourOfDay)
        if(_timeOfDay != findTimeOfDay) {
            _timeOfDay = findTimeOfDay
            onTimeOfDayChanged()
        }
    }

    private fun onTimeOfDayChanged() {
        logger.message(this, "time_changed: ${_timeOfDay.name}")
    }

    override fun shouldRunBefore() = listOf(WorldTimeScript::class)
}