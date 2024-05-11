package app.quasar.qgl.engine.core1

class OverlayScreen1(
    val width: Float,
    val height: Float
) {
    val leftEdge = -(width / 2)
    val rightEdge = width / 2
    val topEdge = height / 2
    val bottomEdge = -(height / 2)

    fun uiTransform(uiX: Float, uiY: Float, uiWidth: Float, uiHeight: Float, callback: (x: Float, y:Float, width:Float, height:Float) -> Unit) {
        callback(uiX, uiY, uiWidth, -uiHeight)
    }

}