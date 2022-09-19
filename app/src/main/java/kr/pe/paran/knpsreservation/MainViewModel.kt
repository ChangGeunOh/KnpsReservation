package kr.pe.paran.knpsreservation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.pe.paran.knpsreservation.model.ReservationData
import kr.pe.paran.knpsreservation.model.Shelter

class MainViewModel : ViewModel() {


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


    private var _reservationList =
        MutableStateFlow<MutableList<List<ReservationData>>>(mutableListOf())
    val reservationList = _reservationList.asStateFlow()
    fun addReservationList(reservationList: List<ReservationData>) {
        _reservationList.value.add(reservationList)
    }

    private var _favoriteShelter by mutableStateOf(
        ReservationData(
            shelter = Shelter.NONE,
            day = ""
        )
    )

    val favoriteShelter = _favoriteShelter

    fun setFavoriteShelter(reservationData: ReservationData) {
        _favoriteShelter = reservationData
    }

    var searchResultList = mutableStateListOf<ReservationData>()

    private var _availableReservation = MutableStateFlow<ReservationData?>(null)
    val availableReservation = _availableReservation.asStateFlow()

    fun setReservationList(reservationList: MutableList<ReservationData>) {
        viewModelScope.launch {
            reservationList.firstOrNull {
                it.day == _favoriteShelter.day && it.shelter == _favoriteShelter.shelter
            }?.let {
                searchResultList.add(it)
                if (searchResultList.size > 10) searchResultList.removeRange(0, searchResultList.size - 12)
                _availableReservation.value = it.availableReservation()
            }
        }
    }


    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()
    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }
}


