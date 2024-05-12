package app.quasar.qgl.engine.core

class DrawContext(val draw: DrawableApi)

class SimContext(
    val engine: EngineApi,
    val clock: EngineClock
)