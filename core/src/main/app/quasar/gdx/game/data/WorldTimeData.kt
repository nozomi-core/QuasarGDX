package app.quasar.gdx.game.data

import app.quasar.gdx.game.scripts.WorldTimeInput
import app.quasar.qgl.serialize.BinaryObject
import app.quasar.qgl.serialize.BinaryRecord
import app.quasar.qgl.serialize.QGLMapper
import org.joda.time.MutableDateTime

data class WorldTimeData(
    val gameTime: MutableDateTime,
    var gameSpeed: Float = WorldTimeInput.DEFAULT_SPEED
)

class WorldTimeMapper: QGLMapper<WorldTimeData> {
    override fun toBinary(data: WorldTimeData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_GAME_TIME, data.gameTime.millis),
            BinaryRecord(ID_GAME_SPEED, data.gameSpeed)
        )
    }

    override fun toEntity(bin: BinaryObject): WorldTimeData {
        return WorldTimeData(
            gameTime = MutableDateTime(bin.value<Long>(ID_GAME_TIME)),
            gameSpeed = bin.value(ID_GAME_SPEED)
        )
    }

    companion object {
        const val ID_GAME_TIME = 0
        const val ID_GAME_SPEED = 1
    }
}