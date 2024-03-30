package app.quasar.gdx.ui.main

import app.quasar.gdx.game.scripts.DayClockScript
import app.quasar.gdx.game.scripts.SeasonScript
import app.quasar.gdx.game.scripts.WorldTimeScript
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class MainWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf(
            WorldTimeScript::class,
            DayClockScript::class,
            SeasonScript::class
        )
    }

    override fun onCreate(engine: EngineApi) {
        super.onCreate(engine)
        engine.createGameNode(MainMap::class)
    }
}