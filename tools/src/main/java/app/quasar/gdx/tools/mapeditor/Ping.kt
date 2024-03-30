package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger

typealias PingCallback = (message: String) -> Unit

interface Pingable {
    fun addCallback(callback: PingCallback)
}

class Ping: RootNode(), Pingable {
    private lateinit var logger: EngineLogger

    private val callbacks = mutableListOf<PingCallback>()
    private var totalTime = 0.0

    override fun addCallback(callback: PingCallback) {
        callbacks.add(callback)
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        totalTime += deltaTime
        if(totalTime >= 10) {
            callbacks.forEach {
                val message = "ping: ${System.currentTimeMillis()}"
                it(message)
                logger.sendMessage(message)
            }
            totalTime = 0.0
        }
    }

    override fun onRootCreated() {
        super.onRootCreated()
        logger = engineApi.requireFindByInterface(EngineLogger::class)
    }
}