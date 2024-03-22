package app.quasar.gdx.localisation

enum class LocalString {
    debug

    //Leave this colon here, easy to add new string enums
    ;
    override fun toString(): String = findLocalString(this)
}