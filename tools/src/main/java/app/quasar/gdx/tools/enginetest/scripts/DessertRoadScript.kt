package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.data.BackgroundData
import app.quasar.qgl.engine.core.DrawContext
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class DessertRoadScript: GameNode<BackgroundData>() {

    private lateinit var texture: Texture

    override fun onCreate(argument: NodeArgument): BackgroundData {
        texture = Texture(Gdx.files.internal("sprites/example_scene_2.png"))
        return BackgroundData()
    }

    override fun onDestroy() {
        texture.dispose()
    }

    override fun onDraw(context: DrawContext, data: BackgroundData) {
       context.draw.texture(texture, 0f,0f)
    }
}