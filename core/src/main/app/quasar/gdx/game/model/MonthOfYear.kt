package app.quasar.gdx.game.model

enum class MonthOfYear(val monthValue: Int) {
    JANUARY(1),
    FEBUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    companion object {
        fun findFromValue(value: Int): MonthOfYear {
            return values().first { it.monthValue == value }
        }
    }
}