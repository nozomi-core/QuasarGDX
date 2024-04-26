package app.quasar.qgl

import app.quasar.qgl.scripts.ConsoleLoggerScript
import app.quasar.qgl.scripts.InputStackScript
import kotlin.reflect.KClass

object QuasarCoreScripts {
    val scripts = listOf<KClass<*>>(
        ConsoleLoggerScript::class,
        InputStackScript::class
    )
}