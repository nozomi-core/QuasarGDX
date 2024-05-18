package app.quasar.gdx.tools.controls

import app.quasar.gdx.tools.enginetest.scripts.PlayerScript
import app.quasar.qgl.engine.core.QuasarEngine
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class PlayerControls(private val engine: QuasarEngine, val script: PlayerScript): JFrame() {

    init {
        setSize(800, 1200)
        setLocation(100,100)
        isVisible = true

        val panel = JPanel(BorderLayout())

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        contentPane.add(panel)

        val button = JButton("Move")
        button.addActionListener {

        }
        panel.add(button, BorderLayout.NORTH)
    }

}