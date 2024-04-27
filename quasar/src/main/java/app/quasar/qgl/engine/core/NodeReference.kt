package app.quasar.qgl.engine.core

import java.lang.ref.WeakReference

class NodeReference<T>(ref: T): WeakReference<T>(ref) {
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