package app.quasar.qgl.engine.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class SimContext(
    val engine: EngineApi,
    val clock: EngineClock
)

class SetupContext(
    val engine: EngineApi
)

class DrawContext(
    val draw: DrawableApi,
    val camera: CameraApi,
    val screen: OverlayScreen,
    val render: (callback: (SpriteBatch) -> Unit) -> Unit
)