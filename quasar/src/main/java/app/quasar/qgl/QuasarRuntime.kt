package app.quasar.qgl

import app.quasar.qgl.engine.core.QuasarEngine

typealias EngineReady = (api: QuasarEngine) -> Unit

open class QuasarRuntime {
    private var callback: EngineReady? = null

    internal fun sendWorldEngine(api: QuasarEngine) {
        callback?.invoke(api)
        callback = null
    }

    fun onWorldEngine(callback: EngineReady) {
        this.callback = callback
    }
}