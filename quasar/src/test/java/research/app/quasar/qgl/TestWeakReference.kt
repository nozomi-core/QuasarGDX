package research.app.quasar.qgl

import org.junit.Assert
import org.junit.Test
import java.lang.ref.WeakReference

class TestWeakReference {

    @Test
    fun testWeakReferenceSet() {
        val set = hashSetOf<WeakReference<String>>()
        set.add(WeakReference("hey"))
        Assert.assertEquals(1, set.size)
        set.remove(WeakReference("hey"))

        set.find {
            it.get() == "hey"
        }?.let {
            set.remove(it)
        }
        Assert.assertEquals(0, set.size)
    }
}