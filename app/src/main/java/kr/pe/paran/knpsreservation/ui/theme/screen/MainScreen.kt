package kr.pe.paran.knpsreservation.ui.theme.screen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.telephony.SmsManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kr.pe.paran.knpsreservation.MainViewModel
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "PermissionLaunchedDuringComposition")
@Composable
fun MainScreen(viewModel: MainViewModel) {

    val context = LocalContext.current

    val title by remember { mutableStateOf("국립공원 대피소 예약") }
    var isShowTune by remember { mutableStateOf(false) }
    val isRunning by viewModel.isRunning.collectAsState()
    val availableReservation by viewModel.availableReservation.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.SEND_SMS
    )

    if (availableReservation != null && phoneNumber.isNotEmpty()) {
        val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
        smsManager.sendTextMessage(
            phoneNumber,
            null,
            availableReservation!!.getMessage(),
            null,
            null
        )
    }

    val resultList = viewModel.searchResultList

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MainTopBar(
                title = title,
                isRunning = isRunning,
                onChangeRunning = { viewModel.setIsRunning(it) },
                onClickTune = { isShowTune = true }
            )
        }
    ) {
        when (cameraPermissionState.status) {
            is PermissionStatus.Denied -> {
                cameraPermissionState.launchPermissionRequest()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { showApplicationSettings(context = context) }
                    ) {
                        Text("문자 보내기 권한 설정")
                    }
                }
            }
            PermissionStatus.Granted -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    WebViewContent(
                        onDone = { it, dayList ->
                            viewModel.setReservationList(it)
                            viewModel.setDays(days = dayList)
                        },
                        viewModel = viewModel
                    )
                    MainContent(resultList = resultList)
                }
            }
        }
    }

    if (isShowTune) {
        SettingDialog(viewModel = viewModel) {
            isShowTune = false
        }
    }
}

fun showApplicationSettings(context: Context) {
    val appDetail = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + context.packageName)
    )
    appDetail.addCategory(Intent.CATEGORY_DEFAULT)
    appDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(appDetail)
}

