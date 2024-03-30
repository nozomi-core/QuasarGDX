package app.quasar.qgl.scripts

import kotlin.reflect.KClass

object QuasarRootScripts {
    val scripts = listOf<KClass<*>>(
        QuasarEngineLogger::class
    )
}