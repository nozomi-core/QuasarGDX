package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class SimContext(
    val engine: EngineApi,
    val clock: EngineClock
)

class SetupContext(
    val engine: EngineApi,
    val registerOverlay: (GameOverlay) -> Unit
)

class DrawContext(
    val draw: DrawableApi,
    val camera: CameraApi,
    val screen: OverlayScreen,
    val render: (callback: (SpriteBatch) -> Unit) -> Unit
)