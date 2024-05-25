package app.quasar.gdx.tools.enginetest.data

import app.quasar.qgl.serialize.BinProp
import app.quasar.qgl.serialize.QGLEntity
import com.badlogic.gdx.math.Vector3

@QGLEntity("missile_data")
class MissileData {
          var position: Vector3 = Vector3()
}