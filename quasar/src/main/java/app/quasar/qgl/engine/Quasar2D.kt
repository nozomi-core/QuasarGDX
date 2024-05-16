package app.quasar.qgl.engine

import app.quasar.qgl.engine.core.QuasarEngineActual
import app.quasar.qgl.render.CameraApiActual
import app.quasar.qgl.render.DrawableApiActual
import app.quasar.qgl.render.ProjectionApiActual
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Quasar2D(
    val textureFile: String,
    val tileset: GameTileset,
    private val tileSize: Int,
    private val window: GameWindow,
): Disposable {
    private val spriteBatch = SpriteBatch()
    private val texture = Texture(textureFile)

    private val engine = QuasarEngineActual {
        drawable = DrawableApiActual(createTileTextures(texture, tileset, tileSize), spriteBatch)
        camera = CameraApiActual(window)
        project = ProjectionApiActual(window.getWorldCamera())
    }

    fun <T: GameWorld> applyWorld(kClass: KClass<T>) {
        kClass.createInstance().apply {
            create(engine)
        }
    }

    fun render() {
        window.getWorldViewport().apply()
        window.getWorldCamera().update()
        spriteBatch.projectionMatrix = window.getWorldCamera().combined
        spriteBatch.begin()
        engine.simulate(Gdx.graphics.deltaTime)
        engine.draw()
        spriteBatch.end()
    }

    override fun dispose() {
        spriteBatch.dispose()
    }

    private fun createTileTextures(texture: Texture, tileset: GameTileset, tileSize: Int): TileTextures {
        return TilesetBuilder(tileSize, texture).also { builder ->
            tileset.onCreateTiles(builder)
        }.build()
    }
}