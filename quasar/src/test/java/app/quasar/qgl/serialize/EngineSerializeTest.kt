package app.quasar.qgl.serialize

import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.NodeArgument
import app.quasar.qgl.engine.serialize.*
import app.quasar.qgl.fixtures.EmptyEngine
import com.badlogic.gdx.math.Vector3
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream

private object TestScripts: ScriptFactory {
    override val scripts: ScriptCallback = {
        add(BasicScript::class)
        add(BasicScriptVector::class)
    }
}

private object DataScripts: DataFactory {
    override val data: DataCallback = {
        add(BasicData::class)
        add(BasicVector::class)
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

    @Test
    fun testEngineSerializeWithVector() {

        val engine = EmptyEngine.create()

        engine.createNode(engine.current, BasicScriptVector::class)
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

@QGLEntity("basic_script")
class BasicScriptVector: GameNode<BasicVector>() {
    override fun onCreate(argument: NodeArgument): BasicVector {
        return BasicVector().apply {
            position.x = 0f
        }
    }
}


@QGLEntity("basic_data")
class BasicData {
    @BinProp(0)       var gameName: String = ""
}

@QGLEntity("basic_vector")
class BasicVector {
    @BinProp(0)       var position = Vector3(1f,2f,3f)
}