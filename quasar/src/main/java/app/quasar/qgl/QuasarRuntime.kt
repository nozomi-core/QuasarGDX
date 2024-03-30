package app.quasar.qgl

import app.quasar.qgl.engine.EngineApi

typealias EngineReady = (api: EngineApi) -> Unit

class QuasarRuntime {
    private var callback: EngineReady? = null

    internal fun sendWorldEngine(api: EngineApi) {
        callback?.invoke(api)
        callback = null
    }

    fun onWorldEngine(callback: EngineReady) {
        this.callback = callback
    }
}