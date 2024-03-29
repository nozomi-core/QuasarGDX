package app.quasar.gdx

import app.quasar.gdx.lumber.Lumber
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import javax.swing.JButton
import javax.swing.JFrame


object CommandArg {
    const val IS_DEBUG = "-debug"
    const val IS_FULLSCREEN = "-fullscreen"
}

fun runKotlinGame(args: Array<String>) {
    //Setup command args
    val isDebug = args.contains(CommandArg.IS_DEBUG)

    //Setup Lwjgl
    val title = if(isDebug) "QuasarGDX - DEBUG" else "QuasarGDX"
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle(title)
    config.setWindowedMode(1920, 1080)

    if(args.contains(CommandArg.IS_FULLSCREEN)) {
        config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())
    }

    setupEditor()

    Lwjgl3Application(
        QuasarGame(
            QuasarConfig(isDebug = isDebug
        )
     ), config)



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