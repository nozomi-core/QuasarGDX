package app.quasar.qgl.engine.core1

import org.junit.Assert
import org.junit.Test

class QuasarEngineTestPattern {

    @Test
    fun testCreateAndFindNode() {
        val engine = createEngine()
        val tag = "example-script"

        engine.createNode(ExampleScript::class) { factory ->
            factory.tag = tag
        }
        val exampleNode = engine.findByTag(tag)
        Assert.assertNotNull(exampleNode)
    }
}

class 