package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.language.serialize.*
import app.quasar.qgl.serialize.*
import app.quasar.qgl.test.fixtures.TestEmptyDrawableApi
import org.junit.Test
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

class QuasarEngineTestSerialize {

    @Test
    fun testEngineSerialization() {
        var engineData: EngineDeserialized? = null

        val quasarEngine = QuasarEngineActual(
            drawableApi = TestEmptyDrawableApi(),
            onExit = {
                engineData = it
            }
        )

        quasarEngine.createGameNode(SerialScript::class)
        quasarEngine.exit()
        quasarEngine.simulate(10f)


        val definitions = QGLDefinitions.Builder().apply {
            addClass(SerialData::class, SerialMapper())
        }.build()

        val qglOut = QGLBinary.Out(DataOutputStream(FileOutputStream(File("quasar_binary.dat"))))

        val engineOut = EngineBinary.Out(definitions, qglOut)
        engineOut.save(engineData!!)

    }
}

@QGLClass(1)
class SerialScript: GameNode<SerialData, Unit>() {
    override fun onCreate(argument: Unit?): SerialData {
        return SerialData(
            title = "BinaryData",
            count = 88
        )
    }
}

@QGLClass(2)
data class SerialData(
    val title: String,
    val count: Int
)

class SerialMapper: QGLMapper<SerialData> {
    override fun toBinary(data: SerialData): Array<BinaryRecord> {
        return arrayOf(
            BinaryRecord(ID_TITLE, data.title),
            BinaryRecord(ID_COUNT, data.count)
        )
    }

    override fun toEntity(bin: BinaryObject): SerialData {
        return SerialData(
            title = bin.value(ID_TITLE),
            count = bin.value(ID_COUNT)
        )
    }

    companion object {
        const val ID_TITLE = 0
        const val ID_COUNT = 1
    }
}