package app.quasar.qgl.engine

import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.render.DrawableApiImp
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngine2D(
    private val config: Quasar2DConfig,
    private val callbacks: QuasarCallbacks,
): Disposable {
    private val drawableApi = config.createDrawApi()

    private var world: GameWorld? = null
    private var overlay: GameOverlay? = null

    fun <T :GameWorld> createWorld(kClass: KClass<T>) {
       this.world = kClass.createInstance().apply {
           onCreate()
       }
    }

    fun <T: GameOverlay> createOverlay(overlay: KClass<T>) {
        this.overlay = overlay.createInstance().apply {
            onCreate()
        }
    }

    fun render() {
        val spriteBatch = config.spriteBatch

        //Draw world frame
        callbacks.useWorldViewport().apply()
        callbacks.useWorldCamera().update()
        spriteBatch.projectionMatrix = callbacks.useWorldCamera().combined
        spriteBatch.begin()
        world?.onSimulate(Gdx.graphics.deltaTime)
        world?.onDraw(drawableApi)
        spriteBatch.end()

        //DrawUI
        callbacks.useOverlayViewport().apply()
        callbacks.useOverlayCamera().update()
        spriteBatch.projectionMatrix = callbacks.useOverlayCamera().combined
        spriteBatch.begin()
        overlay?.onDraw(drawableApi)
        spriteBatch.end()
    }

    override fun dispose() {
        config.spriteBatch.dispose()
        config.texture.dispose()
    }

    private fun createTileTextures(tileset: GameTileset, tileSheetLayout: TileSheetLayout, texture: Texture): TileTextures {
        return TilesetBuilder(tileSheetLayout, texture).also { builder ->
            tileset.onCreateTiles(builder)
        }.build()
    }


    private fun Quasar2DConfig.createDrawApi(): DrawableApi {
        return DrawableApiImp(
            layout,
            createTileTextures(
                tileset,
                layout,
                texture
            ),
            spriteBatch
        )
    }
}

