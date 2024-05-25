package app.quasar.qgl.serialize

import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.serialize.*
import app.quasar.qgl.fixtures.EmptyEngine
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream

private object TestScripts: ScriptFactory {
    override val scripts: ScriptCallback = {
        add(BasicScript::class)
    }
}

private object DataScripts: DataFactory {
    override val data: DataCallback = {
        add(BasicData::class)
    }
}

class EngineSerializeTest {

    @Test
    fun testEngineSerialize() {

        val engine = EmptyEngine.create()

        engine.createNode(engine.current, BasicScript::class)
        engine.simulate(0f)

        val byteOut = ByteArrayOutputStream()

        val output = QGLBinary().Out(BinaryDataWriter(byteOut))

        EngineSerialize(engine) {
            output
        }

        EngineDeserialize(ClassFactory(TestScripts, DataScripts)) {
            QGLBinary().In(DataInputStream(ByteArrayInputStream(byteOut.toByteArray())))
        }

    }
}

@QGLEntity("basic_script")
class BasicScript: GameNode<BasicData>() {
    override fun onCreate(argument: NodeArgument): BasicData {
        return BasicData().apply {
            gameName = "MyGame"
        }
    }
}


@QGLEntity("basic_data")
class BasicData {
    @BinProp(0)       var gameName: String = ""
}