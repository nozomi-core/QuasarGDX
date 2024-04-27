package app.quasar.qgl.engine.core

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.reflect.KClass

//The full engine api, ADMIN api is for functions that should not be exposes to the public
interface QuasarEngine: EngineApi {
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