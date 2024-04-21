package app.quasar.gdx

import app.quasar.gdx.game.scripts.WorldTimeScript
import app.quasar.gdx.ui.main.MainMap
import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class CoreWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf(
            WorldTimeScript::class
        )
    }

    override fun onCreate(engine: EngineApi) {
        super.onCreate(engine)
        engine.createGameNode(MainMap::class)
    }
}