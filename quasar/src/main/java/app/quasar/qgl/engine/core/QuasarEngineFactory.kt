package app.quasar.qgl.engine.core

class QuasarEngineFactory(factory: QuasarEngineFactory.() -> Unit) {
    var drawableApi: DrawableApi? = null

    init {
        factory(this)
    }

    fun requireDrawableApi(): DrawableApi {
        return drawableApi!!
    }
}