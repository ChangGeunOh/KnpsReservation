package kr.pe.paran.knpsreservation.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.pe.paran.knpsreservation.model.ReservationData

@Composable
fun MainContent(resultList: List<ReservationData>) {

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0,
        initialFirstVisibleItemScrollOffset = 0
    )
    LazyColumn(modifier = Modifier.fillMaxWidth(), reverseLayout = true, state = lazyListState) {
        items(items = resultList) {
            ReservationItem(reservationData = it)
        }
    }
}

@Composable
fun ReservationItem(reservationData: ReservationData) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            drawLine(
                color = Color.LightGray,
                Offset(x = 16f, y = size.height - 1f),
                Offset(x = size.width - 16f, size.height - 1f),
                strokeWidth = 1f
            )
        }
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.wrapContentWidth()) {
            Row {
                Text("대피소명 :   ", fontSize = 10.sp, color = Color.DarkGray)
                Text(reservationData.shelter.value)
            }
            Row {
                Text("점검일시 :   ", fontSize = 10.sp, color = Color.DarkGray)
                Text(reservationData.getRegDate())
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier.wrapContentWidth()) {
            Row {
                Text("희망일자 :   ", fontSize = 10.sp, color = Color.DarkGray)
                Text(reservationData.commaDay())
            }
            Row {
                Text("점검결과 :   ", fontSize = 10.sp, color = Color.DarkGray)
                Text(reservationData.getStatusKor(), color = Color.Red)
            }
        }
    }
}