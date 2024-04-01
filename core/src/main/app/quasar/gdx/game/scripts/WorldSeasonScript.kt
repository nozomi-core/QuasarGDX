package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.MonthOfYear
import app.quasar.gdx.game.model.Season
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.language.GameData
import app.quasar.qgl.language.ProviderStack
import app.quasar.qgl.scripts.EngineLogger
import kotlin.reflect.KClass

/*
interface WorldSeason {
    val hasSeasonChanged: Boolean
    fun getSeasonProvider(): ProviderStack<SeasonAlgorithm>
}

class WorldSeasonScript: RootNode<String>(), WorldSeason {
    //Node refs
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime

    //Data
    private lateinit var data: WorldSeasonData

    //Delegate
    override val hasSeasonChanged: Boolean get() = data.hasSeasonChanged

    //Providers
    private lateinit var seasonProvider: ProviderStack<SeasonAlgorithm>
    override fun getSeasonProvider() = seasonProvider

    override fun onSetup(engine: EngineApi) {
        super.onSetup(engine)
        worldTime = engine.requireFindByInterface(WorldTime::class)
        logger = engine.requireFindByInterface(EngineLogger::class)

        val defaultSeason = DefaultSeasonAlgorithm(engine)
        seasonProvider = ProviderStack(defaultSeason)
        data = WorldSeasonData(defaultSeason.onWhatSeasonNow(worldTime.getMonthOfYear()), false)

    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        data.hasSeasonChanged = false
        if(worldTime.hasMonthOfYearChanged()) {
            val lastSeason = data.currentSeason
            doCalculateSeason()
            if(lastSeason != data.currentSeason) {
                onSeasonChanged()
            }
        }
    }

    private fun doCalculateSeason() {
        val seasonAlgorithm = seasonProvider.get()
        data.currentSeason = seasonAlgorithm.onWhatSeasonNow(worldTime.getMonthOfYear())
    }

    private fun onSeasonChanged() {
        data.hasSeasonChanged = true
    }

    override fun shouldRunBefore(): List<KClass<*>> {
        return listOf(WorldTimeScript::class)
    }
}

data class WorldSeasonData(
    var currentSeason: Season,
    var hasSeasonChanged: Boolean
)

abstract class SeasonAlgorithm(engine: EngineApi): GameData(engine) {
    abstract fun onWhatSeasonNow(monthOfYear: MonthOfYear): Season
}

class DefaultSeasonAlgorithm(engine: EngineApi): SeasonAlgorithm(engine) {
    override fun onWhatSeasonNow(monthOfYear: MonthOfYear): Season {
        return when(monthOfYear) {
            MonthOfYear.DECEMBER, MonthOfYear.JANUARY, MonthOfYear.FEBUARY -> Season.SUMMER
            MonthOfYear.MARCH, MonthOfYear.APRIL, MonthOfYear.MAY -> Season.AUTUMN
            MonthOfYear.JUNE, MonthOfYear.JULY, MonthOfYear.AUGUST -> Season.WINTER
            MonthOfYear.SEPTEMBER, MonthOfYear.OCTOBER, MonthOfYear.NOVEMBER -> Season.SPRING
        }
    }
}*/