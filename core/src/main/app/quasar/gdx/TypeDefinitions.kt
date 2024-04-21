package app.quasar.gdx

import app.quasar.gdx.game.scripts.WorldTimeData
import app.quasar.gdx.game.scripts.WorldTimeMapper
import app.quasar.qgl.serialize.QGLDefinitions

fun QGLDefinitions.Builder.buildApplicationTypes() {
    addClass(WorldTimeData::class, WorldTimeMapper())
}