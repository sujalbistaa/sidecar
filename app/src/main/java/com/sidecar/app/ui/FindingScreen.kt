package com.sidecar.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidecar.app.model.ReviewState
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

/**
 * The verdict is the headline. A 1B model produces two sentences, so those two
 * sentences get set as editorial copy instead of being buried in a card.
 */
@Composable
fun FindingScreen(state: ReviewState, onBack: () -> Unit, onApply: () -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = GUTTER),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BasicText(
                "← ${state.hunk.path}",
                style = Type.Wordmark,
                modifier = Modifier.clickable { onBack() },
            )
        }

        Spacer(Modifier.height(22.dp))
        BasicText("VERDICT", style = Type.Micro.copy(letterSpacing = 2.sp), modifier = Modifier.padding(horizontal = GUTTER))

        Spacer(Modifier.height(6.dp))
        BasicText(
            state.headline,
            style = Type.display(58.sp, line = 54.sp),
            modifier = Modifier.padding(horizontal = GUTTER).highlight(),
        )

        Spacer(Modifier.height(18.dp))
        HardRule()

        Spacer(Modifier.height(14.dp))
        BasicText(
            state.output.ifEmpty { "Waiting for the model." },
            style = Type.Body,
            modifier = Modifier.padding(horizontal = GUTTER),
        )

        Spacer(Modifier.height(20.dp))
        BasicText("PATCH", style = Type.Micro.copy(letterSpacing = 2.sp), modifier = Modifier.padding(horizontal = GUTTER))

        Spacer(Modifier.height(8.dp))
        Column(
            Modifier.fillMaxWidth().padding(horizontal = GUTTER).border(2.dp, Ink.Ground),
        ) {
            BasicText(
                "- for i := 0; i <= len(s.tokens); i++ {",
                style = Type.Code,
                modifier = Modifier.fillMaxWidth().background(Ink.DelWash).padding(horizontal = 10.dp, vertical = 5.dp),
            )
            BasicText(
                "+ for i := 0; i < len(s.tokens); i++ {",
                style = Type.Code,
                modifier = Modifier.fillMaxWidth().background(Ink.AddWash).padding(horizontal = 10.dp, vertical = 5.dp),
            )
        }

        Spacer(Modifier.height(22.dp))
        Column(Modifier.padding(horizontal = GUTTER)) {
            Hairline()
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                StatCell(String.format("%.1f", state.tokensPerSec), "TOK/SEC", Modifier.weight(1f))
                StatCell(state.elapsedLabel, "ELAPSED", Modifier.weight(1f))
                StatCell("${state.bytesSent}", "BYTES SENT", Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(24.dp))
        AcidAction("▌ APPLY PATCH", onClick = onApply)
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun StatCell(value: String, caption: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        BasicText(value, style = Type.display(30.sp, line = 30.sp, track = 0.sp))
        Spacer(Modifier.height(3.dp))
        BasicText(caption, style = Type.Micro.copy(letterSpacing = 1.4.sp))
    }
}
