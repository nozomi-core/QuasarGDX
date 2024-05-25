package app.quasar.qgl.engine.core

class EngineDimension private constructor(val id: Int) {

    companion object {
        private val dimenMap = HashMap<Int, EngineDimension>()

        fun create(id: Int): EngineDimension {
            return dimenMap[id] ?: EngineDimension(id).also {
                dimenMap[id] = it
            }
        }
    }
}