package app.quasar.gdx.tools

import app.quasar.gdx.tools.console.EngineConsole
import app.quasar.gdx.tools.mapeditor.MapEditorApplication
import app.quasar.qgl.QuasarRuntime
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object CommandArgs {
    const val MAP_EDITOR = "map_editor"
}

fun runKotlinTooling(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle("Quasar Tools")
    config.setWindowedMode(1920, 1080)

    val toolArgument = args.find {
        it.contains("-tool=")
    } ?: return println("no tool in CLI specified")

    val toolName = toolArgument.split("=")[1]

    val runtime = QuasarRuntime()

    runtime.onWorldEngine {
        EngineConsole(it)
    }

    val gdxApp: ApplicationAdapter = when(toolName) {
        CommandArgs.MAP_EDITOR -> MapEditorApplication(runtime)
        else -> null
    } ?: return println("tool cannot be found try a proper name")

    Lwjgl3Application(gdxApp, config)
}