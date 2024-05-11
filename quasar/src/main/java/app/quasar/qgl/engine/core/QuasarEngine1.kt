package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay1
import kotlin.reflect.KClass

//The full engine api, ADMIN api is for functions that should not be exposes to the public
interface QuasarEngine1: EngineApi1 {
    val registerOverlay: (GameOverlay1) -> Unit

    fun notifyNodeChanged()

    fun simulate(deltaTime: Float)
    //TODO: think about renaming this to `drawWorld`
    fun draw()
    fun drawOverlay()
    fun drawShapes(context: ShapeContext1)
    fun exit()

    fun <T: GameNode1<*>> createStartScripts(scripts: List<KClass<T>>)
    fun destroyNode(node: GameNode1<*>)
}