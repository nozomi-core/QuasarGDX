package app.quasar.qgl.engine.core

class EngineClock {

    private var deltaTime: Float = 0f

    internal fun update(deltaTime: Float) {
        this.deltaTime = deltaTime
    }
}