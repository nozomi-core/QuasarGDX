package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.tools.enginetest.mapper.DestroyMapper
import app.quasar.gdx.tools.enginetest.mapper.MissileMapper
import app.quasar.gdx.tools.enginetest.mapper.PlayerMapper
import app.quasar.gdx.tools.enginetest.mapper.TilemapMapper
import app.quasar.gdx.tools.enginetest.scripts.*
import app.quasar.qgl.engine.serialize.ScriptCallback
import app.quasar.qgl.engine.serialize.ScriptFactory

object TestScripts: ScriptFactory {
    override val callback: ScriptCallback = {
        add(1,          PlayerScript::class,                PlayerMapper)
        add(2,          MissileScript::class,               MissileMapper)
        add(3,          DestroyScript::class,               DestroyMapper)
        add(4,          TilemapScript::class,               TilemapMapper)
        add(5,          BigPlayer::class,                   PlayerMapper)
    }
}