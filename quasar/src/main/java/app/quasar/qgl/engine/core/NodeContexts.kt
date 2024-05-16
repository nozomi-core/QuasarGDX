package app.quasar.qgl.engine.core

class DrawContext(
    val draw: DrawableApi,
    val camera: CameraApi
)

class SimContext(
    val engine: EngineApi,
    val clock: EngineClock,
    val project: ProjectionApi
)