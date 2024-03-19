package app.quasar.qgl.localise

import org.junit.Assert
import org.junit.Test

/**
 * This tests the pattern to use enum classes to load localised string resources
 */
class StringLoader {
    fun findId(id: StringName): String = ""
}

/* Should be ok to store whole file in memory, this is only used
* for application strings and not gameplay dialog */
enum class StringName {
    welcome_home,
    my_age;

    //Will get the string from cache or load from disk if it's not in memory
    override fun toString(): String {
        return "This is a welcome to home text"
    }
}


class LocalisationTest {
    @Test
    fun testEnumStyle() {
        Assert.assertEquals("hey", StringName.welcome_home)
    }
}