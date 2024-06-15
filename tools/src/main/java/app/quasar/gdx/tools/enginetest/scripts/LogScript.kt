package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.data.LogData
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.serialize.QGLEntity

@QGLEntity("log_script")
class LogScript: GameNode<LogData>() {

    override fun onCreate(argument: NodeArgument): LogData {
        return LogData()
    }
}