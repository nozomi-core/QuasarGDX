package app.quasar.gdx.tools.console

import app.quasar.gdx.tools.controls.PlayerControls
import app.quasar.gdx.tools.enginetest.scripts.PlayerScript
import app.quasar.qgl.engine.CommonRuntime
import app.quasar.qgl.engine.core.QuasarEngine
import app.quasar.qgl.engine.core.ReadableGameNode
import java.awt.BorderLayout
import javax.swing.*

class QuasarToolConsole(
    runtime: CommonRuntime
): JFrame() {
    private var engine: QuasarEngine? = null

    //private lateinit var mainPanel: JPanel
    //private lateinit var nodeList: JList<ReadableGameNode>

    private var selected: ReadableGameNode? = null

    init {
        runtime.addWorldListener { engine = it }
        setSize(800, 1200)
        setLocation(100,100)
        setLocationRelativeTo(null)
        isVisible = true
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        setup()
    }

    private fun setup() {
        mainPanel { mainPanel ->
            nodeList(mainPanel) { nodeList ->
                updateButton(mainPanel) {

                }
            }
        }
    }

    private fun mainPanel(callback: (JPanel) -> Unit) {
        callback(JPanel(BorderLayout()).apply {
            contentPane.add(this)
        })
    }

    private fun updateButton(panel: JPanel, onClick: ()-> Unit) {
        val button = JButton("Update")
        panel.add(button, BorderLayout.NORTH)
        button.addActionListener {
            onClick()
        }
    }

    private fun nodeList(panel: JPanel, callback: (JList<ReadableGameNode>) -> Unit) {
        callback(JList<ReadableGameNode>().apply {
            addListSelectionListener { event ->
                if (!event.valueIsAdjusting) {
                    val source = event.source as JList<ReadableGameNode>
                    selected = source.selectedValue
                }
            }
            panel.add(this, BorderLayout.CENTER)
        })
    }

    private fun onUpdateClick(nodeList: JList<ReadableGameNode>) {
        val model = DefaultListModel<ReadableGameNode>()

        engine?.queryAll()?.forEach {  reference ->
            reference.get()?.let { node ->
                model.addElement(node)
            }
        }
        nodeList.model = model
    }

    private fun onClickView(node: ReadableGameNode) {
        val engine = this.engine!!
        when(node) {
            is PlayerScript -> PlayerControls(engine, node)
        }
    }
}

