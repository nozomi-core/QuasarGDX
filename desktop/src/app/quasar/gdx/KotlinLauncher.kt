package app.quasar.gdx

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object CommandArg {
    const val IS_DEBUG = "-debug"
}

fun runKotlinGame(args: Array<String>) {
    //Setup command args
    val isDebug = args.contains(CommandArg.IS_DEBUG)

    //Setup Lwjgl
    val title = if(isDebug) "QuasarGDX - DEBUG" else "QuasarGDX"
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle(title)

    Lwjgl3Application(
        QuasarGame(
            QuasarConfig(isDebug = isDebug
        )
     ), config)
}