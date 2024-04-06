package app.quasar.qgl.localise

import org.junit.Assert
import org.junit.Test

/* Should be ok to store whole file in memory, this is only used
* for application strings and not gameplay dialog */
enum class StringName {
    welcome_home;

    //Will get the string from cache or load from disk if it's not in memory
    override fun toString(): String {
        return "This is a welcome to home text"
    }
}

class LocalisationTest {
    @Test
    fun testEnumStyle() {
        Assert.assertEquals("This is a welcome to home text", StringName.welcome_home.toString())
    }
}