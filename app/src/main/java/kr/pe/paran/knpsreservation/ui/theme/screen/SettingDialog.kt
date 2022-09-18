package kr.pe.paran.knpsreservation.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.pe.paran.knpsreservation.MainViewModel
import kr.pe.paran.knpsreservation.component.GenieDatePicker
import kr.pe.paran.knpsreservation.component.GenieDropDownMenu
import kr.pe.paran.knpsreservation.component.GenieOutlinedTextField
import kr.pe.paran.knpsreservation.component.GenieRoundedButton
import kr.pe.paran.knpsreservation.model.Shelter

@Composable
fun SettingDialog(viewModel: MainViewModel, onDismiss: () -> Unit) {

    val context = LocalContext.current

    var isShowDatePicker by remember { mutableStateOf(false) }
    var reservationData by remember { mutableStateOf(viewModel.favoriteShelter) }
    var phoneNumber by remember { mutableStateOf(viewModel.phoneNumber.value) }
    val dateList by viewModel.days.collectAsState()

    val itemList = Shelter.values().map { it.value }.dropLast(1)


    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                GenieDropDownMenu(
                    label = "대피소",
                    items = itemList,
                    text = if(reservationData.shelter != Shelter.NONE) reservationData.shelter.value else "",
                    onValueChange = {
                        reservationData = reservationData.copy(shelter = Shelter.fromValue(it))
                    }
                )
                GenieOutlinedTextField(
                    label = "예약날짜",
                    text = reservationData.commaDay(),
                    enabled = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isShowDatePicker = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendar Icon"
                            )
                        }
                    },
                )
                GenieOutlinedTextField(
                    label = "연락번호",
                    text = phoneNumber,
                    enabled = true,
                    onValueChange = { phoneNumber = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))
                GenieRoundedButton(text = "설정하기") {
                    if (dateList.isNotEmpty() && !dateList.contains(
                            reservationData.day.replace(".", "")
                        )
                    ) {
                        Toast.makeText(context, "예약 가능한 날짜가 아닙니다.", Toast.LENGTH_SHORT).show()
                    } else if (reservationData.shelter == Shelter.NONE) {
                        Toast.makeText(context, "예약할 대피소를 선택하세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.setFavoriteShelter(reservationData = reservationData)
                        viewModel.setPhoneNumber(phoneNumber)
                        onDismiss()
                    }
                }
            }
        }
    }

    if (isShowDatePicker) {
        GenieDatePicker(
            onDismiss = {
                it?.let {
                    reservationData.setDate(it.replace(".", ""))
                    reservationData = reservationData.copy()
                }
                isShowDatePicker = false
            }
        )
    }
}
