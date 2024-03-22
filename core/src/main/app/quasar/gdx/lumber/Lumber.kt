package app.quasar.gdx.lumber

class LumberConfig() {
    var isDebug = false
}

fun setupLumber(callback: LumberConfig.() -> Unit) {
    val dslConfig = LumberConfig()
    callback(dslConfig)
    Lumber.setup(dslConfig)
}

object Lumber {
    private var config: LumberConfig? = null

    fun setup(dsl: LumberConfig) {
        config = dsl
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