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
                    label = "λνΌμ",
                    items = itemList,
                    text = if (settingData.shelter != Shelter.NONE) settingData.shelter.value else "",
                    onValueChange = {
                        onChangeData(settingData.copy(shelter = Shelter.fromValue(it)))

                    }
                )
                GenieOutlinedTextField(
                    label = "μμ½λ μ§",
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
                    label = "μ°λ½λ²νΈ",
                    text = settingData.phoneNumber,
                    enabled = true,
                    onValueChange = { onChangeData(settingData.copy(phoneNumber = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))
                GenieRoundedButton(text = "μ€μ νκΈ°") {
                    if (validDataList.isNotEmpty() && !validDataList.contains(
                            settingData.date.replace(".", "")
                        )
                    ) {
                        Toast.makeText(context, "μμ½ κ°λ₯ν λ μ§κ° μλλλ€.", Toast.LENGTH_SHORT).show()
                    } else if (settingData.shelter == Shelter.NONE) {
                        Toast.makeText(context, "μμ½ν  λνΌμλ₯Ό μ ννμΈμ.", Toast.LENGTH_SHORT).show()
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
