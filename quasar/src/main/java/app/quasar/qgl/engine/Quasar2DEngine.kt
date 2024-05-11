package app.quasar.qgl.engine

import app.quasar.qgl.QuasarRuntime
import app.quasar.qgl.engine.core1.*
import app.quasar.qgl.render.CameraApiActual
import app.quasar.qgl.render.DrawableApiActual1
import app.quasar.qgl.QuasarCoreScripts
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Quasar2DEngine(
    private val runtime: QuasarRuntime,
    private val config: QuasarEngine2DConfig,
    private val uiHooks: GameWindow,
): Disposable {
    private val drawableApi = config.createDrawApi()

    private val shapeContext = ShapeContext1(ShapeRenderer(), config.screen)

    private val drawContext = DrawContext1(drawableApi, CameraApiActual(uiHooks), config.screen) {
        it(config.spriteBatch)
    }

    private val engineApi: QuasarEngine1 = QuasarEngineActual1(
        deserialized = null,
        drawContext = drawContext,
        onExit = {},
        frameworkScripts = QuasarCoreScripts.scripts
    )

    fun <T: GameWorld> createWorld(kClass: KClass<T>) {
        kClass.createInstance().apply {
           /*createRootScripts(useRootScripts())
           create(engineApi)
           runtime.sendWorldEngine(engineApi)*/
       }
    }

    private fun createRootScripts(scripts: List<KClass<*>>) {
        val rootScripts = scripts.filterIsInstance<KClass<GameNode1<*>>>()
        engineApi.createStartScripts(rootScripts)
    }

    fun render() {
        val spriteBatch = config.spriteBatch

        //Draw world frame
        uiHooks.getWorldViewport().apply()
        uiHooks.getWorldCamera().update()
        spriteBatch.projectionMatrix = uiHooks.getWorldCamera().combined
        spriteBatch.begin()
        engineApi.simulate(Gdx.graphics.deltaTime)
        engineApi.draw()
        spriteBatch.end()

        //Shape renderer
        shapeContext.shape.projectionMatrix = uiHooks.getOverlayCamera().combined
        engineApi.drawShapes(shapeContext)

        //DrawUI
        uiHooks.getOverlayViewport().apply()
        uiHooks.getOverlayCamera().update()
        spriteBatch.projectionMatrix = uiHooks.getOverlayCamera().combined
        spriteBatch.begin()
        engineApi.drawOverlay()
        spriteBatch.end()
    }

    override fun dispose() {
        config.spriteBatch.dispose()
        config.texture.dispose()
        shapeContext.shape.dispose()
    }

    private fun createTileTextures(tileset: GameTileset, tileSize: Int, texture: Texture): TileTextures {
        return TilesetBuilder(tileSize, texture).also { builder ->
            tileset.onCreateTiles(builder)
        }.build()
    }

    private fun QuasarEngine2DConfig.createDrawApi(): DrawableApi1 {
        return DrawableApiActual1(
            tileSize,
            createTileTextures(
                tileset,
                tileSize,
                texture
            ),
            spriteBatch
        )
    }
}

