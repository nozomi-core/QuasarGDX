package app.quasar.qgl.engine.core

import app.quasar.qgl.engine.core.interfaces.GameOverlay
import kotlin.reflect.KClass

//The full engine api, ADMIN api is for functions that should not be exposes to the public
interface QuasarEngine: EngineApi {
    val registerOverlay: (GameOverlay) -> Unit

    fun notifyNodeChanged()

    fun simulate(deltaTime: Float)
    //TODO: think about renaming this to `drawWorld`
    fun draw()
    fun drawOverlay()
    fun drawShapes(context: ShapeContext)
    fun exit()

    fun <T: GameNode<*>> createStartScripts(scripts: List<KClass<T>>)
    fun destroyNode(node: GameNode<*>)
}