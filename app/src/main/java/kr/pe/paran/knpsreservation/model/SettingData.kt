package kr.pe.paran.knpsreservation.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class SettingData(
    var shelter: Shelter = Shelter.NONE,
    val date: String = "",
    val phoneNumber: String = ""
) {

    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(jsonString: String): SettingData {
            return Json.decodeFromString(jsonString)
        }
    }
}
