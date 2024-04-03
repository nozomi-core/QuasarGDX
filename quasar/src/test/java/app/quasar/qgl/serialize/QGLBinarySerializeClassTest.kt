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

        val qglSerialize = QGLSerialize(define)

        val bin = qglSerialize.write(tim)
        val kObject = qglSerialize.read(bin) as MyCustomer
        Assert.assertEquals( "Timmy", kObject.name)
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
        TODO("Not yet implemented")
    }

    override fun toEntity(binaryObject: BinaryObject): MyDog {
        TODO("Not yet implemented")
    }
}