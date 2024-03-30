package app.quasar.gdx.game.scripts

import app.quasar.gdx.game.model.GameTime
import app.quasar.gdx.game.model.GameTimeAdmin
import app.quasar.qgl.engine.EngineApi
import app.quasar.qgl.entity.RootNode
import app.quasar.qgl.scripts.EngineLogger

interface WorldTime {
    val gameTime: GameTime
}

class WorldTimeScript: RootNode(), WorldTime {
    private val _gameTime = GameTimeAdmin()

    override val gameTime: GameTime get() = _gameTime

    private lateinit var logger: EngineLogger

    private var gameSpeed: Float = DEFAULT_SPEED

    private var lastHour = gameTime.hourOfDay

    override fun onCreate(engine: EngineApi, argument: Any?) {
        super.onCreate(engine, argument)
        gameSpeed = when(argument) {
            is Double -> argument.toFloat()
            is Float -> argument
            else -> DEFAULT_SPEED
        }

        logger = engineApi.requireFindByInterface(EngineLogger::class)
    }

    override fun onSimulate(deltaTime: Float) {
        super.onSimulate(deltaTime)
        _gameTime.addSeconds(deltaTime * gameSpeed)

        if(lastHour != gameTime.hourOfDay) {
            lastHour = gameTime.hourOfDay
            onHourChanged()
        }
    }

    private fun onHourChanged() {
        logger.message(this, _gameTime.toString())
    }

    companion object {
        const val DEFAULT_SPEED = 5000f
    }
}