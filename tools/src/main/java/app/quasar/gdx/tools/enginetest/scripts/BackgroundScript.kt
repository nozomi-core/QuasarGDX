package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.EngineTestWorld
import app.quasar.gdx.tools.enginetest.data.BackgroundData
import app.quasar.qgl.engine.core.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture

class BackgroundScript: GameNode<BackgroundData>() {

    private lateinit var texture: Texture

    override fun onCreate(argument: NodeArgument): BackgroundData {
        return BackgroundData()
    }

    override fun onAttach(self: SelfContext, engine: EngineApi, data: BackgroundData) {
        self.spawnChild(selfDimension, AmbientScript::class)
    }

    override fun onEnter() {
        texture = Texture(Gdx.files.internal("sprites/example_scene.png"))
    }

    override fun onExit() {
        texture.dispose()
    }

    override fun onDestroy() {
        texture.dispose()
    }

    override fun onSimulate(self: SelfContext, context: SimContext, data: BackgroundData) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.END)) {
            self.destroy()
        }
    }

    override fun onDraw(context: DrawContext, data: BackgroundData) {
        context.draw.texture(texture, 0f, 0f)
    }
}