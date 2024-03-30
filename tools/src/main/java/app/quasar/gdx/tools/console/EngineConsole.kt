package app.quasar.gdx.tools.console

import app.quasar.gdx.lumber.Lumber
import app.quasar.gdx.tools.mapeditor.Ping
import app.quasar.gdx.tools.mapeditor.Pingable
import app.quasar.qgl.engine.EngineApi
import javax.swing.JButton
import javax.swing.JFrame

class EngineConsole(private val engineApi: EngineApi) {
    val ping: Pingable = engineApi.requireFindByInterface(Pingable::class)

    init {
        setupEditor()
    }
    private fun setupEditor() {

        //Swing window
        val frame = JFrame("My Swing Window")

        // Set the size of the JFrame

        // Set the size of the JFrame
        frame.setSize(400, 300)

        // Set the default close operation to exit the application when the window is closed

        // Set the default close operation to exit the application when the window is closed
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        // Create a JLabel to display text

        // Create a JLabel to display text
        val button = JButton("Click Me")

        ping.addCallback {
            button.text = it
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