package app.quasar.gdx.game.model

import org.joda.time.MutableDateTime

interface GameTime {
    val dayOfMonth: Int
    val year: Int
    val monthOfYear: Int
    val hourOfDay: Int
    val minuteOfHour: Int
}

class GameTimeAdmin(instant: Long): GameTime {
    val gameDate = MutableDateTime(instant)

    override val minuteOfHour       get() = gameDate.minuteOfHour
    override val hourOfDay: Int     get() = gameDate.hourOfDay
    override val dayOfMonth: Int    get() = gameDate.dayOfMonth
    override val monthOfYear: Int   get() = gameDate.monthOfYear
    override val year: Int          get() = gameDate.year

    fun addSeconds(seconds: Float) {
        val millis = (seconds * 1000).toInt()
        gameDate.addMillis(millis)
    }

    override fun toString(): String {
        return "$year-$monthOfYear-$dayOfMonth $hourOfDay:$minuteOfHour"
    }
}