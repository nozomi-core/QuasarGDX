package app.quasar.qgl.engine.core

class NodeRecord<D>(
    var nodeId: Long,
    var tag: String,
    var data:D,
    var dimension: EngineDimension
)