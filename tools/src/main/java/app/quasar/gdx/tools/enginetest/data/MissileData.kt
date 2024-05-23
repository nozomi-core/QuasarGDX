package app.quasar.gdx.tools.enginetest.data

import app.quasar.qgl.engine.core.GameData
import app.quasar.qgl.serialize.BinProp
import com.badlogic.gdx.math.Vector3

class MissileData: GameData {
    @BinProp(0)            var position: Vector3 = Vector3()
}