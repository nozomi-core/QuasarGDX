package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.tiles.GameWorld

class EditWorld: GameWorld {

    override fun onCreateRoot(engine: EngineApi) {
        engine.createGameNode(EditMap::class)
    }

    override fun onCreate(engine: EngineApi) {
        engine.createGameNode(MapTool::class)
    }
}