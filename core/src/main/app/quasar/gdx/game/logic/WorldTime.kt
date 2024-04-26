package app.quasar.gdx.game.logic

import app.quasar.gdx.game.data.WorldTimeData
import app.quasar.gdx.game.scripts.WorldTimeScript
import app.quasar.qgl.engine.core.SimContext
import app.quasar.qgl.scripts.ConsoleLog

fun doWorldTime(
    script: WorldTimeScript,
    context: SimContext,
    data: WorldTimeData
) {
    val deltaTime = context.clock.deltaTime

    data.gameTime.addSeconds((deltaTime * data.gameSpeed).toInt())
}