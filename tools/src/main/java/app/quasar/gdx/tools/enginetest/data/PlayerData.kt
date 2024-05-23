package app.quasar.gdx.tools.enginetest.data

import app.quasar.qgl.engine.core.GameData
import app.quasar.qgl.serialize.BinProp
import com.badlogic.gdx.math.Vector3

class PlayerData: GameData {
    @BinProp(0)         var position: Vector3 = Vector3()
    @BinProp(1)         var rotation: Float = 0f
    @BinProp(2)         var isRotating: Boolean = false
    @BinProp(3)         var rotateSpeed: Float = 0f
}