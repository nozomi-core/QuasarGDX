package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay1
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class SimContext1(
    val engine: EngineApi1,
    val clock: EngineClock1
)

class SetupContext1(
    val engine: EngineApi1,
    val registerOverlay: (GameOverlay1) -> Unit
)

class DrawContext1(
    val draw: DrawableApi1,
    val camera: CameraApi1,
    val screen: OverlayScreen1,
    val render: (callback: (SpriteBatch) -> Unit) -> Unit
)