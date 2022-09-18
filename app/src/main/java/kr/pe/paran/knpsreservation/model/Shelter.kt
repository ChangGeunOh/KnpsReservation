package kr.pe.paran.knpsreservation.model

enum class Shelter(val value: String) {
    BYONGSORYEONG("벽소령대피소"),
    SESEOK("세석대피소"),
    JANGTEMOK("장터목대피소"),
    ROTARY("로타리대피소"),
    CHIBANMOK("치밭목대피소"),
    YEONHACHEON("연하천대피소"),
    NONGODAN("노고단대피소"),
    NONE("NONE");

    companion object {
        fun fromValue(value: String): Shelter = when(value) {
            Shelter.BYONGSORYEONG.value -> Shelter.BYONGSORYEONG
            Shelter.SESEOK.value -> Shelter.SESEOK
            Shelter.JANGTEMOK.value -> Shelter.JANGTEMOK
            Shelter.ROTARY.value -> Shelter.ROTARY
            Shelter.CHIBANMOK.value -> Shelter.CHIBANMOK
            Shelter.YEONHACHEON.value -> Shelter.YEONHACHEON
            Shelter.NONGODAN.value -> Shelter.NONGODAN
            else -> Shelter.NONE
        }

    }
}


//벽소령대피소, 세석대피소, 장터목대피소, 로타리대피소, 치밭목대피소, 연하천대피소, 노고단대피소

/*
    companion object {
        fun fromValue(value: String) = when(value.lowercase()) {
            ReservationStatus.RESERVATION_AVAILABLE.value -> ReservationStatus.RESERVATION_AVAILABLE
            ReservationStatus.RESERVATION_WAITING.value -> ReservationStatus.RESERVATION_WAITING
            ReservationStatus.RESERVATION_NONE.value -> ReservationStatus.RESERVATION_NONE
            else -> ReservationStatus.NONE
        }
    }
 */