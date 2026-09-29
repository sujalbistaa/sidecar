package com.sidecar.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidecar.app.model.SessionStats
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

@Composable
fun SessionScreen(stats: SessionStats) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Masthead(badge = stats.since, showDot = false)

        Spacer(Modifier.height(30.dp))
        Column(Modifier.padding(horizontal = GUTTER)) {
            BasicText("UPLOADED TO A SERVER", style = Type.Micro.copy(letterSpacing = 2.sp))
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                BasicText("${stats.bytesSent}", style = Type.display(124.sp, line = 100.sp, track = (-6).sp))
                Spacer(Modifier.width(10.dp))
                BasicText(
                    "BYTES",
                    style = Type.Micro.copy(fontSize = 11.sp, letterSpacing = 1.8.sp),
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        HardRule()

        Column(Modifier.padding(horizontal = GUTTER)) {
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Figure("${stats.diffsReviewed}", "DIFFS REVIEWED")
                Figure("${stats.defectsCaught}", "DEFECTS CAUGHT", TextAlign.End)
            }
            Spacer(Modifier.height(16.dp))
            Hairline()
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Figure(stats.tokensGenerated, "TOKENS GENERATED")
                Figure(stats.averageRate, "AVG TOK/SEC", TextAlign.End)
            }
            Spacer(Modifier.height(16.dp))
            Hairline()

            Spacer(Modifier.height(18.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .border(2.dp, Ink.Ground)
                    .background(Ink.Acid)
                    .padding(14.dp),
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    BasicText("₹0.00", style = Type.display(46.sp, line = 38.sp, track = 0.sp))
                    Spacer(Modifier.width(8.dp))
                    BasicText(
                        "SPENT",
                        style = Type.MicroInk.copy(fontSize = 10.sp, letterSpacing = 1.4.sp),
                        modifier = Modifier.padding(bottom = 5.dp),
                    )
                }
                Spacer(Modifier.height(9.dp))
                BasicText(
                    "Every review this session ran on the phone. There is no API bill, and there never will be.",
                    style = Type.Body.copy(fontSize = 10.sp, lineHeight = 16.sp),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Figure(value: String, caption: String, align: TextAlign = TextAlign.Start) {
    Column {
        BasicText(value, style = Type.display(42.sp, line = 38.sp, track = 0.sp).copy(textAlign = align))
        Spacer(Modifier.height(4.dp))
        BasicText(caption, style = Type.Micro.copy(textAlign = align))
    }
}
