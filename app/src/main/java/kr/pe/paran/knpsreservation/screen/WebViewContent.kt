package kr.pe.paran.knpsreservation.screen

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.pe.paran.knpsreservation.INSPECTION_CYCLE_MINUTE
import kr.pe.paran.knpsreservation.MainViewModel
import kr.pe.paran.knpsreservation.model.ReservationData
import kr.pe.paran.knpsreservation.model.ReservationStatus
import kr.pe.paran.knpsreservation.model.Shelter
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WebViewContent(
    onDone: (MutableList<ReservationData>, List<String>) -> Unit,
    isRunning: Boolean,
    phoneNumber: String,
) {
    var webView by remember { mutableStateOf<WebView?>(null) }

    Log.i(":::::", "isRunning>$isRunning")
    Log.i(":::::", "phoneNumber>$phoneNumber")

    // Keep Screen ON
    val currentView = LocalView.current
    DisposableEffect(key1 = Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }

    Log.i(":::::", "WebViewContent> isRunning> $isRunning > Phone> $phoneNumber")

    val reservationUrl: String =
        "https://reservation.knps.or.kr/reservation/shelter/searchSimpleShelterReservation.do"

    webView?.let {
        LaunchedEffect(key1 = isRunning) {
            CoroutineScope(Dispatchers.Main).launch {
                while (isRunning) {
                    it.reload()
                    delay(1000 * 60 * INSPECTION_CYCLE_MINUTE)               // 10분 마다 스크롱
                }
            }
        }
    }

    AndroidView(
        modifier = Modifier
            .width(0.dp)
            .height(0.dp),
        factory = {

            WebView(it).apply {
                settings.apply {
                    javaScriptEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT

                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = true

                    blockNetworkImage = false
                    loadsImagesAutomatically = true

                    useWideViewPort = true
                    loadWithOverviewMode = true
                    javaScriptCanOpenWindowsAutomatically = true
                    mediaPlaybackRequiresUserGesture = false

                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    allowContentAccess = true
                    setGeolocationEnabled(true)
                    allowFileAccess = true

                    userAgentString =
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36"
                    addJavascriptInterface(
                        MyJavaScriptInterface { list, dayList ->
                            onDone(list, dayList)
                        },
                        "HtmlViewer"
                    );
                }

                webViewClient = object : WebViewClient() {
                    override fun onLoadResource(view: WebView?, url: String?) {
                        super.onLoadResource(view, url)
                        Log.i(":::::", ">>${url.toString()} :: ${view.toString()}")
                        url?.let { resourceUrl ->
                            if (resourceUrl.contains("https://netfunnel.knps.or.kr/ts.wseq?opcode=5004&key=")) {
                                view?.loadUrl(
                                    "javascript:window.HtmlViewer.showHTML" +
                                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
                                );
                            }
                        }
                    }

                }
            }
        }
    ) {

        webView = it
        it.loadUrl(reservationUrl)
    }
}

internal class MyJavaScriptInterface(
    private val onDone: (MutableList<ReservationData>, List<String>) -> Unit,
) {

    @JavascriptInterface
    fun showHTML(html: String) {

        val document = Jsoup.parse(html)
        val daysList = mutableListOf<String>()
        val firstDay =
            document.selectXpath("//*[@id=\"tab1-1\"]/div[3]/div/div[1]/table[2]/thead/tr[2]")
                .first()?.select("td")?.first()?.text()?.toInt() ?: 0
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, firstDay - 1)

        document.selectXpath("//*[@id=\"tab1-1\"]/div[3]/div/div[1]/table[2]/thead/tr[2]").first()
            ?.select("td")?.forEach {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val formattedDate: String = df.format(calendar.time)
                daysList.add(formattedDate)
            }

        val reservationList = mutableListOf<ReservationData>()

        document.selectXpath("//*[@id=\"tab1-1\"]/div[3]/div/div[2]/table[2]/tbody").select("tr")
            .forEachIndexed { index, element ->
                val shelter = Shelter.values()[index]
                element.select("td").forEachIndexed { index1, element1 ->
                    val code = element1.id()
                    val status = element1.select("i").attr("class")
                    val reservationData = ReservationData(
                        shelter = shelter,
                        day = daysList[index1],
                        code = code,
                        status = ReservationStatus.fromValue(status)
                    )
                    reservationList.add(reservationData)
                }
            }
        Log.i(":::::", "onDone>${Date().time}")
        onDone(reservationList, daysList.toList())
    }
}
