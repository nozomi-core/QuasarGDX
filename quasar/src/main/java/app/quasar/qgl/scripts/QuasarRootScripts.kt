package app.quasar.qgl.scripts

import kotlin.reflect.KClass

object QuasarRootScripts {
    val scripts = listOf<KClass<*>>(
        ConsoleLoggerScript::class,
        InpuStackScript::class
    )
}