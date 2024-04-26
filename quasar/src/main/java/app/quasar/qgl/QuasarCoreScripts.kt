package app.quasar.qgl

import app.quasar.qgl.scripts.ConsoleLogScript
import app.quasar.qgl.scripts.InputStackScript
import kotlin.reflect.KClass

object QuasarCoreScripts {
    val scripts = listOf<KClass<*>>(
        ConsoleLogScript::class,
        InputStackScript::class
    )
}