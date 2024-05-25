package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.tools.enginetest.data.*
import app.quasar.gdx.tools.enginetest.scripts.*
import app.quasar.qgl.engine.serialize.DataCallback
import app.quasar.qgl.engine.serialize.DataFactory
import app.quasar.qgl.engine.serialize.ScriptCallback
import app.quasar.qgl.engine.serialize.ScriptFactory

object TestScripts: ScriptFactory {
    override val scripts: ScriptCallback = {
        add(PlayerScript::class)
        add(MissileScript::class)
        add(DestroyScript::class)
        add(TilemapScript::class)
        add(BigPlayer::class)
        add(SpinnerScript::class)
        add(LogScript::class)
    }
}

object DataScripts: DataFactory {
    override val data: DataCallback = {
        add(PlayerData::class)
        add(MissileData::class)
        add(DestroyData::class)
        add(TilemapData::class)
        add(SpinnerData::class)
        add(UnitData::class)
    }
}