package app.quasar.gdx.tools.enginetest

import app.quasar.gdx.tools.enginetest.scripts.DestroyScript
import app.quasar.gdx.tools.enginetest.scripts.MissileScript
import app.quasar.gdx.tools.enginetest.scripts.PlayerScript
import kotlin.reflect.KClass

enum class TestScripts(val id: Int, val kClass: KClass<*>) {
    Destroy(1, DestroyScript::class),
    Missile(2, MissileScript::class),
    Player(3, PlayerScript::class)
}