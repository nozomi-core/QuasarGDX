package app.quasar.gdx.tools.console

import app.quasar.qgl.engine.CommonRuntime
import app.quasar.qgl.engine.core.QuasarEngine
import app.quasar.qgl.engine.core.ReadableGameNode
import javax.swing.*

class QuasarToolConsole(
    runtime: CommonRuntime
): JFrame() {
    private lateinit var engine: QuasarEngine

    private var selected: ReadableGameNode? = null

    init {
        runtime.addWorldListener { engine = it }
        setSize(800, 1200)
        setLocation(100,100)
        setLocationRelativeTo(null)

        defaultCloseOperation = EXIT_ON_CLOSE
        setup()

        isVisible = true
    }

    private fun setup() {
        displayEngineToolbar(this) { option ->
            when(option) {
                MenuOption.SAVE -> SaveWindow(engine)
                MenuOption.LOAD -> LoadWindow { filename ->
                   //EngineDeserialize(filename)
                }
                MenuOption.SHUTDOWN -> engine.shutdown()
                MenuOption.PAUSE -> engine.pause()
                MenuOption.RESUME -> engine.resume()
            }
        }
    }
}

