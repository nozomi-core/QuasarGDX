package app.quasar.gdx.tools.console

import app.quasar.qgl.engine.core.QuasarEngine
import app.quasar.qgl.scripts.EngineLog
import app.quasar.qgl.scripts.EngineLogLevel
import app.quasar.qgl.scripts.EngineLogger
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

class SwingGameConsole(engineApi: QuasarEngine) {
    private val console: EngineLogger = engineApi.requireFindByInterface(EngineLogger::class)

    init {
        setupEditor()
    }
    private fun setupEditor() {
        val text = JTextArea().apply {
            isEditable = true
        }

        JFrame("GameLogger").apply {
            val panel = JPanel(BorderLayout())

            setSize(800, 1200)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            contentPane.add(panel)
            panel.add(JScrollPane(text), BorderLayout.CENTER)
            setLocationRelativeTo(null)
            isVisible = true
        }

        console.addOnLogMessage {
            addJLogMessage(text, it)
        }
    }

    private fun addJLogMessage(text: JTextArea, log: EngineLog) {
        val part1 = when(log.level) {
            EngineLogLevel.ERROR -> "[ ERROR ])\t\t"
            EngineLogLevel.WARN ->"[ WARN ]\t\t"
            else -> "\t\t"
        }

        text.append("$part1${log.message}\n")
        text.caretPosition = text.document.length;
    }
}