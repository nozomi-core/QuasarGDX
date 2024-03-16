import java.io.File
import java.util.*
import kotlin.random.Random

data class Customer(
    val customerId: Int = 1,
    val name: String,
    val age: Int
)

class Example {

    fun doWrite() {
        val customers = mutableListOf<Customer>()

        val random = Random(System.currentTimeMillis())

        for(i in 1.. 100) {
            val name = UUID.randomUUID().toString()
            val age = random.nextInt(99)

            customers.add(Customer(4, name, age))
        }


        val writer = BinaryFile.createFileOut(File("customers.dat")) { writer ->
            customers.forEach { customer ->
                writer.writeInt(1, customer.age)
                writer.writeString(2, customer.name)
            }
        }
    }

    fun doRead() {


        val record = BinaryOutput()
        val input = BinaryFile.createFileIn(File("customers.dat"))

        while(input.read(record)) {
            println("data-> ${record.data}")
        }



    }

    fun runThis() {
       doRead()
    }
}