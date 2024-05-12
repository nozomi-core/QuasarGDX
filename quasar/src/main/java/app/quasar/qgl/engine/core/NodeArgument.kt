package app.quasar.qgl.engine.core

interface NodeArgument

object NullArgument: NodeArgument

data class AnyNodeArgument(val value: Any): NodeArgument