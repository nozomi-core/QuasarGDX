package app.quasar.gdx

import app.quasar.gdx.game.scripts.WorldTimeScript
import app.quasar.gdx.ui.main.MainMap
import app.quasar.qgl.engine.core1.EngineApi1
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class CoreWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf(
            WorldTimeScript::class
        )
    }

    override fun onCreate(engine: EngineApi1) {
        super.onCreate(engine)
        engine.createNode(MainMap::class)
    }
}