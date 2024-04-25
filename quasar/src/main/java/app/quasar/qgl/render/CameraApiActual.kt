package app.quasar.qgl.render

import app.quasar.qgl.engine.UiHooks
import app.quasar.qgl.engine.core.CameraApi

class CameraApiActual(private val hooks: UiHooks): CameraApi {

    override fun setCamera(x: Float, y: Float) {
        val worldCam = hooks.useWorldCamera()
        worldCam.position.x = x
        worldCam.position.y = y
    }
}