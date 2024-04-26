package app.quasar.qgl.engine.serialize

import app.quasar.qgl.engine.core.GameNode
import app.quasar.qgl.engine.core.EngineDeserialized
import app.quasar.qgl.serialize.*

class EngineBinary {

    class Out(
        private val definitions: QGLDefinitions,
        private val qglOut: QGLBinary.Out,
        engineData: EngineDeserialized
    ) {
        init {
            save(engineData)
        }

        private fun save(engineData: EngineDeserialized) {
            qglOut.writeFrame(
                startId = ID_FRAME_METADATA_START,
                endId = ID_FRAME_METADATA_END
            ) {
                qglOut.writeLong(ID_LAST_RUNTIME_ID, engineData.currentRuntimeId)
                qglOut.writeIntArray(ID_ROOT_SCRIPTS, engineData.coreScripts.map { qglGetBinaryClassId(it) }.toIntArray())
            }

            //Serialise node data
            engineData.graph.forEach { node ->
                qglOut.writeFrame(
                    startId = ID_FRAME_NODE_START,
                    endId = ID_FRAME_NODE_END
                ) {
                    writeGameNode(node)
                    //TODO: write child nodes
                }
            }
        }

        private fun writeGameNode(node: GameNode<*>) {
            qglOut.writeInt(ID_NODE_CLASS_ID, qglGetBinaryClassId(node::class))

            qglOut.writeLong(ID_NODE_RUNTIME_ID, node.runtimeId)
            node.parentNodeId?.let { parentRuntimeId ->
                qglOut.writeLong(ID_PARENT_NODE_ID, parentRuntimeId)
            }

            //Serialize Data
            val nodeData = node.getDataForBinary()

            if(nodeData != null) {
                val mapper = definitions.findMapperForClass(nodeData::class) as QGLMapper<Any>
                val binaryNode = mapper.toBinary(nodeData)
                val nodeClassId = qglGetBinaryClassId(nodeData::class)

                val binaryObject = BinaryObject(
                    classId = nodeClassId,
                    matrix = binaryNode
                )

                qglOut.writeObject(ID_NODE_DATA, binaryObject)
            }
        }
    }

    class In(
        private val qglIn: QGLBinary.In
    ) {
        var engineMetaData: BinaryEngineMetaData? = null
            private set

        private val _engineNodes = mutableListOf<BinaryEngineNode>()
        val engineNodes: List<BinaryEngineNode> get() = _engineNodes

        init {
            load()
        }

        private fun load() {
            //Reads frame related data
            val output = BinaryOutput()

            while(qglIn.read(output)) {
                when(output.id) {
                    ID_FRAME_METADATA_START -> {
                        engineMetaData = readEngineMetadata(output)
                    }
                    ID_FRAME_NODE_START -> {
                        _engineNodes.add(readGameNode(output))
                    }
                    else -> throw Exception("Expected the next frameId in the binary format")
                }
            }
        }

        private fun readEngineMetadata(output: BinaryOutput): BinaryEngineMetaData {
            var lastRuntimeId: Long? = null
            var rootScriptIds: IntArray? = null

            while (qglIn.readUntil(ID_FRAME_METADATA_END, output)) {
                when(output.id) {
                    ID_LAST_RUNTIME_ID -> lastRuntimeId = output.data as Long
                    ID_ROOT_SCRIPTS -> rootScriptIds = output.data as IntArray
                }
            }

            return BinaryEngineMetaData(
                lastNodeRuntimeId = lastRuntimeId!!,
                rootScriptsClassIds = rootScriptIds ?: IntArray(0)
            )
        }

        private fun readGameNode(output: BinaryOutput): BinaryEngineNode {
            var nodeClassId: Int? = null
            var nodeParentRuntimeId: Long? = null
            var nodeRuntimeId: Long? = null
            var nodeData: BinaryObject? = null

            while (qglIn.readUntil(ID_FRAME_NODE_END, output)) {
                when(output.id) {
                    ID_NODE_CLASS_ID -> nodeClassId = output.data as Int
                    ID_PARENT_NODE_ID -> nodeParentRuntimeId = output.data as Long
                    ID_NODE_RUNTIME_ID -> nodeRuntimeId = output.data as Long
                    ID_NODE_DATA -> nodeData = output.data as BinaryObject
                }
            }

            return BinaryEngineNode(
                classId = nodeClassId!!,
                nodeRuntimeId = nodeRuntimeId!!,
                nodeParentRuntimeId = nodeParentRuntimeId,
                nodeData = nodeData
            )
        }
    }

    companion object {
        const val ID_FRAME_METADATA_START =     1
        const val ID_FRAME_METADATA_END =       2
        const val ID_FRAME_NODE_START =         3
        const val ID_FRAME_NODE_END =           4
        const val ID_LAST_RUNTIME_ID =          5
        const val ID_ROOT_SCRIPTS =             6
        const val ID_NODE_CLASS_ID =            7
        const val ID_NODE_RUNTIME_ID =          8
        const val ID_PARENT_NODE_ID =           9
        const val ID_NODE_DATA =                10
    }
}

class BinaryEngineNode(
    val classId: Int,
    val nodeRuntimeId: Long,
    val nodeParentRuntimeId: Long?,
    val nodeData: BinaryObject?
)

class BinaryEngineMetaData(
    val lastNodeRuntimeId: Long,
    val rootScriptsClassIds: IntArray
)