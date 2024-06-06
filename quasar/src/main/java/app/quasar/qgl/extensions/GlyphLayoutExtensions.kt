package app.quasar.qgl.extensions

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.utils.Align

fun GlyphLayout.setTextBounds(font: BitmapFont, str: String, c: Color, targetWidth: Float, targetHeight: Float) {
    var useString = str
    var length = str.length

    setText(font, useString, c, targetWidth, Align.left, true)

    while(this.height>= targetHeight) {
        length /= 2
        useString = str.substring(0, length - 1)
        setText(font, useString, c, targetWidth, Align.left, true)
    }
}