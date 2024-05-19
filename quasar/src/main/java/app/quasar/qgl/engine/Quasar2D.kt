package app.quasar.qgl.engine

import app.quasar.qgl.engine.serialize.EngineDeserialize
import app.quasar.qgl.engine.core.QuasarEngineActual
import app.quasar.qgl.render.CameraApiActual
import app.quasar.qgl.render.DrawableApiActual
import app.quasar.qgl.render.ProjectionApiActual
import app.quasar.qgl.engine.serialize.ScriptFactory
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
    private val runtime: CommonRuntime,
    private val tileSize: Int,
    private val window: GameWindow,
    private val scriptFactory: ScriptFactory,
): Disposable {
    private val spriteBatch = SpriteBatch()
    private val texture = Texture(textureFile)

    private lateinit var engine: QuasarEngineActual

    fun <T: GameWorld> createWorld(kClass: KClass<T>) {
        engine = QuasarEngineActual {
            drawable = DrawableApiActual(createTileTextures(texture, tileset, tileSize), spriteBatch)
            camera = CameraApiActual(window)
            project = ProjectionApiActual(window.getWorldCamera())
            scripts = scriptFactory
        }

        kClass.createInstance().apply {
            create(engine)
        }
        runtime.notifyWorld(engine)
    }

    fun loadWorld(filename: String) {
        val engineData = EngineDeserialize(filename, scriptFactory)

        engine = QuasarEngineActual {
            drawable = DrawableApiActual(createTileTextures(texture, tileset, tileSize), spriteBatch)
            camera = CameraApiActual(window)
            project = ProjectionApiActual(window.getWorldCamera())
            accounting = engineData.accounting
            nodeGraph = engineData.nodeGraph
            scripts = scriptFactory
        }
        //Simulate 1 frame after reloading to ensure camera are updated
        engine.simulate(Gdx.graphics.deltaTime)
        runtime.notifyWorld(engine)
    }

    fun render() {
        window.getWorldViewport().apply()
        window.getWorldCamera().update()
        spriteBatch.projectionMatrix = window.getWorldCamera().combined
        spriteBatch.begin()
        engine.draw()
        engine.simulate(Gdx.graphics.deltaTime)
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