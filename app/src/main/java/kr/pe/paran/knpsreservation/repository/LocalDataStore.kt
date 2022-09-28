package kr.pe.paran.knpsreservation.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.pe.paran.knpsreservation.model.SettingData
import kr.pe.paran.knpsreservation.repository.LocalDataStore.PreferencesKeys.SETTING_DATA

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting_data_store")

class LocalDataStore(context: Context) {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val SETTING_DATA = stringPreferencesKey("setting_data")
    }

    suspend fun getSettingData(): Flow<SettingData> {
        return dataStore.data.map { preferences ->
            val jsonString = preferences[SETTING_DATA] ?: ""
            if (jsonString.isEmpty()) SettingData() else Json.decodeFromString(jsonString)
        }
    }

    suspend fun setSettingData(settingData: SettingData) {
        dataStore.edit {
            it[SETTING_DATA] = Json.encodeToString(settingData)
        }
    }
}