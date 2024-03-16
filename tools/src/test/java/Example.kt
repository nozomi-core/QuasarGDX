import app.quasar.gdx.tools.BinaryUtils
import org.junit.Assert
import org.junit.Test
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.*

class ExampleTest {

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