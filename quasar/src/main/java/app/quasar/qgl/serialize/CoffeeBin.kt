package app.quasar.qgl.serialize

import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class CoffeeBin {

    inner class Out(private val dataOut: QGLBinary.Out) {

        fun write(list: List<Any>) {
            dataOut.writeInt(ID_SIZE, list.size)
            list.forEach {
                writeObjectRecord(it)
            }
        }

        private fun writeObjectRecord(value: Any) {
            val classEntity = writeClassType(value)
            writeObjectRecord(classEntity, value)
        }

        private fun writeClassType(value: Any): QGLEntity {
            val qglEntity = value::class.annotations.filterIsInstance<QGLEntity>().first()
            val classId = qglEntity.typeId
            dataOut.writeString(ID_CLASS_ID, classId)
            return qglEntity
        }

        private fun writeObjectRecord(entity: QGLEntity, value: Any) {
            val propList = mutableListOf<BinaryRecord>()

            value::class.declaredMemberProperties.forEach { member ->
                val prop = member as KProperty1<Any, Any>
                val propAnnotation = prop.annotations.filterIsInstance<BinProp>().firstOrNull()

                if(propAnnotation != null) {
                    val propValue = prop.get(value)
                    val idProp = propAnnotation.typeId

                    propList.add(BinaryRecord(id = idProp, data =  propValue))
                }
            }
            if(propList.isNotEmpty()) {
                dataOut.writeObject(ID_OBJECT, BinaryObject(entity.classId, propList.toTypedArray()))
            } else {
                dataOut.writeString(ID_EMPTY_OBJECT, "")
            }
        }
    }

    inner class In(
        private val kClassMap: KClassMap,
        private val dataIn: QGLBinary.In
) {
        fun read(): List<Any> {
            val list = mutableListOf<Any>()

            val output = BinaryOutput()
            dataIn.read(output)

            repeat(output.data as Int) {
                list.add(readObjectRecord())
            }

            return list
        }

        fun readObjectRecord(): Any {
            val output = BinaryOutput()

            dataIn.read(output)
            val nextClass = newClassInstance(output.data as String)

            dataIn.read(output)

            if(output.id == ID_OBJECT) {
                setObject(nextClass, output.data as BinaryObject)
            }

            return nextClass
        }

        private fun newClassInstance(classId: String): Any {
            val classDef = kClassMap.findById(classId)
            return classDef?.createInstance()!!
        }

        private fun setObject(theObject: Any, binObject: BinaryObject) {
            for(i in 0 until binObject.size) {
                val record = binObject[i]
                setObjectRecord(theObject, record)
            }
        }

        private fun setObjectRecord(theObject: Any, record: BinaryRecord) {
            val settableProperty = theObject::class.declaredMemberProperties.find { kProp ->
                /* 1. For each property, filter out the BinProp
                   2. find if the list has any BinProp that matches the id */
                val propAnno = kProp.annotations.filterIsInstance<BinProp>()
                propAnno.any { it.typeId == record.id    }
            }

            settableProperty?.let {
                it.isAccessible = true
                it.javaField?.set(theObject,record.data)
            }
        }
    }

    companion object {
        private const val ID_SIZE = 1
        private const val ID_OBJECT = 2
        private const val ID_CLASS_ID = 3
        private const val ID_EMPTY_OBJECT = 4
    }
}