package app.quasar.gdx.tools.console

import app.quasar.gdx.tools.Strings
import app.quasar.qgl.engine.core.QuasarEngine
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextField

class LoadWindow(private val callback: (String) -> Unit): JFrame() {
    init {
        setSize(300, 100)
        setLocation(100,100)
        setLocationRelativeTo(null)

        defaultCloseOperation = DISPOSE_ON_CLOSE
        setup()
        isVisible = true
    }

    private fun setup() {
        val panel = JPanel(BorderLayout())
        panel.layout = FlowLayout()
        contentPane.add(panel)

        val text = JTextField()
        text.setSize(200, 50)
        text.columns = 25

        panel.add(text)

        val button = JButton(Strings.LOAD)
        panel.add(button)

        button.addActionListener {
            callback(text.text)
            isVisible = false
            dispose()
        }
    }
}