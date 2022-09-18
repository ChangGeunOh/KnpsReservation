package kr.pe.paran.knpsreservation.model

enum class ReservationStatus(val value: String) {
    RESERVATION_AVAILABLE ("icon-reservation"),
    RESERVATION_WAITING("icon-waiting"),
    RESERVATION_NONE("icon-none-reservation"),
    NONE("icon-end");

    companion object {
        fun fromValue(value: String) = when(value.lowercase()) {
            ReservationStatus.RESERVATION_AVAILABLE.value -> ReservationStatus.RESERVATION_AVAILABLE
            ReservationStatus.RESERVATION_WAITING.value -> ReservationStatus.RESERVATION_WAITING
            ReservationStatus.RESERVATION_NONE.value -> ReservationStatus.RESERVATION_NONE
            else -> ReservationStatus.NONE
        }
    }
}
