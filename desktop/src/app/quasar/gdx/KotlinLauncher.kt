package app.quasar.gdx

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

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

    /*
    Lwjgl3Application(
        CoreGame(
            CoreRuntime(),
            CoreConfig(isDebug = isDebug
        )
     ), config)*/
}