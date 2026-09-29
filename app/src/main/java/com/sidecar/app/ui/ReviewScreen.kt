package com.sidecar.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sidecar.app.model.DiffLine
import com.sidecar.app.model.Hunk
import com.sidecar.app.model.LineKind
import com.sidecar.app.model.ReviewState
import com.sidecar.app.model.SampleDiff
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

private val GUTTER = 20.dp

@Composable
fun ReviewScreen(
    state: ReviewState,
    onRun: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(Ink.Ground)
            .padding(horizontal = GUTTER)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(56.dp))
        Masthead(offline = state.offline)

        Spacer(Modifier.height(28.dp))
        BasicText("review", style = Type.Display)

        Spacer(Modifier.height(24.dp))
        Rule()

        Spacer(Modifier.height(14.dp))
        FileHeader(state.hunk)

        Spacer(Modifier.height(12.dp))
        DiffBlock(state.hunk.lines)

        Spacer(Modifier.height(20.dp))
        RunAction(running = state.running, onRun = onRun)

        if (state.output.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            Rule()
            Spacer(Modifier.height(14.dp))
            BasicText("FINDING", style = Type.Label)
            Spacer(Modifier.height(8.dp))
            BasicText(state.output, style = Type.Body)
        }

        Spacer(Modifier.height(32.dp))
        Rule()
        Spacer(Modifier.height(10.dp))
        Telemetry(state)
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun Masthead(offline: Boolean) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText("SIDECAR", style = Type.Label.copy(color = Ink.Paper))
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Square, not a circle. Hard edges everywhere.
            Box(Modifier.size(6.dp).background(if (offline) Ink.Acid else Ink.Blood))
            Spacer(Modifier.width(7.dp))
            BasicText(
                if (offline) "OFFLINE" else "NETWORK UP",
                style = Type.Label.copy(color = if (offline) Ink.Acid else Ink.Blood),
            )
        }
    }
}

@Composable
private fun FileHeader(hunk: Hunk) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BasicText(hunk.path, style = Type.Label.copy(color = Ink.Paper))
        Row {
            BasicText("+${hunk.added}", style = Type.Label.copy(color = Ink.Acid))
            Spacer(Modifier.width(10.dp))
            BasicText("−${hunk.removed}", style = Type.Label.copy(color = Ink.Blood))
        }
    }
}

@Composable
private fun DiffBlock(lines: List<DiffLine>) {
    Column(Modifier.fillMaxWidth()) {
        lines.forEach { line ->
            val wash = when (line.kind) {
                LineKind.ADD -> Ink.AddBg
                LineKind.DEL -> Ink.DelBg
                LineKind.CTX -> Color.Transparent
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(wash)
                    .padding(vertical = 1.dp),
            ) {
                BasicText(
                    text = line.number?.toString()?.padStart(3) ?: "   ",
                    style = Type.Gutter,
                )
                Spacer(Modifier.width(12.dp))
                BasicText(
                    text = line.marker + " " + line.text,
                    style = Type.Code.copy(
                        color = when (line.kind) {
                            LineKind.ADD -> Ink.Acid
                            LineKind.DEL -> Ink.Blood
                            LineKind.CTX -> Ink.Paper
                        },
                    ),
                )
            }
        }
    }
}

@Composable
private fun RunAction(running: Boolean, onRun: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(if (running) Ink.Faint else Ink.Acid)
            .clickable(enabled = !running) { onRun() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            if (running) "WORKING" else "▌ RUN REVIEW",
            style = Type.Action.copy(color = if (running) Ink.Dim else Ink.Ground),
        )
    }
}

@Composable
private fun Telemetry(state: ReviewState) {
    val rate = if (state.tokensPerSec > 0) String.format("%.1f tok/s", state.tokensPerSec) else "idle"
    BasicText(
        "$rate  ·  ${state.model}  ·  ${state.bytesSent} bytes sent",
        style = Type.Meta,
    )
}

@Composable
private fun Rule() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(Ink.Faint))
}

@Preview(widthDp = 412, heightDp = 915, backgroundColor = 0xFF0C0C0B, showBackground = true)
@Composable
private fun PreviewIdle() {
    ReviewScreen(state = ReviewState(hunk = SampleDiff.hunk), onRun = {})
}

@Preview(widthDp = 412, heightDp = 915, backgroundColor = 0xFF0C0C0B, showBackground = true)
@Composable
private fun PreviewReviewed() {
    ReviewScreen(
        state = ReviewState(
            hunk = SampleDiff.hunk,
            output = SampleDiff.finding,
            tokensPerSec = 18.4,
        ),
        onRun = {},
    )
}
