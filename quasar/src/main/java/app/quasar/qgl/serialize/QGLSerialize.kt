package app.quasar.qgl.serialize

import kotlin.reflect.KClass

class QGLSerialize(
    private val definitions: QGLDefinitions
) {
    fun write(kObject: Any): BinaryObject {
        checkObjectIsSerializable(kObject)

        val classId = kObject::class.java.getAnnotation(QGLClass::class.java).classId
        val serialMapper = definitions.findMapperForId(classId) as QGLMapper<Any>

        return BinaryObject(
            classId = classId,
            matrix = serialMapper.toBinary(kObject)
        )
    }

    fun read(binObject: BinaryObject): Any {
        val serialMapper = definitions.findMapperForId(binObject.classId)
        return serialMapper.toEntity(binObject)!!
    }
}

class QGLDefinitions(private val serialMap: Map<Int, Pair<KClass<*>, QGLMapper<*>>>) {

    class Builder {
        private val classMap = HashMap<Int, Pair<KClass<*>, QGLMapper<*>>>()

        fun <T : Any> addClass(kClass: KClass<T>, mapper: QGLMapper<T>): Builder {
            checkClassIsSerializable(kClass)

            val classId = qglGetBinaryClassId(kClass)

            if(classMap.containsKey(classId)) {
                throw IllegalArgumentException("QGLClass ID's must be unique, please store them in single file to keep track")
            }

            classMap[classId] = Pair(kClass, mapper)

            return this
        }

        fun build(): QGLDefinitions {
            return QGLDefinitions(classMap)
        }
    }

    fun findMapperForClass(kClass: KClass<*>): QGLMapper<*> {
        return serialMap[qglGetBinaryClassId(kClass)]!!.second
    }

    fun findMapperForId(id: Int): QGLMapper<*> {
        return serialMap[id]!!.second
    }
}

fun qglGetBinaryClassId(kClass: KClass<*>) = kClass.java.getAnnotation(QGLClass::class.java).classId


fun checkClassIsSerializable(kClass: KClass<*>) {
    val hasAnnotation = kClass.java.isAnnotationPresent(QGLClass::class.java)
    if(!hasAnnotation) {
        throw IllegalArgumentException("All QGLClasses must have an id to represent that class")
    }
}

fun checkObjectIsSerializable(kObject: Any) {
    checkClassIsSerializable(kObject::class)
}

interface QGLMapper<T> {
    fun toBinary(data: T): Array<BinaryRecord>
    fun toEntity(bin: BinaryObject): T
}

@Retention(AnnotationRetention.RUNTIME)
annotation class QGLClass(val classId: Int)