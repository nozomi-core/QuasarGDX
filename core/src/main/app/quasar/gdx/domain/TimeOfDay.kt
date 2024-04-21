package app.quasar.gdx.domain

enum class TimeOfDay(val predicate: (hour: Int) -> Boolean
) {
    NIGHT({ hour ->
        hour in 0..4
    }),
    MORNING({ hour ->
        hour in 5..11
    }),
    AFTERNOON({ hour ->
        hour in 12 .. 18
    }),
    EVENING({ hour ->
        hour in 19..24
    });

    companion object {
        fun findTimeOfDay(hour: Int): TimeOfDay {
            val listTimes = values()

            return listTimes.first {timeOfDay ->
                timeOfDay.predicate(hour)
            }
        }
    }
}