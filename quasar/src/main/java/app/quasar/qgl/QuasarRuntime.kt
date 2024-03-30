package app.quasar.qgl

import app.quasar.qgl.engine.EngineApi

typealias EngineReady = (api: EngineApi) -> Unit

class QuasarRuntime {
    private var callback: EngineReady? = null

    fun postWorldEngine(api: EngineApi) {
        callback?.invoke(api)
    }

    fun onWorldEngine(callback: EngineReady) {
        this.callback = callback
    }
}