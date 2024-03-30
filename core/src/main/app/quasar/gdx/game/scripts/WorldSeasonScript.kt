package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.MonthOfYear
import app.quasar.gdx.game.model.Season
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.language.ProviderStack
import app.quasar.qgl.scripts.EngineLogger
import kotlin.reflect.KClass

interface WorldSeason {
    val seasonProvider: ProviderStack<SeasonAlgorithm>
}

class WorldSeasonScript: RootNode(), WorldSeason {
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime

    private lateinit var currentSeason: Season

    private val defaultSeason = object : SeasonAlgorithm {
        override fun onWhatSeasonNow(monthOfYear: MonthOfYear): Season {
            return when(monthOfYear) {
                MonthOfYear.DECEMBER, MonthOfYear.JANUARY, MonthOfYear.FEBUARY -> Season.SUMMER
                MonthOfYear.MARCH, MonthOfYear.APRIL, MonthOfYear.MAY -> Season.AUTUMN
                MonthOfYear.JUNE, MonthOfYear.JULY, MonthOfYear.AUGUST -> Season.WINTER
                MonthOfYear.SEPTEMBER, MonthOfYear.OCTOBER, MonthOfYear.NOVEMBER -> Season.SPRING
            }
        }
    }

    override val seasonProvider = ProviderStack<SeasonAlgorithm>(defaultSeason)

    override fun onCreate(engine: EngineApi, argument: Any?) {
        super.onCreate(engine, argument)
        worldTime = engine.requireFindByInterface(WorldTime::class)
        logger = engine.requireFindByInterface(EngineLogger::class)
        doCalculateSeason()
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        if(worldTime.hasMonthOfYearChanged) {
            val lastSeason = currentSeason
            doCalculateSeason()
            if(lastSeason != currentSeason) {
                onSeasonChanged()
            }
        }
    }

    private fun doCalculateSeason() {
        val seasonAlgorithm = seasonProvider.get()
        currentSeason = seasonAlgorithm.onWhatSeasonNow(worldTime.whatMonth)
    }

    private fun onSeasonChanged() {
        logger.message(this, "onSeason: ${currentSeason.name}")
    }

    override fun shouldRunBefore(): List<KClass<*>> {
        return listOf(WorldTimeScript::class)
    }
}

interface SeasonAlgorithm {
    fun onWhatSeasonNow(monthOfYear: MonthOfYear): Season
}