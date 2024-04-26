package app.quasar.gdx.tools

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

fun ShapeRenderer.canvas(type: ShapeType, callback: ShapeRenderer.() -> Unit) {
    begin(type)
    callback(this)
    end()
}