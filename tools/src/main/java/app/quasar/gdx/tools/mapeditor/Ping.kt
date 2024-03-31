package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger
import app.quasar.qgl.scripts.QuasarEngineLogger
import kotlin.reflect.KClass

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
            val message = "ping: ${System.currentTimeMillis()}"
            logger.message(this, message)
            callbacks.forEach {
                it(message)
            }
            totalTime = 0.0
        }
    }

    override fun shouldRunBefore(): List<KClass<*>> {
        return listOf(QuasarEngineLogger::class)
    }

    override fun onRootCreated() {
        super.onRootCreated()
        //logger = engineApi.requireFindByInterface(EngineLogger::class)
    }
}