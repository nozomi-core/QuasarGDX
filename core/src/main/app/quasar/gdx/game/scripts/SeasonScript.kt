package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.Season
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger
import kotlin.reflect.KClass

class SeasonScript: RootNode() {
    private lateinit var logger: EngineLogger
    private lateinit var worldTime: WorldTime

    private lateinit var currentSeason: Season

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
        val month = worldTime.gameTime.monthOfYear
        //TODO: implement
        currentSeason = if(month in 1..8) {
            Season.WINTER
        } else {
            Season.SPRING
        }
    }

    private fun onSeasonChanged() {
        logger.message(this, "onSeason: ${currentSeason.name}")
    }

    override fun shouldRunBefore(): List<KClass<*>> {
        return listOf(WorldTimeScript::class)
    }
}