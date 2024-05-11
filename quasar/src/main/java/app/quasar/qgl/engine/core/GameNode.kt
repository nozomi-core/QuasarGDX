package app.quasar.qgl.engine.core

abstract class GameNode: ReadableGameNode {

    var tag: String? = null
        private set

    protected open fun onSimulate() {}
    protected open fun onCreate() {}

    internal fun create(factory: NodeFactory) {
        this.tag = factory.tag
        onCreate()
    }

    internal fun simulate() {
        onSimulate()
    }
}