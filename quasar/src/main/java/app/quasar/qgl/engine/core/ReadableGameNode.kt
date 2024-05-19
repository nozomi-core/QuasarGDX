package app.quasar.qgl.engine.core

interface ReadableGameNode {
    val isAlive: Boolean
    val nodeId: Long
    val tag: String
}