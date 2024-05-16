package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.tools.enginetest.scripts.DestroyScript
import app.quasar.gdx.tools.enginetest.scripts.PlayerScript
import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.tiles.GameWorld

class EngineTestWorld: GameWorld() {

    override fun onCreate(engine: EngineApi) {
        engine.createNode(PlayerScript::class)
        engine.createNode(DestroyScript::class)
    }
}