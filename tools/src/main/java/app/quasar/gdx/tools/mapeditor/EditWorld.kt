package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.tiles.GameWorld
import kotlin.reflect.KClass

class EditWorld: GameWorld() {

    override fun useRootScripts(): List<KClass<*>> {
        return listOf(
            EditMap::class,
            Player::class
        )
    }

     override fun onCreate(engine: EngineApi) {

    }
}