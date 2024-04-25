package app.quasar.gdx.tools.mapeditor

import app.quasar.gdx.CoreAssets
import app.quasar.qgl.engine.core.GameNodeUnit
import app.quasar.qgl.engine.core.SetupContext
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

class AudioScript: GameNodeUnit() {

    private val tracks = arrayOf(CoreAssets.Music.PIANO, CoreAssets.Music.AMBIENT_WAVE)
    private var currentIndex = 0

    private var music: Music? = null

    override fun onSetup(context: SetupContext, data: Unit?) {
        playNextTrack()
    }

    private fun playNextTrack() {
        music?.dispose()
        music = Gdx.audio.newMusic(Gdx.files.internal(tracks[currentIndex])).apply {
            setOnCompletionListener { oldMusic ->
                currentIndex ++
                currentIndex %= tracks.size

                oldMusic.setOnCompletionListener(null)
                oldMusic.dispose()
                playNextTrack()
            }
            play()
        }
    }
}