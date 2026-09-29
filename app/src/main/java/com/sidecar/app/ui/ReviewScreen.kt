package com.sidecar.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidecar.app.model.DiffLine
import com.sidecar.app.model.LineKind
import com.sidecar.app.model.ReviewState
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

@Composable
fun ReviewScreen(state: ReviewState, onRun: () -> Unit) {
    Column {
        Masthead(badge = if (state.offline) "AIRPLANE MODE" else "NETWORK UP")

        Spacer(Modifier.height(26.dp))
        BigStat("01", "CHANGE\nSTAGED", 104.sp)

        Spacer(Modifier.height(24.dp))
        HardRule()

        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth().padding(horizontal = GUTTER),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            BasicText(state.hunk.path, style = Type.Body.copy(letterSpacing = 0.5.sp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Chip("+${state.hunk.added}", Ink.Acid)
                Chip("−${state.hunk.removed}", Ink.Chip)
            }
        }

        Spacer(Modifier.height(12.dp))
        DiffBox(state.hunk.lines)

        Spacer(Modifier.height(16.dp))
        AcidAction(
            label = if (state.running) "WORKING" else "▌ RUN REVIEW",
            enabled = !state.running,
            onClick = onRun,
        )
        Spacer(Modifier.height(8.dp))
        BasicText(
            "RUNS ON THIS PHONE. NOTHING IS UPLOADED.",
            style = Type.Micro.copy(letterSpacing = 1.4.sp),
            modifier = Modifier.padding(horizontal = GUTTER),
        )
    }
}

@Composable
private fun DiffBox(lines: List<DiffLine>) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = GUTTER)
            .border(2.dp, Ink.Ground)
            .padding(vertical = 10.dp),
    ) {
        lines.take(8).forEach { line ->
            val wash = when (line.kind) {
                LineKind.ADD -> Ink.AddWash
                LineKind.DEL -> Ink.DelWash
                LineKind.CTX -> Color.Transparent
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(wash)
                    .padding(horizontal = 10.dp, vertical = 1.dp),
            ) {
                BasicText(line.number?.toString()?.padStart(2) ?: "  ", style = Type.Gutter)
                Spacer(Modifier.width(10.dp))
                BasicText("${line.marker} ${line.text.replace("\t", "  ")}", style = Type.Code)
            }
        }
    }
}
