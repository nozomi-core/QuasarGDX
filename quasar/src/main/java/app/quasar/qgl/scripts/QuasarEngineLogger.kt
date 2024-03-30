package app.quasar.qgl.scripts

import app.quasar.qgl.entity.GameNode

interface EngineLogger {
    fun sendMessage(message: String)
}

class QuasarEngineLogger: GameNode(), EngineLogger {
    override fun sendMessage(message: String) {
        println("Sending message from logger: $message")
    }
}