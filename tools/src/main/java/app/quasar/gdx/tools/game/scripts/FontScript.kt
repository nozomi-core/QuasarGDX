package app.quasar.gdx.tools.game.scripts

import app.quasar.gdx.tools.canvas
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNodeUnit
import app.quasar.qgl.engine.core.interfaces.GameOverlay
import app.quasar.qgl.engine.core.interfaces.GameOverlayShape
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class FontScript: GameNodeUnit(), GameOverlay, GameOverlayShape {

    private val font = BitmapFont().apply {
        color = Color.BLACK
    }

    override fun onDrawOverlay(context: DrawContext) {
        context.render { batch ->
            font.draw(batch, "Hey", 100f,100f)
        }
    }

    override fun onDrawShape(shape: ShapeRenderer) {
        shape.canvas(ShapeRenderer.ShapeType.Filled) {
            color = Color.LIGHT_GRAY
            rect(10f, 10f, 35f, 35f)
        }

        shape.canvas(ShapeRenderer.ShapeType.Line) {
            color = Color.NAVY
            rect(10f, 10f, 35f, 35f)
        }
    }
}