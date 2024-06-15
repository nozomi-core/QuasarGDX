package app.quasar.qgl.engine

import app.quasar.qgl.engine.core.QuasarEngine

typealias WorldListener = (QuasarEngine) -> Unit

class CommonRuntime {
    private val listeners = mutableListOf<WorldListener>()

    fun notifyWorld(engine: QuasarEngine) {
        listeners.forEach { it(engine) }
    }

    fun addWorldListener(callback: WorldListener) {
        listeners.add(callback)
    }
}
