package app.quasar.qgl.engine.core

class EngineClock {

    var deltaTime: Float = 0f
        internal set

    fun mulDeltaTime(value: Float) = deltaTime * value
}