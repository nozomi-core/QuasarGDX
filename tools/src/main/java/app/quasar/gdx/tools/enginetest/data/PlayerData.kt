package app.quasar.gdx.tools.enginetest.data

import app.quasar.qgl.serialize.BinProp
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.math.Vector3

@QGLEntity("player_data")
class PlayerData {

    @BinProp(1)         var rotation: Float = 0f
    @BinProp(2)         var isRotating: Boolean = false
    @BinProp(3)         var rotateSpeed: Float = 0f
   // @BinProp(4)         var x = 0f
    //@BinProp(5)         var y = 0f
    //@BinProp(6)         var z = 0f

    @BinProp(7) var position = Vector3()
}