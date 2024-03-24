package app.quasar.qgl.engine

import app.quasar.qgl.entity.GameNode

//The full engine api, ADMIN api is for functions that should not be exposes to the public
interface EngineApiAdmin: EngineApi {
    fun destroyNode(node: GameNode)
}