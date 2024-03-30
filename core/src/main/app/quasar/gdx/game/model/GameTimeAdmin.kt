package app.quasar.gdx.game.model

import org.joda.time.MutableDateTime

interface GameTime {
    val dayOfMonth: Int
    val year: Int
    val monthOfYear: Int
    val hourOfDay: Int
    val minuteOfHour: Int
}

class GameTimeAdmin(instant: Long = System.currentTimeMillis()): GameTime {
    private val date = MutableDateTime(instant)

    override val minuteOfHour       get() = date.minuteOfHour
    override val hourOfDay: Int     get() = date.hourOfDay
    override val dayOfMonth: Int    get() = date.dayOfMonth
    override val monthOfYear: Int   get() = date.monthOfYear
    override val year: Int          get() = date.year

    fun addSeconds(seconds: Float) {
        val millis = (seconds * 1000).toInt()
        date.addMillis(millis)
    }

    override fun toString(): String {
        return "$year-$monthOfYear-$dayOfMonth $hourOfDay:$minuteOfHour"
    }
}