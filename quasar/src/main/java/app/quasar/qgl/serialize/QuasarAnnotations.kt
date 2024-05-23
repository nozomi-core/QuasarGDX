package app.quasar.qgl.serialize

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class BinProp(val typeId: Int)

@Retention(AnnotationRetention.RUNTIME)
annotation class QGLEntity(val typeId: String)
