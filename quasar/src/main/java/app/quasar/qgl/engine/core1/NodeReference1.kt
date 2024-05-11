package app.quasar.qgl.engine.core1

import java.lang.ref.WeakReference

class NodeReference1<T>(ref: T): WeakReference<T>(ref) {
    override fun get(): T? {
        val reference = super.get() ?: return null

        if(reference !is ReadableGameNode) {
            return reference
        }

        if(!reference.isAlive) {
            return null
        }

        return reference
    }
}