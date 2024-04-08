package app.quasar.qgl.engine

import app.quasar.qgl.serialize.*

class EngineBinary {

    class Out(
        private val definitions: QGLDefinitions,
        private val qglOut: QGLBinary.Out) {

        fun save(engineData: EngineDeserialized) {

            engineData.graph.nodes.forEach { node ->
                qglOut.writeFrame(ID_FRAME_NODE)
                qglOut.writeInt(ID_NODE_CLASS_ID, qglGetBinaryClassId(node::class))

                qglOut.writeLong(ID_NODE_RUNTIME_ID, node.runtimeId)
                qglOut.writeLong(ID_PARENT_NODE_ID, node.parentNodeId ?: NULL_PARENT_ID)

                //Serialize Data
                val nodeData = node.requireDataForInterface

                if(nodeData != null) {
                    val mapper = definitions.findMapperForClass(nodeData::class) as QGLMapper<Any>
                    val binaryNode = mapper.toBinary(nodeData)
                    val nodeClassId =  qglGetBinaryClassId(nodeData::class)

                    val binaryObject = BinaryObject(
                        classId = nodeClassId,
                        matrix = binaryNode
                    )

                    qglOut.writeObject(ID_NODE_DATA, binaryObject)
                }
            }

            qglOut.close()
        }
    }

    companion object {
        const val NULL_PARENT_ID = -1L

        const val ID_FRAME_NODE =           1
        const val ID_NODE_CLASS_ID =        2
        const val ID_NODE_RUNTIME_ID =      3
        const val ID_PARENT_NODE_ID =       4
        const val ID_NODE_DATA =            5
        const val ID_REFERENCE_MATRIX =     6 // BinaryObject([reference ids[], nodeIds[] ]) <----- how to recreate the n references inside the node
    }
}