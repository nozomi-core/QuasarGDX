package app.quasar.qgl.engine.core

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

interface ShapeApi {
    fun getRender(): ShapeRenderer
}

class ShapeApiActual(val renderer: ShapeRenderer): ShapeApi {
    override fun getRender(): ShapeRenderer {
        return renderer
    }
}