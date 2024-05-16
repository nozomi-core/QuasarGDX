package app.quasar.qgl.engine.core

class QuasarEngineFactory(factory: QuasarEngineFactory.() -> Unit) {
    var drawable: DrawableApi? = null
    var camera: CameraApi? = null
    var project: ProjectionApi? = null

    init {
        factory(this)
    }

    fun requireDrawableApi() = drawable!!
    fun requireCamera() = camera!!
    fun requireProject() = project!!
}