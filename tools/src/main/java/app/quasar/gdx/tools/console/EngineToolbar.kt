package app.quasar.gdx.tools.console

import app.quasar.gdx.tools.Strings
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

fun displayEngineToolbar(frame: JFrame, callback: (MenuOption) -> Unit) {
    val menuBar = JMenuBar()

    //File
    val fileMenu = JMenu(Strings.ENGINE).apply {
        menuBar.add(this)
    }

    //Menu Options
    JMenuItem(Strings.SAVE).apply {
        fileMenu.add(this)
        addActionListener {
            callback(MenuOption.SAVE)
        }
    }

    JMenuItem(Strings.LOAD).apply {
        fileMenu.add(this)
        addActionListener {
            callback(MenuOption.LOAD)
        }
    }

    JMenuItem(Strings.PAUSE).apply {
        fileMenu.add(this)
        addActionListener {
            callback(MenuOption.PAUSE)
        }
    }

    JMenuItem(Strings.RESUME).apply {
        fileMenu.add(this)
        addActionListener {
            callback(MenuOption.RESUME)
        }
    }

    JMenuItem(Strings.SHUTDOWN).apply {
        fileMenu.add(this)
        addActionListener {
            callback(MenuOption.SHUTDOWN)
        }
    }

    frame.jMenuBar = menuBar
}

enum class MenuOption {
    SAVE,
    LOAD,
    PAUSE,
    RESUME,
    SHUTDOWN
}