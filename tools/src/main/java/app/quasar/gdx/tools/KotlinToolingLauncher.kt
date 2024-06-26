package app.quasar.gdx.tools

import app.quasar.gdx.tools.console.QuasarToolConsole
import app.quasar.gdx.tools.enginetest.EngineTestApplication
import app.quasar.qgl.engine.CommonRuntime
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object CommandArgs {
    const val MAP_EDITOR = "map_editor"
    const val DEBUG_GAME = "debug_game"
}

fun runKotlinTooling(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle("Quasar Tools")
    config.setWindowedMode(1920, 1080)

    val toolArgument = args.find {
        it.contains("-tool=")
    } ?: return println("no tool in CLI specified")

    val runtime = CommonRuntime()

    QuasarToolConsole(runtime)

    //TODO: add command args back
    val gdxApp: ApplicationListener = when(toolArgument.split("=")[1]) {
        CommandArgs.MAP_EDITOR -> EngineTestApplication(runtime)
        CommandArgs.DEBUG_GAME -> EngineTestApplication(runtime)
        else -> null
    } ?: return println("tool cannot be found try a proper name")

    Lwjgl3Application(gdxApp, config)
}