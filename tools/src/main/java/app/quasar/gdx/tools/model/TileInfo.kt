package app.quasar.gdx.tools.model

import app.quasar.gdx.tiles.CoreTiles
import kotlin.random.Random

class TileInfo(
    val rowX: Int,
    val columnY: Int,
    val spriteId: String
)

fun createRandomTileInfo(rows: Int, columns: Int): List<TileInfo> {
    return mutableListOf<TileInfo>().apply {
        val random = Random(System.currentTimeMillis())

        for(rowX in 0 until  rows) {

            for (columnY in 0 until columns) {
                val rand = random.nextInt(10)

                if(rand < 2) {
                    add(TileInfo(rowX,columnY, CoreTiles.GREEN_LIGHT.id))
                }

            }
        }
    }
}