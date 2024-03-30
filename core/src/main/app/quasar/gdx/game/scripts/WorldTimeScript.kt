package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.GameTime
import app.quasar.gdx.game.model.GameTimeAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger

interface WorldTime {
    val gameTime: GameTime
    val hasMinuteOfHourChanged: Boolean
    val hasHourOfDayChanged: Boolean
    val hasMonthOfYearChanged: Boolean
    val hasYearChange: Boolean
}

class WorldTimeScript: RootNode(), WorldTime {
    private val _gameTime = GameTimeAdmin()

    override val gameTime: GameTime get() = _gameTime

    private lateinit var logger: EngineLogger

    private var gameSpeed: Float = DEFAULT_SPEED
    private val timeChanges = TimeChanges()

    override val hasMinuteOfHourChanged: Boolean    get() = timeChanges.minute
    override val hasHourOfDayChanged: Boolean       get() = timeChanges.hour
    override val hasMonthOfYearChanged: Boolean     get() = timeChanges.month
    override val hasYearChange: Boolean             get() = timeChanges.year

    override fun onCreate(engine: EngineApi, argument: Any?) {
        super.onCreate(engine, argument)
        gameSpeed = when(argument) {
            is Double -> argument.toFloat()
            is Float -> argument
            else -> DEFAULT_SPEED
        }

        logger = engineApi.requireFindByInterface(EngineLogger::class)
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        timeChanges.clear()

        val lastMinuteOfHour = gameTime.minuteOfHour
        val lastHourOfDay = gameTime.hourOfDay
        val lastDayOfMonth = gameTime.dayOfMonth
        val lastYear = gameTime.year

        _gameTime.addSeconds(deltaTime * gameSpeed)

        if(lastMinuteOfHour != gameTime.minuteOfHour) {
            onMinuteOfHourChanged()
        }

        if(lastHourOfDay != gameTime.hourOfDay) {
            onHourOfDayChanged()
        }

        if(lastDayOfMonth != gameTime.dayOfMonth) {
            onDayOfMonthChanged()
        }

        if(lastYear != gameTime.year) {
            onYearChanged()
        }
    }

    private fun onMinuteOfHourChanged() {
        timeChanges.minute = true
    }

    private fun onHourOfDayChanged() {
        timeChanges.hour = true
        logger.message(this, _gameTime.toString())
    }

    private fun onDayOfMonthChanged() {
        timeChanges.day = true
    }

    private fun onYearChanged() {
        timeChanges.year = true
    }

    companion object {
        const val DEFAULT_SPEED = 5000f
    }
}

data class TimeChanges(
    var minute: Boolean = false,
    var hour: Boolean = false,
    var day: Boolean = false,
    var month: Boolean = false,
    var year: Boolean = false) {

    fun clear() {
        minute = false
        hour = false
        day = false
        month = false
        year = false
    }
}


