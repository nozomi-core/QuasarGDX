package app.quasar.qgl.serialize

import app.quasar.qgl.serializers.VectorMapper
import com.badlogic.gdx.math.Vector3
import kotlin.reflect.KClass

//TODO: implement proper data mapping system
class BinaryMap {

    private val classMapper = HashMap<KClass<*>, QGLMapper<*>>()
    private val binaryMapper = HashMap<String, QGLMapper<*>>()

    init {
        classMapper[Vector3::class] = VectorMapper
        binaryMapper["vector"] = VectorMapper
    }

    fun toBinary(value: Any): Any {
        val mapper = classMapper[value::class] as? QGLMapper<Any>

        return mapper?.let {
            val classId = binaryMapper.filterValues { it == mapper }.keys.first()
            BinaryObject(classId, it.toBinary(value))
        } ?: value
    }

    fun toObject(bin: BinaryObject): Any {
        val mapper = binaryMapper[bin.classId]!!
        return mapper.toObject(bin) as Any
    }
}