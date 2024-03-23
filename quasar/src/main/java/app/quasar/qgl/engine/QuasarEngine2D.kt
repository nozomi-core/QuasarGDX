package app.quasar.qgl.engine

import app.quasar.qgl.render.DrawApi
import app.quasar.qgl.tiles.*
import com.badlogic.gdx.graphics.Texture

class QuasarEngine2D(
    private val config: QuasarCallbacks) {

    private val drawApi = DrawApi(
        config.getTileLayout(),
        createTileTextures(
            config.getGameTileset(),
            config.getTileLayout(),
            config.getTexture()
        ),
        config.getSpriteBatch()
    )

    private var world: GameMap? = null

    fun setWorld(map: GameMap) {
        map.onCreate()
        world = map
    }

    fun render() {
        val spriteBatch = config.getSpriteBatch()
        spriteBatch.projectionMatrix = config.getCamera().combined
        spriteBatch.begin()
        world?.onDraw(drawApi)
        spriteBatch.end()
    }
}


private fun createTileTextures(tileset: GameTileset, tileSheetLayout: TileSheetLayout, texture: Texture): TileTextures {
    return TilesetBuilder(tileSheetLayout, texture).also { builder ->
        tileset.onCreateTiles(builder)
    }.build()
}