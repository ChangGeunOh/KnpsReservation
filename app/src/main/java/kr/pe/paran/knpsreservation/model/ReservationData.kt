package kr.pe.paran.knpsreservation.model

import java.text.SimpleDateFormat
import java.util.*

data class ReservationData(
    val shelter: Shelter = Shelter.NONE,
    var day: String = "",
    val code: String = "",
    val status: ReservationStatus = ReservationStatus.NONE,
    var regDate: Long = Date().time
) {

    fun setDate(reservationDate: String) {
        day = reservationDate
    }


//    fun setReservationDate(reservationDate: String) {
//        date = SimpleDateFormat(
//            "yyyy.MM.dd",
//            Locale.getDefault()
//        ).parse(reservationDate)?.time ?: 0L
//    }
//
//    fun getReservationDate(): String {
//        return SimpleDateFormat(
//            "yyyy.MM.dd",
//            Locale.getDefault()
//        ).format(date)
//    }

    fun getRegDate(): String {
        return SimpleDateFormat(
            "yyyy.MM.dd hh:mm",
            Locale.getDefault()
        ).format(regDate)
    }

    fun getStatusKor(): String {
        return when (status) {
            ReservationStatus.RESERVATION_AVAILABLE -> "예약가능"
            ReservationStatus.RESERVATION_WAITING -> "대기가능"
            ReservationStatus.RESERVATION_NONE -> "예약불가"
            ReservationStatus.NONE -> "이용불가"
        }
    }

    fun commaDay(): String {
        // "YYYYMMDD" -> "YYYY.MM.DD"

        return if (day.length > 6) day.substring(0..3) + "." + day.subSequence(4..5) + "." + day.substring(6..7) else ""
    }

    fun availableReservation(): ReservationData? {
        return if (status == ReservationStatus.RESERVATION_AVAILABLE || status == ReservationStatus.RESERVATION_WAITING) this else null
    }

    fun getMessage(): String {
        return "${shelter.value}, ${day}, ${getStatusKor()}"
    }
}
