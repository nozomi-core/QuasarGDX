package app.quasar.gdx.tools.enginetest.data

import app.quasar.qgl.serialize.BinProp
import app.quasar.qgl.serialize.QGLEntity

@QGLEntity("destroy_data")
class DestroyData {
    @BinProp(0)         var totalTime: Float = 0f
}