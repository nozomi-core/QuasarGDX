package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.MonthOfYear
import app.quasar.gdx.game.model.Season
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.language.Providable
import app.quasar.qgl.language.ProviderStack
import app.quasar.qgl.scripts.EngineLogger
import kotlin.reflect.KClass

interface WorldSeason {
    val seasonProvider: ProviderStack<SeasonAlgorithm>
    val hasSeasonChanged: Boolean
}

class WorldSeasonScript: RootNode(), WorldSeason {
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime
    private lateinit var currentSeason: Season
    private lateinit var defaultSeason: SeasonAlgorithm

    private var _hasSeasonChanged = false
    override val hasSeasonChanged: Boolean get() = _hasSeasonChanged

    override val seasonProvider = ProviderStack(defaultSeason)

    override fun onCreate(engine: EngineApi, argument: Any?) {
        super.onCreate(engine, argument)
        defaultSeason = object: SeasonAlgorithm(engine) {
            override fun onWhatSeasonNow(monthOfYear: MonthOfYear): Season {
                return when(monthOfYear) {
                    MonthOfYear.DECEMBER, MonthOfYear.JANUARY, MonthOfYear.FEBUARY -> Season.SUMMER
                    MonthOfYear.MARCH, MonthOfYear.APRIL, MonthOfYear.MAY -> Season.AUTUMN
                    MonthOfYear.JUNE, MonthOfYear.JULY, MonthOfYear.AUGUST -> Season.WINTER
                    MonthOfYear.SEPTEMBER, MonthOfYear.OCTOBER, MonthOfYear.NOVEMBER -> Season.SPRING
                }
            }
        }

        worldTime = engine.requireFindByInterface(WorldTime::class)
        logger = engine.requireFindByInterface(EngineLogger::class)
        doCalculateSeason()
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        _hasSeasonChanged = false
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
        _hasSeasonChanged = true
    }

    override fun shouldRunBefore(): List<KClass<*>> {
        return listOf(WorldTimeScript::class)
    }
}

abstract class SeasonAlgorithm(engine: EngineApi): Providable(engine) {
    abstract fun onWhatSeasonNow(monthOfYear: MonthOfYear): Season
}