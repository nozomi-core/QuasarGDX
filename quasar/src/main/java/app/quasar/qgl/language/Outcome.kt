package app.quasar.qgl.language

import java.lang.Exception

sealed class Outcome<out Success, out Failed> {
    class Success<out Success, Failed>(val value: Success): Outcome<Success, Failed>()
    class Failed<out Success, Failed>(val exception: Exception, val failed: Failed): Outcome<Success, Failed>()
}