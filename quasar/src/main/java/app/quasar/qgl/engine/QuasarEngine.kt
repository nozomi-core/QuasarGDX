package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode
import kotlin.reflect.KClass

//The full engine api, ADMIN api is for functions that should not be exposes to the public
interface QuasarEngine: EngineApi {
    fun simulate(deltaTime: Float)
    fun draw()

    fun <T: GameNode<*,*>> createRootScripts(scripts: List<KClass<T>>)
    fun destroyNode(node: GameNode<*,*>)
    fun setCurrentNodeRunning(node: GameNode<*,*>)
    fun checkNodeIsNotRunning(node: GameNode<*,*>)
}