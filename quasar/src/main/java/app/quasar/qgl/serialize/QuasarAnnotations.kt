package app.quasar.qgl.serialize

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class BinProp(val id: Int)

@Retention(AnnotationRetention.RUNTIME)
annotation class QGLEntity(val type: String)
