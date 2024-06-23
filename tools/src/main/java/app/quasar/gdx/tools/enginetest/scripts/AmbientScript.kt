package app.quasar.gdx.tools.enginetest.scripts

import app.quasar.gdx.tools.enginetest.data.BackgroundData
import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class AmbientScript: GameNode<BackgroundData>() {
    private lateinit var sound: Sound

    override fun onCreate(argument: NodeArgument): BackgroundData {
        return BackgroundData()
    }

    override fun onEnter() {
        // Load the sound file
        sound = Gdx.audio.newSound(Gdx.files.internal("music/ambient.wav"))
        // Play the sound
        sound.play(1.0f);
    }

    override fun onExit() {
        sound.stop()
        sound.dispose()
    }
}