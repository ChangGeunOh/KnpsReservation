package kr.pe.paran.knpsreservation

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kr.pe.paran.knpsreservation.model.ReservationData
import kr.pe.paran.knpsreservation.model.SettingData
import kr.pe.paran.knpsreservation.model.Shelter
import kr.pe.paran.knpsreservation.repository.LocalDataStore
import kr.pe.paran.knpsreservation.repository.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val repository by lazy { Repository(LocalDataStore(context = context))}


    private var _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()
    fun setIsRunning(isRunning: Boolean) {
        _isRunning.value = isRunning
    }

    private var _days = MutableStateFlow<List<String>>(listOf())
    val days = _days.asStateFlow()

    fun setDays(days: List<String>) {
        _days.value = days
    }


    var searchResultList = mutableStateListOf<ReservationData>()

    private var _availableReservation = MutableStateFlow<ReservationData?>(null)
    val availableReservation = _availableReservation.asStateFlow()

    fun setReservationList(reservationList: MutableList<ReservationData>) {
        viewModelScope.launch {
            reservationList.firstOrNull {
                it.day == _settingData.value.date.replace(".", "") && it.shelter == _settingData.value.shelter
            }?.let {
                searchResultList.add(it)
                if (searchResultList.size > 10) searchResultList.removeRange(
                    0,
                    searchResultList.size - 12
                )
                _availableReservation.value = it.availableReservation()
            }
        }
    }


    private var _settingData = MutableStateFlow(SettingData())
    val settingData = _settingData.asStateFlow()

    fun getSettingData() {
        viewModelScope.launch {
            repository.getSettingData().collect {
                _settingData.value = it
            }
        }
    }

    fun setSettingData(settingData: SettingData) {
        _settingData.value = settingData
    }

    fun saveSettingData(settingData: SettingData) {
        viewModelScope.launch {
            repository.setSettingData(settingData = settingData)
        }
        getSettingData()
    }
}


