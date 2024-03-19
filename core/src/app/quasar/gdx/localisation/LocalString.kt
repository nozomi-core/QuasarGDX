package app.quasar.gdx.localisation

//TODO: see LocalisationTest for ideas how nice way to implement string resources
enum class LocalString {
    welcome_to_center,
    entry_level_naming;

    override fun toString(): String {
        TODO("using id, load from resources file, store in memory for cache")
    }
}