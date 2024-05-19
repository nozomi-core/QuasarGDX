package app.quasar.gdx.tools.model

class Grid(
    tileSize: Int,
    rows: Int,
    columns: Int,
    originX: Float,
    originY: Float

) {
    private val gridMap = HashMap<String, GridItem>()

    init {

        for(rowX in 0 until  rows) {
            val gridX = (tileSize * rowX).toFloat() + originX

            for (columnY in 0 until columns) {
                val gridY = (tileSize * columnY).toFloat() + originY

                gridMap[getKey(rowX, columnY)] = GridItem(gridX, gridY, rowX, columnY)
            }
        }
    }

    fun getLocation(rowX: Int, columnY: Int): GridItem? = gridMap[getKey(rowX, columnY)]

    fun forEach(callback:(GridItem) -> Unit) {
        gridMap.forEach { t, u ->
            callback(u)
        }
    }

    private fun getKey(rowX: Int, columnY: Int) = "$rowX,$columnY"
}