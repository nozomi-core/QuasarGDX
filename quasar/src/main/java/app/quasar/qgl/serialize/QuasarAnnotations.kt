package app.quasar.qgl.serialize

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class BinProp(val typeId: Int)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class QGLEntity(val typeId: String, val classId: Int = 0)
