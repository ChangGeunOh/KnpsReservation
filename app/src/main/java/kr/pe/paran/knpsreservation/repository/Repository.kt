package kr.pe.paran.knpsreservation.repository

import kotlinx.coroutines.flow.Flow
import kr.pe.paran.knpsreservation.model.SettingData

class Repository(private val localDataStore: LocalDataStore) {

    suspend fun setSettingData(settingData: SettingData) {
        localDataStore.setSettingData(settingData)
    }

    suspend fun getSettingData(): Flow<SettingData> {
        return localDataStore.getSettingData()
    }

}