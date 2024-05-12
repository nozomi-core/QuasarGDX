package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3

class ProjectScript: GameNode<Unit>() {

    private var position = Vector3()

    override fun onCreate(argument: NodeArgument) {}

    override fun onSimulate(self: SelfContext, context: SimContext, data: Unit) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            val mouseX = Gdx.input.x.toFloat()
            val mouseY = Gdx.input.y.toFloat()

            position = context.project.unprojectWorld(Vector3(mouseX, mouseY, 0f))
        }
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        context.draw.tilePx(CoreTiles.RED_DARK, position)
    }
}