package com.sidecar.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidecar.app.model.ReviewRecord
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

@Composable
fun LogScreen(records: List<ReviewRecord>, onOpen: (ReviewRecord) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Masthead(badge = "AIRPLANE MODE")

        Spacer(Modifier.height(26.dp))
        BigStat("${records.size}", "REVIEWS\nTODAY", 104.sp)

        Spacer(Modifier.height(24.dp))
        HardRule()

        records.forEach { record ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable { onOpen(record) }
                    .padding(horizontal = GUTTER),
            ) {
                Spacer(Modifier.height(13.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    BasicText(record.path, style = Type.Body)
                    BasicText(record.ago, style = Type.Micro.copy(letterSpacing = 1.4.sp))
                }
                Spacer(Modifier.height(5.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Chip(record.verdict, if (record.clean) Ink.Acid else Ink.Chip)
                    BasicText(record.rate, style = Type.Micro.copy(letterSpacing = 1.2.sp))
                }
                Spacer(Modifier.height(13.dp))
                Hairline()
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}
