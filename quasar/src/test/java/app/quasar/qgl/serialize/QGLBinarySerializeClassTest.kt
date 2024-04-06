package app.quasar.qgl.serialize

import org.junit.Assert
import org.junit.Test

class QGLBinarySerializeClassTest {

    @Test
    fun testSerializeClass() {
        val define = QGLDefinitions.Builder()
            .addClass(MyCustomer::class, MyCustomerMapper())
            .build()

        val tim = MyCustomer("Timmy", 26, true)

        val qglSerialize = QGLSerialize(
            definitions = define
        )
        val out = QGLBinary.createMemoryOut { binStream ->
            val bin = qglSerialize.write(tim)
            binStream.writeObject(bin.classId, bin)
            val kObject = qglSerialize.read(bin) as MyCustomer
            Assert.assertEquals( "Timmy", kObject.name)
        }

        val readSerial = QGLBinary.createMemoryIn(out)
        val output = BinaryOutput()
        readSerial.read(output)

        val theObject = qglSerialize.read(output.data as BinaryObject)
        Assert.assertEquals(MyCustomer::class, theObject::class)
    }

    @Test
    fun testSerializeMultiple() {
        val define = QGLDefinitions.Builder()
            .addClass(MyCustomer::class, MyCustomerMapper())
            .addClass(MyDog::class, MyDogMapper())
            .build()

        val tim = MyCustomer("Timmy", 26, true)
        val dog = MyDog("Milo", 45, true)

        val serialize = QGLSerialize(
            definitions = define
        )

        val objects = listOf(tim, dog)

        val out = QGLBinary.createMemoryOut { binStream ->
            binStream.writeInt(999, objects.size)

            objects.forEach { kObject ->
                val bin = serialize.write(kObject)
                binStream.writeObject(0, bin)
            }
        }

        val readSerial = QGLBinary.createMemoryIn(out)
        val output = BinaryOutput()
        readSerial.read(output)

        val size = output.data as Int

        val readableObjects = mutableListOf<Any>()

        for(i in 0 until size) {
            readSerial.read(output)
            val binObject = output.data as BinaryObject
            readableObjects.add(serialize.read(binObject))
        }

        Assert.assertEquals(2, readableObjects.size)
        Assert.assertEquals(MyCustomer::class, readableObjects[0]::class)
        Assert.assertEquals(MyDog::class, readableObjects[1]::class)
    }
}


@QGLClass(classId = 1)
data class MyCustomer(
    val name: String,
    val age: Int,
    val isActive: Boolean
)

@QGLClass(classId = 2)
data class MyDog(
    val petName: String,
    val age: Int,
    val isRunning: Boolean
)


class MyCustomerMapper: QGLMapper<MyCustomer> {

    override fun toBinary(data: MyCustomer): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_NAME, data.name),
            BinaryRecord(ID_AGE, data.age),
            BinaryRecord(ID_IS_ACTIVE, data.isActive)
        )
    }

    override fun toEntity(bin: BinaryObject): MyCustomer {
        return MyCustomer(
            name = bin.value(ID_NAME),
            age = bin.value(ID_AGE),
            isActive = bin.value(ID_IS_ACTIVE)
        )
    }

    companion object {
        const val ID_NAME = 1
        const val ID_AGE = 2
        const val ID_IS_ACTIVE = 3
    }
}

class MyDogMapper: QGLMapper<MyDog> {
    override fun toBinary(data: MyDog): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_PET_NAME, data.petName),
            BinaryRecord(ID_AGE, data.age),
            BinaryRecord(ID_IS_RUNNING, data.isRunning)
        )
    }

    override fun toEntity(bin: BinaryObject): MyDog {
        return MyDog(
            petName = bin.value(ID_PET_NAME),
            age = bin.value(ID_AGE),
            isRunning = bin.value(ID_IS_RUNNING)
        )
    }

    companion object {
        const val ID_PET_NAME = 1
        const val ID_AGE = 2
        const val ID_IS_RUNNING = 3
    }
}