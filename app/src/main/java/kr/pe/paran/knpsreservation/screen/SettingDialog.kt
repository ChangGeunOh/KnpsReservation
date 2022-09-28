package kr.pe.paran.knpsreservation.screen

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
import kr.pe.paran.knpsreservation.model.SettingData
import kr.pe.paran.knpsreservation.model.Shelter

@Composable
fun SettingDialog(
    settingData: SettingData,
    onChangeData: (SettingData) -> Unit,
    validDataList: List<String> = emptyList<String>(),
    onSaveData: (SettingData) -> Unit,
    onDismiss: () -> Unit,
) {

    val context = LocalContext.current
    var isShowDatePicker by remember { mutableStateOf(false) }
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
                    text = if (settingData.shelter != Shelter.NONE) settingData.shelter.value else "",
                    onValueChange = {
                        onChangeData(settingData.copy(shelter = Shelter.fromValue(it)))

                    }
                )
                GenieOutlinedTextField(
                    label = "예약날짜",
                    text = settingData.date,
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
                    text = settingData.phoneNumber,
                    enabled = true,
                    onValueChange = { onChangeData(settingData.copy(phoneNumber = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))
                GenieRoundedButton(text = "설정하기") {
                    if (validDataList.isNotEmpty() && !validDataList.contains(
                            settingData.date.replace(".", "")
                        )
                    ) {
                        Toast.makeText(context, "예약 가능한 날짜가 아닙니다.", Toast.LENGTH_SHORT).show()
                    } else if (settingData.shelter == Shelter.NONE) {
                        Toast.makeText(context, "예약할 대피소를 선택하세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        onSaveData(settingData)
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
                    onChangeData(settingData.copy(date = it))
                }
                isShowDatePicker = false
            }
        )
    }
}
