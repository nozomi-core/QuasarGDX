package app.quasar.qgl.render

import app.quasar.qgl.tiles.UiHooks
import app.quasar.qgl.engine.core1.CameraApi1

class CameraApiActual(private val hooks: UiHooks): CameraApi1 {

    override fun setCamera(x: Float, y: Float) {
        val worldCam = hooks.useWorldCamera()
        worldCam.position.x = x
        worldCam.position.y = y
    }
}