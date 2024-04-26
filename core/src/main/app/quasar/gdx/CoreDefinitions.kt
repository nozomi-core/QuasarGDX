package app.quasar.gdx

import app.quasar.gdx.game.data.WorldTimeData
import app.quasar.gdx.game.data.WorldTimeMapper
import app.quasar.qgl.serialize.QGLDefinitions

fun QGLDefinitions.Builder.buildApplicationTypes() {
    addClass(WorldTimeData::class, WorldTimeMapper())
}