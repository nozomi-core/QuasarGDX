package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.data.BackgroundData
import app.quasar.qgl.engine.core.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class BackgroundScript: GameNode<BackgroundData>() {

    private lateinit var texture: Texture

    override fun onCreate(argument: NodeArgument): BackgroundData {
        texture = Texture(Gdx.files.internal("sprites/example_scene.png"))
        return BackgroundData()
    }

    override fun onDestroy() {
        texture.dispose()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: BackgroundData) {
    }

    override fun onDraw(context: DrawContext, data: BackgroundData) {
        context.draw.texture(texture, 0f, 0f)
    }
}