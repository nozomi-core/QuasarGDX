package app.quasar.qgl.engine.core

interface SelfContext {
    fun setDimension(dimension: EngineDimension)
    fun destroy()
}