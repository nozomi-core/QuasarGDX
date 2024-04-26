package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.tools.scripts.ConsolePrinterScript
import app.quasar.gdx.tools.scripts.PlayerScript
import app.quasar.gdx.tools.scripts.ProximityScript
import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.scripts.WorldBoundScript
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class EditWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf(
            PlayerScript::class,
            EditMap::class,
            ConsolePrinterScript::class,
            WorldBoundScript::class,
            ProximityScript::class
        )
    }

     override fun onCreate(engine: EngineApi) {

    }
}