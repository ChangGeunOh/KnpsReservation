package kr.pe.paran.knpsreservation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kr.pe.paran.knpsreservation.ui.theme.KnpsReservationTheme
import kr.pe.paran.knpsreservation.ui.theme.screen.MainScreen
import java.util.*


const val INSPECTION_CYCLE_MINUTE = 10L

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            KnpsReservationTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

private fun checkAvailableReservation(
    context: Context,
    webView: WebView,
    urlString: String = "https://reservation.knps.or.kr/reservation/shelter/searchSimpleShelterReservation.do"
) {

    val t = Timer()
    t.scheduleAtFixedRate(
        object : TimerTask() {
            override fun run() {
                webView.loadUrl(urlString)
            }
        },
        0,
        1000 * 60 * 3 // 3분마다.
    )
    t.cancel()
}

