package integrations.app.quasar.qgl

import app.quasar.qgl._fixtures.TestDrawContext
import app.quasar.qgl.engine.serialize.EngineBinary1
import app.quasar.qgl.serialize.*
import app.quasar.qgl.engine.core.EngineDeserialized1
import app.quasar.qgl.engine.core.GameNode1
import app.quasar.qgl.engine.core.NodeInput1
import app.quasar.qgl.engine.core.QuasarEngineActual1
import org.junit.Assert
import org.junit.Test

class QuasarEngineTestSerialize {

    @Test
    fun testEngineSerialization() {
        var engineData: EngineDeserialized1? = null

        val quasarEngine = QuasarEngineActual1(
            drawContext = TestDrawContext.create(),
            onExit = {
                engineData = it
            },
            deserialized = null,
            frameworkScripts = listOf()
        )

        quasarEngine.createNode(SerialScript::class)
        quasarEngine.exit()
        quasarEngine.simulate(10f)


        val definitions = QGLDefinitions.Builder().apply {
            addClass(SerialData::class, SerialMapper())
        }.build()

        val inMemoryEngine = QGLBinary.createMemoryOut { memOut ->
            EngineBinary1.Out(definitions, memOut, engineData!!)
        }

        val engineIn = QGLBinary.createMemoryIn(inMemoryEngine)
        val engineInData = EngineBinary1.In(engineIn)

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
class SerialScript: GameNode1<SerialData>() {
    override fun onCreate(input: NodeInput1): SerialData {
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