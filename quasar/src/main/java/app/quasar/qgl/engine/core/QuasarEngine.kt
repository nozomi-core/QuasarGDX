package app.quasar.qgl.engine.core

interface QuasarEngine: EngineApi {
    fun notifyDimensionChanged(node: GameNode<*>)
    fun destroyNode(node: GameNode<*>)
    fun saveToFile(filename: String)
    fun shutdown()
    fun pause()
    fun resume()
}