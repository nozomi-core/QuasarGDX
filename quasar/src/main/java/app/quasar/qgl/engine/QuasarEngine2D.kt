package app.quasar.qgl.engine

import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.render.DrawableApiQuasar
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngine2D(
    private val config: QuasarEngine2DConfig,
    private val callbacks: EngineCallbacks,
): Disposable {
    private val drawableApi = config.createDrawApi()

    private var world: GameWorld? = null
    private var overlay: GameOverlay? = null

    private val engineApi = QuasarEngineApi(drawableApi)

    fun <T: GameWorld> createWorld(kClass: KClass<T>) {
       this.world = kClass.createInstance().apply {
           onCreateRoot(engineApi)
           onCreate(engineApi)
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
        engineApi.simulate(Gdx.graphics.deltaTime)
        engineApi.draw()
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


    private fun QuasarEngine2DConfig.createDrawApi(): DrawableApi {
        return DrawableApiQuasar(
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

