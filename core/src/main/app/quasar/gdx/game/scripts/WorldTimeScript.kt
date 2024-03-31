package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.GameTime
import app.quasar.gdx.game.model.GameTimeAdmin
import app.quasar.gdx.game.model.MonthOfYear
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger

interface WorldTime {
    fun getGameTime(): GameTime
    fun getMonthOfYear(): MonthOfYear
    fun hasMinuteOfHourChanged(): Boolean
    fun hasHourOfDayChanged(): Boolean
    fun hasDayOfMonthChange(): Boolean
    fun hasMonthOfYearChanged(): Boolean
    fun hasYearChange(): Boolean
}

class WorldTimeScript: RootNode(), WorldTime {
    //Nodes
    private lateinit var logger: EngineLogger

    //Data
    private val time = GameTimeAdmin(System.currentTimeMillis())
    private val data = WorldTimeData()

    //Delegates
    override fun getGameTime() = time
    override fun hasMinuteOfHourChanged() = data.minute
    override fun hasHourOfDayChanged() = data.hour
    override fun hasDayOfMonthChange() = data.day
    override fun hasMonthOfYearChanged() = data.month
    override fun hasYearChange() = data.year
    override fun getMonthOfYear() = MonthOfYear.findFromValue(getGameTime().monthOfYear)

    override fun onCreate(argument: Any?) {
        super.onCreate(argument)
        data.gameSpeed = when(argument) {
            is Double -> argument.toFloat()
            is Float -> argument
            else -> DEFAULT_SPEED
        }
    }

    override fun onSetup(engine: EngineApi) {
        super.onSetup(engine)
        logger = engine.requireFindByInterface(EngineLogger::class)
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        data.clear()
        val gameTime = getGameTime()


        val lastMinuteOfHour = gameTime.minuteOfHour
        val lastHourOfDay = gameTime.hourOfDay
        val lastDayOfMonth = gameTime.dayOfMonth
        val lastMonthOfYear = gameTime.monthOfYear
        val lastYear = gameTime.year

        time.addSeconds(deltaTime * data.gameSpeed)

        if(lastMinuteOfHour != gameTime.minuteOfHour) {
            onMinuteOfHourChanged()
        }

        if(lastHourOfDay != gameTime.hourOfDay) {
            onHourOfDayChanged()
        }

        if(lastDayOfMonth != gameTime.dayOfMonth) {
            onDayOfMonthChanged()
        }

        if(lastMonthOfYear != gameTime.monthOfYear) {
            onMonthOfTheYearChanged()
        }

        if(lastYear != gameTime.year) {
            onYearChanged()
        }
    }

    private fun onMinuteOfHourChanged() {
        data.minute = true
    }

    private fun onHourOfDayChanged() {
        data.hour = true
    }

    private fun onMonthOfTheYearChanged() {
        data.month = true
        logger.message(this, time.toString())
    }

    private fun onDayOfMonthChanged() {
        data.day = true
    }

    private fun onYearChanged() {
        data.year = true
    }

    companion object {
        const val DEFAULT_SPEED = 1000000f
    }
}

data class WorldTimeData(
    var gameSpeed: Float = WorldTimeScript.DEFAULT_SPEED,
    var minute: Boolean = false,
    var hour: Boolean = false,
    var day: Boolean = false,
    var month: Boolean = false,
    var year: Boolean = false
) {
    fun clear() {
        minute = false
        hour = false
        day = false
        month = false
        year = false
    }
}


