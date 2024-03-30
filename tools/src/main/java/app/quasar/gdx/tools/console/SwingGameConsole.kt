package app.quasar.gdx.tools.console

import app.quasar.gdx.lumber.Lumber
import app.quasar.gdx.tools.mapeditor.Pingable
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.scripts.EngineLogger
import javax.swing.JButton
import javax.swing.JFrame

class SwingGameConsole(private val engineApi: EngineApi) {
    private val console: EngineLogger = engineApi.requireFindByInterface(EngineLogger::class)

    init {
        setupEditor()
    }
    private fun setupEditor() {

        //Swing window
        val frame = JFrame("GameLogger")

        // Set the size of the JFrame

        // Set the size of the JFrame
        frame.setSize(600, 800)

        // Set the default close operation to exit the application when the window is closed

        // Set the default close operation to exit the application when the window is closed
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        // Create a JLabel to display text

        // Create a JLabel to display text
        val button = JButton("Click Me")

        console.addOnLogMessage {
            button.text = it.message
        }

        button.addActionListener {
            Lumber.debug("SwingThread :${Thread.currentThread().name}")
        }
        // Add the label to the content pane of the JFrame

        // Add the label to the content pane of the JFrame
        frame.contentPane.add(button)

        // Center the JFrame on the screen

        // Center the JFrame on the screen
        frame.setLocationRelativeTo(null)

        // Make the JFrame visible

        // Make the JFrame visible
        frame.isVisible = true
    }
}