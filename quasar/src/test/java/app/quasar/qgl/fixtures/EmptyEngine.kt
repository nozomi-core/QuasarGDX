package app.quasar.qgl.fixtures

import app.quasar.qgl.engine.core.QuasarEngineActual
import app.quasar.qgl.engine.serialize.*

class EmptyScripts: ScriptFactory {
    override val scripts: ScriptCallback = {}
}

class EmptyData: DataFactory {
    override val data: DataCallback = {}
}

object EmptyEngine {
    fun create(): QuasarEngineActual {
        return QuasarEngineActual {
            drawable = EmptyDrawableApi()
            project = EmptyProjectApi()
            camera = EmptyCameraApi()
            classes = ClassFactory(EmptyScripts(), EmptyData())
        }
    }
}