package app.quasar.qgl.render

import app.quasar.qgl.tiles.GameWindow
import app.quasar.qgl.engine.core.CameraApi

class CameraApiActual(private val window: GameWindow): CameraApi {

    override fun setCamera(x: Float, y: Float) {
        val worldCam = window.getWorldCamera()
        worldCam.position.x = x
        worldCam.position.y = y
        worldCam.update()
    }
}