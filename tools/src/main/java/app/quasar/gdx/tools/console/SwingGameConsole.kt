package app.quasar.gdx.tools.console

import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.scripts.EngineLogger
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea


class SwingGameConsole(private val engineApi: EngineApi) {
    private val console: EngineLogger = engineApi.requireFindByInterface(EngineLogger::class)

    init {
        setupEditor()
    }
    private fun setupEditor() {

        //Swing window
        val frame = JFrame("GameLogger")
        frame.setSize(800, 1200)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val text = JTextArea()

        val scrollPane = JScrollPane(text)

        text.isEditable = false

        console.addOnLogMessage {
            text.append("${it.message}\n")
            text.caretPosition = text.document.length;
        }

        frame.contentPane.add(scrollPane)
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}