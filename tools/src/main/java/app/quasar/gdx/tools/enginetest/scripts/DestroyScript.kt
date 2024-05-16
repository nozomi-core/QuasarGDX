package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.qgl.engine.core.*

class DestroyScript: GameNode<Unit>() {

    private var totalTime = 0f

    override fun onCreate(argument: NodeArgument) {}

    override fun onSimulate(self: SelfContext, context: SimContext, data: Unit) {
        totalTime += context.clock.deltaTime

        if(totalTime > 10) {
            self.destroy()
        }
    }

    override fun onDraw(context: DrawContext, data: Unit) {
        context.draw.tilePx(CoreTiles.SIGNAL_REGULAR, 64f, 64f)
    }
}