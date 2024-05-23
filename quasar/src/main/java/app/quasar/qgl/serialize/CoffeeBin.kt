package app.quasar.qgl.serialize

import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class CoffeeBin {

    inner class Out(private val dataOut: QGLBinary.Out) {

        fun write(value: Any) {
            writeClassType(value)
            dataOut.writeFrame(ID_OBJECT_PROPERTIES) {
                writeProperties(value)
            }
        }

        private fun writeClassType(value: Any) {
            val classId = value::class.annotations.filterIsInstance<QGLEntity>().first().typeId
            dataOut.writeString(ID_CLASS_ID, classId)
        }

        private fun writeProperties(value: Any) {
            value::class.declaredMemberProperties.forEach { member ->
                val prop = member as KProperty1<Any, Any>
                val propAnnotation = prop.annotations.filterIsInstance<BinProp>().firstOrNull()

                if(propAnnotation != null) {
                    val propValue = prop.get(value)
                    val id = propAnnotation.typeId
                    dataOut.writeAny(id, propValue)
                }
            }
        }

    }

    inner class In(
        private val kClassMap: KClassMap,
        private val dataIn: QGLBinary.In
) {

        fun read(): Any {
            val output = BinaryOutput()

            dataIn.read(output)
            val nextObject = newClassInstance(output.data as String)

            dataIn.readFrame { propOut ->
                setObjectProp(nextObject, propOut)
            }

            return nextObject
        }

        private fun newClassInstance(classId: String): Any {
            val classDef = kClassMap.findById(classId)
            return classDef?.createInstance()!!
        }

        private fun setObjectProp(theObject: Any, propOut: BinaryOutput) {
            val propertyId = propOut.id

            val settableProperty = theObject::class.declaredMemberProperties.find {  kProp ->
                /* 1. For each property, filter out the BinProp
                   2. find if the list has any BinProp that matches the id */
                val propAnno = kProp.annotations.filterIsInstance<BinProp>()
                propAnno.any { it.typeId == propertyId }
            }

            settableProperty?.let {
                it.isAccessible = true
                it.javaField?.set(theObject, propOut.data)
            }
        }
    }


    companion object {
        private const val ID_OBJECT = 1
        private const val ID_OBJECT_PROPERTIES = 1
        private const val ID_CLASS_ID = 2
    }
}