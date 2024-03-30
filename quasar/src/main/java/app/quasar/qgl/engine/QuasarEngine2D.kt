package app.quasar.qgl.engine

import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.render.DrawableApi
import app.quasar.qgl.render.DrawableApiQuasar
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngine2D(
    private val runtime: QuasarRuntime,
    private val config: QuasarEngine2DConfig,
    private val engineHooks: EngineHooks,
): Disposable {
    private val drawableApi = config.createDrawApi()

    private var world: GameWorld? = null
    private var overlay: GameOverlay? = null

    private val engineApi = QuasarEngineApi(drawableApi)

    fun <T: GameWorld> createWorld(kClass: KClass<T>) {
       this.world = kClass.createInstance().apply {
           createRootScripts(useRootScripts())
           onCreate(engineApi)
           runtime.postWorldEngine(engineApi)
       }
    }

    private fun createRootScripts(scripts: List<KClass<*>>) {
        val rootScripts = scripts.filterIsInstance<KClass<GameNode>>()
        engineApi.createRootScripts(rootScripts)
    }

    fun <T: GameOverlay> createOverlay(overlay: KClass<T>) {
        this.overlay = overlay.createInstance().apply {
            onCreate()
        }
    }

    fun render() {
        val spriteBatch = config.spriteBatch

        //Draw world frame
        engineHooks.useWorldViewport().apply()
        engineHooks.useWorldCamera().update()
        spriteBatch.projectionMatrix = engineHooks.useWorldCamera().combined
        spriteBatch.begin()
        engineApi.simulate(Gdx.graphics.deltaTime)
        engineApi.draw()
        spriteBatch.end()

        //DrawUI
        engineHooks.useOverlayViewport().apply()
        engineHooks.useOverlayCamera().update()
        spriteBatch.projectionMatrix = engineHooks.useOverlayCamera().combined
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

