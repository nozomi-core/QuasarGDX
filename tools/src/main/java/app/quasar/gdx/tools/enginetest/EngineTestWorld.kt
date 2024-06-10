package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.tools.enginetest.scripts.*
import app.quasar.qgl.engine.core.EngineApi
import app.quasar.qgl.engine.core.EngineDimension
import app.quasar.qgl.tiles.GameWorld

class EngineTestWorld: GameWorld() {

    override fun onCreate(engine: EngineApi): EngineDimension {
        engine.createNode(NextDimen, TilemapScript::class)
        engine.createNode(MainDimen, DestroyScript::class)
        engine.createNode(MainDimen, SpinnerScript::class)
        engine.createNode(MainDimen, BackgroundScript::class)
        engine.createNode(MainDimen, PlayerScript::class)
        engine.createNode(DessertDimen, DessertRoadScript::class)

        return MainDimen
    }

    companion object {
        val MainDimen = EngineDimension.create(1)
        val NextDimen = EngineDimension.create(2)
        val DessertDimen = EngineDimension.create(3)
    }
}