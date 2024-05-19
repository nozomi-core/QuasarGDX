package app.quasar.qgl.engine.core

private val spriteMap = HashMap<String, SpriteId>()

class SpriteId(val id: String) {

    init {
        spriteMap[id] = this
    }

    companion object {
        fun find(id: String): SpriteId? = spriteMap[id]
    }
}