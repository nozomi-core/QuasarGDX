package app.quasar.qgl.engine.core

import org.junit.Assert
import org.junit.Test

class NodeProviderTest {

    @Test
    fun testNodeProvider() {
        val provider = NodeProvider1<String>()
        Assert.assertNull(provider.provide())
    }

    @Test
    fun testNodeProviderDefault() {
        val provider = NodeProvider1<String>()
        provider.setDefault("default")
        Assert.assertEquals("default", provider.provide())
    }

    @Test
    fun testNodeProviderDefaultAndPush() {
        val provider = NodeProvider1<String>()
        provider.setDefault("default")
        provider.push("push")
        Assert.assertEquals("push", provider.provide())
    }

    @Test
    fun testNodeProviderDefaultAndPushPop() {
        val provider = NodeProvider1<String>()
        provider.setDefault("default")
        provider.push("push")
        provider.pop("push")
        Assert.assertEquals("default", provider.provide())
    }
}