package app.quasar.qgl.engine.core1

import app.quasar.qgl.engine.core1.interfaces.GameOverlay1
import app.quasar.qgl.engine.core1.interfaces.WorldPosition1
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class QuasarEngineActual1(
    deserialized: EngineDeserialized1?,
    private val drawContext: DrawContext1,
    private val frameworkScripts: List<KClass<*>>,
    private val onExit: (EngineDeserialized1) -> Unit,
): QuasarEngine1, NodeSearchable1 {
    private val data = deserialized?.toEngineData() ?: EngineData1.createDefault()

    //Engine accounting variables
    private val destructionQueue = mutableListOf<GameNode1<*>>()
    private val creationQueue = mutableListOf<Pair<KClass<out GameNode1<*>>, Any?>>()

    private var isRunning = true
    private var engineMarkedToExit = false

    private val drawableNodes = DrawableNodeGraph1(this.data.graph)
    private val overlayNodes = DrawableOverlayGraph1()

    override val registerOverlay: (GameOverlay1) -> Unit = {
        overlayNodes.add(it)
    }

    //Contexts
    private val simContext = SimContext1(
        engine = this,
        clock = this.data.clock
    )

    /** Interface :: (QuasarEngine) */
    override fun notifyNodeChanged() {
        drawableNodes.notifyNodeChanged()
    }

    override fun simulate(deltaTime: Float) {
        data.clock.frameDeltaTime = deltaTime
        data.clock.tick()


        if(!isRunning) {
            return
        }

        doDestructionStep()
        doCreationStep()
        doSimulationStep()

        if(engineMarkedToExit && isRunning) {
            doExit()
        }
    }

    override fun draw() {
        drawableNodes.draw(drawContext)
    }

    override fun drawOverlay() {
        overlayNodes.draw(drawContext)
    }

    override fun drawShapes(context: ShapeContext1) {
        overlayNodes.drawShapes(context)
    }

    override fun exit() {
        engineMarkedToExit = true
    }

    override fun destroyNode(node: GameNode1<*>) {
        checkNodeIsCoreScriptThenThrow(node)
        destructionQueue.add(node)
    }

    private fun doExit() {
        isRunning = false
        onExit(
            EngineDeserialized1(
                currentRuntimeId = data.currentRuntimeId,
                coreScripts = data.coreScripts,
                graph = data.graph
            )
        )
    }

    //:: Engine steps
    private fun doDestructionStep() {
        destructionQueue.forEach {
            data.graph.remove(it)
        }
        destructionQueue.clear()
    }

    private fun doCreationStep() {
        creationQueue.forEach { createNode ->
            val (kClass, argument) = createNode

            val newEntity = kClass.createInstance()
            newEntity.create(this, argument)
            data.graph.add(newEntity)
        }
        creationQueue.clear()
    }

    private fun doSimulationStep() {
        data.graph.forEach {
            it.simulate(simContext)
        }
    }

    override fun generateId() = data.currentRuntimeId++

    override fun <T : GameNode1<*>> createNode(node: KClass<T>, argument: Any?) {
        checkNodeIsCoreScriptThenThrow(node)
        creationQueue.add(Pair(node, argument))
    }

    override fun <T : GameNode1<*>> createSingleNode(node: KClass<T>, argument: Any?) {
        if(!data.graph.contains(node)) {
            createSingleNode(node, argument)
        }
    }

    override fun <T : GameNode1<*>> createStartScripts(scripts: List<KClass<T>>) {
        //Add quasars own root scripts that will be spawned alongside the game scripts by developer
        val mergeAllScripts = mutableListOf<KClass<*>>().apply {
            addAll(frameworkScripts)
            addAll(scripts)
        }.toList()

        checkUniqueCoreScripts(mergeAllScripts)
        mergeAllScripts.filterIsInstance<KClass<T>>().forEach {
            createNode(it)
        }
        doCreationStep()
        checkScriptOrderIntegrity()

        data.coreScripts = mutableListOf()
        data.coreScripts.addAll(mergeAllScripts)

        //If node is a CORE node, we can run data after the initial core is created
        data.graph.forEach {
            if (it is CoreNode1) {
                it.onCoreCreated()
            }
        }
    }
    //Interface :: (NodeSearchable)

    override fun <T : Any> requireFindByInterface(nodeInterface: KClass<T>): T {
        return data.graph.requireFindByInterface(nodeInterface)
    }

    override fun <T : Any> findById(id: Long, nodeInterface: KClass<T>): T? {
        return data.graph.findById(id, nodeInterface)
    }

    override fun <T : Any> forEachInterface(nodeInterface: KClass<T>, callback: (T) -> Unit) {
        data.graph.forEachInterface(nodeInterface, callback)
    }

    override fun <T : Any> getNearby(target: WorldPosition1, distance: Float, nodeInterface: KClass<T>): List<T> {
        return data.graph.getNearby(target, distance, nodeInterface)
    }

    /** Utility and Helper Methods */

    private fun checkScriptOrderIntegrity() {
        data.graph.forEach { currentNode ->
            if(currentNode is CoreNode1) {
                val shouldBeBefore = currentNode.getShouldRunBefore()
                val thisNodeIndex = data.graph.indexOf(currentNode)
                shouldBeBefore.forEach { dependClass ->
                    val dependIndex = data.graph.indexOf(data.graph.first { it::class == dependClass })
                    if(dependIndex > thisNodeIndex) {
                        throw SecurityException("${dependClass.simpleName} should be spawned before ${currentNode::class.simpleName}")
                    }
                }
            }
        }
    }

    private fun checkNodeIsCoreScriptThenThrow(node: GameNode1<*>) {
        checkNodeIsCoreScriptThenThrow(node::class)
    }

    private fun checkNodeIsCoreScriptThenThrow(script: KClass<*>) {
        if(data.coreScripts.contains(script)) {
            throw SecurityException("Root scripts are immutable, you can not add or remove them")
        }
    }

    private fun checkUniqueCoreScripts(scripts: List<KClass<*>>) {
        val checkDuplicates = HashSet<KClass<*>>()
        scripts.forEach {
            if(checkDuplicates.contains(it)) {
                throw IllegalArgumentException("Can not have duplicate classes in root scripts, only 1 instance shall be spawned")
            }
            checkDuplicates.add(it)
        }
    }
}
