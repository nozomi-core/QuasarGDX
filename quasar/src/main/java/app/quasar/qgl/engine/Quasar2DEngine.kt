package app.quasar.qgl.engine

import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.engine.core.*
import app.quasar.qgl.render.CameraApiActual
import app.quasar.qgl.render.DrawableApiActual
import app.quasar.qgl.QuasarCoreScripts
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.Disposable
import java.awt.Shape
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Quasar2DEngine(
    private val runtime: QuasarRuntime,
    private val config: QuasarEngine2DConfig,
    private val uiHooks: UiHooks,
): Disposable {
    private val drawableApi = config.createDrawApi()

    private val shapeContext = ShapeContext(ShapeRenderer(), config.screen)

    private val drawContext = DrawContext(drawableApi, CameraApiActual(uiHooks), config.screen) {
        it(config.spriteBatch)
    }

    private val engineApi: QuasarEngine = QuasarEngineActual(
        deserialised = null,
        drawContext = drawContext,
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

        //Shape renderer
        shapeContext.shape.projectionMatrix = uiHooks.useOverlayCamera().combined
        engineApi.drawShapes(shapeContext)

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
        shapeContext.shape.dispose()
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

