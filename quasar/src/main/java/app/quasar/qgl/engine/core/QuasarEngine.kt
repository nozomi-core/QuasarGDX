package app.quasar.qgl.engine.core

interface QuasarEngine: EngineApi {
    fun destroyNode(node: GameNode<*>)
}