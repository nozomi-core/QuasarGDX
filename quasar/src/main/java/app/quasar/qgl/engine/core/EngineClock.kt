package app.quasar.qgl.engine.core

class EngineClock {
    internal var frameDeltaTime: Float = 0f

    var ticks: Int = 0
        internal set

    //Getters
    val deltaTime: Float get() = frameDeltaTime

    var tick2 = false
        internal set
    var tick4 = false
        internal set
    var tick32 = false
        internal set

    fun multiply(value: Float): Float {
        return deltaTime * value
    }

    internal fun tick() {
        ticks++

        tick2 = ticks % 2 == 0
        tick4 = ticks % 4 == 0
        tick32 = ticks % 32 == 0

        if(ticks > MAX_TICK_NUMBER) {
            ticks = 0
        }
    }

    companion object {
        const val MAX_TICK_NUMBER = 32
    }
}