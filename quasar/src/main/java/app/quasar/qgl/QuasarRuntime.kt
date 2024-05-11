package app.quasar.qgl

import app.quasar.qgl.engine.core.QuasarEngine1

typealias EngineReady = (api: QuasarEngine1) -> Unit

open class QuasarRuntime {
    private var callback: EngineReady? = null

    internal fun sendWorldEngine(api: QuasarEngine1) {
        callback?.invoke(api)
        callback = null
    }

    fun onWorldEngine(callback: EngineReady) {
        this.callback = callback
    }
}