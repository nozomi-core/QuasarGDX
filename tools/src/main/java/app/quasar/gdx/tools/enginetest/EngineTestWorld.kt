package app.quasar.gdx.tools.enginetest

import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.tiles.GameWorld

class EngineTestWorld: GameWorld() {

    override fun onCreate(engine: EngineApi) {
        engine.createNode(TestNode::class)
    }
}