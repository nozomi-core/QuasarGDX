package app.quasar.qgl.entity

open class RootNode: GameNode() {
    protected open fun onRootCreated(){}

    internal fun doRootCreated() {
        onRootCreated()
    }
}