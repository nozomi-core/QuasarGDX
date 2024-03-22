package app.quasar.gdx.localisation

import app.quasar.qgl.language.Outcome
import app.quasar.qgl.serialize.QGLJson
import app.quasar.qgl.serialize.qglDecodeJsonFile
import java.io.File

private var currentLanguage = LocalLanguage.ENG
private var currentLanguageFile: QGLJson? = null

fun changeRuntimeLanguage(useLanguage: LocalLanguage) {
    currentLanguage = useLanguage

    val languageFile = File(whatLanguagePath(currentLanguage))

    currentLanguageFile = when(val result = qglDecodeJsonFile(languageFile)) {
        is Outcome.Success -> result.value
        is Outcome.Failed ->  null
    }
}

fun findLocalString(id: LocalString): String {
    val languageFile = currentLanguageFile

    if(languageFile != null) {
        return languageFile.getLanguageResource(id)
    }

    val languageFilepath = whatLanguagePath(currentLanguage)

    return when(val result = qglDecodeJsonFile(File(languageFilepath))) {
        is Outcome.Success -> {
            result.value.run {
                currentLanguageFile = this
                getString(id.name)
            }
        }
        is Outcome.Failed -> throw result.exception
    }
}

//Gets the language resource and wraps the error if it can't find it
fun QGLJson.getLanguageResource(id: LocalString): String {
    return try {
        getString(id.name)
    }catch (e: Exception) {
        throw Exception("Unable to find the string resource id '${id.name}' for this locale id (${currentLanguage.name})")
    }
}

private fun whatLanguagePath(language: LocalLanguage): String ="strings/${language.name.lowercase()}.json"