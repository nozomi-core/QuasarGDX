package app.quasar.qgl.helpers

class TimeCounter(private val triggerSeconds: () -> Float) {
    private var counter = 0.0
    private var nextTrigger = triggerSeconds()

    fun simulate(deltaSeconds: Float, callback: () -> Unit) {
        counter += deltaSeconds
        if(counter >= nextTrigger) {
            callback()
            counter = 0.0
            nextTrigger = triggerSeconds()
        }
    }
}