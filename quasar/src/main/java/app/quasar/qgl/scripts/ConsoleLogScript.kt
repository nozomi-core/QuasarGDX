package app.quasar.qgl.scripts

import app.quasar.qgl.engine.core.GameNodeUnit
import kotlin.reflect.KClass

interface ConsoleLog {
    fun message(source: Any, message: String)
    fun warn(source: Any, message: String)
    fun send(log: EngineLog)
    fun addOnLogMessage(callback: LogCallback)
}

class ConsoleLogScript: GameNodeUnit(), ConsoleLog {
    private val callbacks = mutableListOf<LogCallback>()

    override fun send(log: EngineLog) {
        callbacks.forEach {
            it.invoke(log)
        }
    }

    override fun addOnLogMessage(callback: LogCallback) {
        callbacks.add(callback)
    }

    override fun message(source: Any, message: String) {
        send(EngineLog(source = source::class, message = message, level = EngineLogLevel.MESSAGE))
    }

    override fun warn(source: Any, message: String) {
        send(EngineLog(source = source::class, message = message, level = EngineLogLevel.WARN))
    }
}

typealias LogCallback = (log: EngineLog) -> Unit

data class EngineLog(
    val source: KClass<*>,
    val message: String,
    val level: EngineLogLevel
)

enum class EngineLogLevel {
    MESSAGE,
    WARN,
    ERROR
}