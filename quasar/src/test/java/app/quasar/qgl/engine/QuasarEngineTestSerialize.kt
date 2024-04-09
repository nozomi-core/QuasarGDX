package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import app.quasar.qgl.serialize.*
import app.quasar.qgl.test.fixtures.TestEmptyDrawableApi
import org.junit.Assert
import org.junit.Test

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

        val inMemoryEngine = QGLBinary.createMemoryOut { memOut ->
            EngineBinary.Out(definitions, memOut, engineData!!)
        }

        val engineIn = QGLBinary.createMemoryIn(inMemoryEngine)
        val engineInData = EngineBinary.In(engineIn)

        Assert.assertEquals(1, engineInData.engineNodes.size)
        engineInData.engineNodes[0].let { node ->
            Assert.assertEquals(64,  node.classId)
            Assert.assertEquals(null, node.nodeParentRuntimeId)
            Assert.assertEquals(1, node.nodeRuntimeId)
            Assert.assertEquals(73, node.nodeData?.classId)
        }
        Assert.assertEquals(2L, engineInData.engineMetaData?.lastNodeRuntimeId)
        Assert.assertEquals(0, engineInData.engineMetaData?.rootScriptsClassIds?.size)

        val serialData = engineInData.engineNodes[0].nodeData!!
        val mapper = definitions.findMapperForId(serialData.classId)
        val convertData = mapper.toEntity(serialData) as SerialData

        Assert.assertEquals("BinaryData", convertData.title)
        Assert.assertEquals(88, convertData.count)
    }
}

@QGLClass(64)
class SerialScript: GameNode<SerialData, Unit>() {
    override fun onCreate(argument: Unit?): SerialData {
        return SerialData(
            title = "BinaryData",
            count = 88
        )
    }
}

@QGLClass(73)
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