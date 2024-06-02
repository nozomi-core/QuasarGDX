package app.quasar.qgl.engine.core.interfaces

import app.quasar.qgl.engine.core.ShapeApi

interface ShapeOverlay {
    fun onShape(shape: ShapeApi)
}