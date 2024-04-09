package app.quasar.gdx.game.logic

import app.quasar.gdx.game.scripts.WorldTimeData
import app.quasar.gdx.game.scripts.WorldTimeScript

fun doWorldTime(
    node: WorldTimeScript,
    deltaTime: Float,
    data: WorldTimeData
) {
    data.gameTime.addSeconds((deltaTime * data.gameSpeed).toInt())
}