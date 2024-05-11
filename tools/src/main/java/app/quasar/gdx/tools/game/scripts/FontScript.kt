package app.quasar.gdx.tools.game.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.canvas
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.engine.core.interfaces.GameOverlay1
import app.quasar.qgl.engine.core.interfaces.GameOverlayShape1
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class FontScript: GameNodeUnit1(), GameOverlay1, GameOverlayShape1 {

    private val font = BitmapFont().apply {
        color = Color.BLACK
        region.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    private var timer = 0f

    override fun onSetup(context: SetupContext1, data: Unit) {
        context.registerOverlay(this)
    }

    override fun onSimulate(context: SimContext1, self: SelfContext1, data: Unit) {
        timer += context.clock.deltaTime
        if(timer > 20f) {
            self.destroyNode()
        }
    }

    override fun onDraw(context: DrawContext1, data: Unit) {
        context.draw.tilePx(CoreTiles.SIGNAL_REGULAR, -32f, -32f)
    }

    override fun onDrawOverlay(context: DrawContext1) {
        val screen = context.screen

        context.render { batch ->
            font.draw(batch, "Season: ->", screen.leftEdge,screen.topEdge)
        }
    }

    override fun onDrawShape(context: ShapeContext1) {
        val screen = context.screen

        context.shape.canvas(ShapeRenderer.ShapeType.Filled) {
            color = Color.WHITE
            screen.uiTransform(screen.leftEdge, screen.topEdge, 150f, 150f) { x, y, width, height ->
                rect(x, y, width, height)
            }
        }
    }
}