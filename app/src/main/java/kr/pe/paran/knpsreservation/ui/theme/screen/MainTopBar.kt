package kr.pe.paran.knpsreservation.ui.theme.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainTopBar(
    title: String = "국립공원 대피소 예약",
    isRunning: Boolean,
    onChangeRunning: (Boolean) -> Unit,
    onClickTune: () -> Unit
) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(title) },
        actions = {
            IconButton(onClick = { onChangeRunning(!isRunning) }) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = "Play Button"
                )
            }
            IconButton(onClick = {
                onClickTune()
            }) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Tune Button"
                )
            }
        }
    )

}