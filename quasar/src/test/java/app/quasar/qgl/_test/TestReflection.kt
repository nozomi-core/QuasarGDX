package app.quasar.qgl._test

import org.junit.Assert
import org.junit.Test
import kotlin.reflect.full.createInstance

class SecretField {
    private var heya: String? = null

    fun whatName() = heya
    fun useName(name: String) {
        this.heya = name
    }
}

class TestReflection {

    @Test
    fun testSetPrivateField() {
        val kObject = SecretField::class.createInstance()
        val member = kObject::class.java.getDeclaredField("heya")
        member.isAccessible = true

        member.set(kObject, "wow")
        Assert.assertEquals("wow", kObject.whatName())
    }
}