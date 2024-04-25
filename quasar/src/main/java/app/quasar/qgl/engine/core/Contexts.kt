package app.quasar.qgl.engine.core

class SimContext(
    val engine: EngineApi,
    val clock: EngineClock
)

class SetupContext(
    val engine: EngineApi
)

class DrawContext(
    val draw: DrawableApi,
    val camera: CameraApi
)