package app.quasar.qgl.engine

import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.render.CameraApiActual
import app.quasar.qgl.render.DrawableApiActual
import app.quasar.qgl.QuasarCoreScripts
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Quasar2DEngine(
    private val runtime: QuasarRuntime,
    private val config: QuasarEngine2DConfig,
    private val uiHooks: UiHooks,
): Disposable {
    private val drawableApi = config.createDrawApi()

    private val engineApi: QuasarEngine = QuasarEngineActual(
        deserialised = null,
        drawContext = DrawContext(drawableApi, CameraApiActual(uiHooks)),
        onExit = {},
        frameworkScripts = QuasarCoreScripts.scripts
    )

    fun <T: GameWorld> createWorld(kClass: KClass<T>) {
        kClass.createInstance().apply {
           createRootScripts(useRootScripts())
           create(engineApi)
           runtime.sendWorldEngine(engineApi)
       }
    }

    private fun createRootScripts(scripts: List<KClass<*>>) {
        val rootScripts = scripts.filterIsInstance<KClass<GameNode<*>>>()
        engineApi.createStartScripts(rootScripts)
    }

    fun render() {
        val spriteBatch = config.spriteBatch

        //Draw world frame
        uiHooks.useWorldViewport().apply()
        uiHooks.useWorldCamera().update()
        spriteBatch.projectionMatrix = uiHooks.useWorldCamera().combined
        spriteBatch.begin()
        engineApi.simulate(Gdx.graphics.deltaTime)
        engineApi.draw()
        spriteBatch.end()

        //DrawUI
        uiHooks.useOverlayViewport().apply()
        uiHooks.useOverlayCamera().update()
        spriteBatch.projectionMatrix = uiHooks.useOverlayCamera().combined
        spriteBatch.begin()
        engineApi.drawOverlay()
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
        return DrawableApiActual(
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

