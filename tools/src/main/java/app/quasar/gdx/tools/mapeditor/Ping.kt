package app.quasar.gdx.tools.mapeditor

import app.quasar.qgl.entity.GameNode

typealias PingCallback = (message: String) -> Unit

interface Pingable {
    fun addCallback(callback: PingCallback)
}

class Ping: GameNode(), Pingable {
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
                it("ping: ${System.currentTimeMillis()}")
            }
            totalTime = 0.0
        }
    }
}