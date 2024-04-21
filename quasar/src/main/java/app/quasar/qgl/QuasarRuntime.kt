package app.quasar.qgl

import app.quasar.qgl.engine.QuasarEngine

typealias EngineReady = (api: QuasarEngine) -> Unit

class QuasarRuntime {
    private var callback: EngineReady? = null

    internal fun sendWorldEngine(api: QuasarEngine) {
        callback?.invoke(api)
        callback = null
    }

    fun onWorldEngine(callback: EngineReady) {
        this.callback = callback
    }
}