package app.quasar.gdx.game.logic

import app.quasar.gdx.game.scripts.WorldTimeData

fun doWorldTime(
    deltaTime: Float,
    data: WorldTimeData
) {
    data.gameTime.addSeconds((deltaTime * data.gameSpeed).toInt())
}