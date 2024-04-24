package app.quasar.qgl.engine.core

class EngineClock {
    internal var frameDeltaTime: Float = 0f
    val deltaTime: Float get() = frameDeltaTime

    fun multiply(value: Float): Float {
        return deltaTime * value
    }
}