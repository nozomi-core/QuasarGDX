package app.quasar.gdx.lumber

object Lumber {
    private var config: LumberConfig? = null

    fun setup(setupConfig: LumberConfig) {
        if(config == null) {
            config = setupConfig
        } else {
            warn("Lumber setup should only be called once, we ignore any further called to setup")
        }
    }

    fun debug(message: String) {
        if(config?.isDebug == true) {
            print(message)
        }
    }

    fun warn(message: String) {
        print("WARNING -----> $message")
    }
}