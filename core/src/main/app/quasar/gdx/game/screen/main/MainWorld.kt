package app.quasar.gdx.game.screen.main

import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class MainWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf()
    }

    override fun onCreate(engine: EngineApi) {
        super.onCreate(engine)
        engine.createGameNode(MainMap::class)
    }
}