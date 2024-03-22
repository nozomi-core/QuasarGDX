package app.quasar.qgl.language

import java.lang.Exception

sealed class Outcome<out S, out F> {
    class Success<out S, F>(val value: S): Outcome<S, F>()
    class Failed<out S, F>(val exception: Exception, val failed: F): Outcome<S, F>()

    fun getOrNull(): S? {
        return when(this) {
            is Outcome.Success -> value
            is Failed -> null
        }
    }
}