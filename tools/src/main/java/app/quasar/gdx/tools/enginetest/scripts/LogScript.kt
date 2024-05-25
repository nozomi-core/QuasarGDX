package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.data.UnitData
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.serialize.QGLEntity

@QGLEntity("log_script")
class LogScript: GameNode<UnitData>() {

    override fun onCreate(argument: NodeArgument): UnitData {
        return UnitData()
    }
}