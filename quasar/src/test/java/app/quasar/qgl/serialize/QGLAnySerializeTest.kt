package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

class KlassMap(klasses: List<KClass<*>>) {

    private val classMap = HashMap<String, KClass<*>>()

    init {
        klasses.forEach { aClass ->
            val classDef = aClass.findAnnotation<QGLEntity>() ?: throw Exception("KlassMap classes require @QGLEntity annotation for mapping")
            classMap[classDef.type] = aClass
        }
    }

    fun findById(id: String): KClass<*>? = classMap[id]
}


class QGLAnySerializeTest {

    @Test
    fun notQglEntity() {
        var didThrow = false
        try {
            KlassMap(listOf(NotSerial::class))
        }catch (e: Exception) {
            didThrow = true
        }
        Assert.assertEquals(true, didThrow)
    }

    @Test
    fun testSerializeVector() {
        KlassMap(listOf(NotSerial::class))


        val myVec = MyVector()
        myVec.age = 18

        convertToBinary(myVec) { id, data ->

        }
    }

    private fun convertToBinary(data: Any, serialise: (Int, Any) -> Unit) {
        data::class.declaredMemberProperties.forEach { member ->
            val prop = member as KProperty1<Any, Any>
            val propAnnotation = prop.annotations.filterIsInstance<BinProp>().firstOrNull()

            if(propAnnotation != null) {
                val value = prop.get(data)
                val id = propAnnotation.id

                serialise(id, value)
            }
        }
    }
}

@QGLEntity("my_vector")
class MyVector {

    @BinProp(id = 0)        var name = ""
    @BinProp(id = 1)        var age: Int = 99

    var notSerial: Long = System.currentTimeMillis()
}

class NotSerial