package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class EditWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf(
            Player::class,
            EditMap::class,
            ConsolePrinterScript::class,
            AudioScript::class
        )
    }

     override fun onCreate(engine: EngineApi) {

    }
}