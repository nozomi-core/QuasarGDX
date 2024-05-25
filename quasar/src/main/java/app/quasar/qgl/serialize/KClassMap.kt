package app.quasar.qgl.serialize

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

class KClassMap(klasses: List<KClass<*>>) {

    private val classMap = HashMap<String, KClass<*>>()

    init {
        klasses.forEach { aClass ->
            val classDef = aClass.findAnnotation<QGLEntity>() ?: throw Exception("KlassMap classes require @QGLEntity annotation for mapping")
            classMap[classDef.typeId] = aClass
        }
    }

    fun findById(id: String): KClass<*>? = classMap[id]


    companion object {
        fun of(vararg klasses : KClass<*>): KClassMap {
            return KClassMap(mutableListOf<KClass<*>>().apply {
                klasses.forEach { add(it) }
            })
        }
    }
}