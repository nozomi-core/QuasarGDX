package app.quasar.qgl.serialize

class BinaryRecord internal constructor(
    val id: Int,
    val data: Any
) {
    constructor(id: Int, data: String): this(id, data as Any)
    constructor(id: Int, data: Int): this(id, data as Any)
    constructor(id: Int, data: Double): this(id, data as Any)
    constructor(id: Int, data: Long): this(id, data as Any)
    constructor(id: Int, data: Float): this(id, data as Any)
    constructor(id: Int, data: Boolean): this(id, data as Any)
}