import app.quasar.gdx.tools.BinaryFormat
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*


annotation class BinaryId(val name: Int)

@BinaryId(BinTypes.CUSTOMERS)
data class Customer(
    @BinaryId(BinTypes.CUSTOMERS_ID)
    val id: Int? = null,
    @BinaryId(BinTypes.CUSTOMERS_NAME)
    val name: String? = null,
    @BinaryId(BinTypes.CUSTOMERS_AGE)
    val age: Int? = null
)

class ExampleTest {

    @Before
    fun setupFormat() {
        BinaryFormat.extend(listOf(Customer::class)) { typeId ->
            when(typeId) {
                BinTypes.CUSTOMERS -> Customer::class
                else -> null
            }
        }
    }

    @Test
    fun aTest() {
        val write = BinaryFile.createFileOut(File("mydata.txt")) { writer ->
            for (i in 1..100) {
                val uuid = UUID.randomUUID().toString()

                writer.writeStringXOR(9, uuid);
            }
        }
    }

    @Test
    fun readTest() {
        val reader = BinaryFile.createFileIn(File("mydata.txt"))

        val output = BinaryOutput()

        while(reader.read(output)) {
            print(output.data)
        }
    }
}