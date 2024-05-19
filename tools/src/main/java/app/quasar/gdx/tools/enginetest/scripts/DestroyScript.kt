package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tiles.CoreTiles
import app.quasar.gdx.tools.enginetest.data.DestroyData
import app.quasar.qgl.engine.core.*

class DestroyScript: GameNode<DestroyData>() {

    override fun onCreate(argument: NodeArgument): DestroyData {
        return DestroyData(
            totalTime = 0f
        )
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: DestroyData) {
        data.totalTime += context.clock.deltaTime

        if(data.totalTime > 20) {
            self.destroy()
        }
    }

    override fun onDraw(context: DrawContext, data: DestroyData) {
        context.draw.tilePx(CoreTiles.SIGNAL_REGULAR, 64f, 64f)
    }
}