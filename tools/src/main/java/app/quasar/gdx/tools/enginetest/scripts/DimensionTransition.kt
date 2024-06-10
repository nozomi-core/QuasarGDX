package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.data.BackgroundData
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.core.SelfContext
import app.quasar.qgl.engine.core.SimContext

class DimensionTransition: GameNode<BackgroundData>() {

    private val TIMEOUT_SECONDS = 1f
    private var globalTimer = 0f

    private var isFadeOut = true
    private var fadeTimer = 0f
    private val TotalFadeTime = TIMEOUT_SECONDS / 2

    override fun onCreate(argument: NodeArgument): BackgroundData {
        return BackgroundData()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: BackgroundData) {
        globalTimer += context.clock.deltaTime

        if(globalTimer > TIMEOUT_SECONDS) {
            self.destroy()
        } else {
            //calculate fade position
            fadeTimer += context.clock.deltaTime
            if(fadeTimer > TotalFadeTime) {
                fadeTimer = 0f
                isFadeOut = !isFadeOut
            }

            //perform fade
            var fadePercent = fadeTimer / TotalFadeTime
            if(isFadeOut) {
                fadePercent = 1 - fadePercent
            }

            context.render.setAlpha(fadePercent)
        }
    }
}