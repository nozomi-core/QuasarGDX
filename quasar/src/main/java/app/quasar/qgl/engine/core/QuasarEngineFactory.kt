package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.serialize.ScriptFactory

class QuasarEngineFactory(factory: QuasarEngineFactory.() -> Unit) {
    var drawable: DrawableApi? = null
    var camera: CameraApi? = null
    var project: ProjectionApi? = null

    var scripts: ScriptFactory? = null

    var accounting: EngineAccounting? = null
    var nodeGraph: NodeGraph? = null

    init {
        factory(this)
    }

    fun requireDrawableApi() = drawable!!
    fun requireCamera() = camera!!
    fun requireProject() = project!!
}