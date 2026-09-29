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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidecar.app.model.ModelSpec
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

/**
 * The pitch screen. Params, quantisation, backend, file size and a byte counter
 * pinned at zero — the argument that nothing left the device, as a spec sheet.
 */
@Composable
fun ModelScreen(spec: ModelSpec, loaded: Boolean) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Masthead(badge = if (loaded) "LOADED" else "NOT LOADED")

        Spacer(Modifier.height(22.dp))
        Row(
            Modifier.fillMaxWidth().padding(horizontal = GUTTER),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                BasicText("1B", style = Type.display(104.sp, track = (-4).sp))
                Spacer(Modifier.width(12.dp))
                BasicText(
                    "PARAMS\nON DEVICE",
                    style = Type.Micro.copy(lineHeight = 15.sp),
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
            Box(
                Modifier
                    .padding(top = 18.dp)
                    .rotate(-7f)
                    .border(2.dp, Ink.Ground)
                    .padding(horizontal = 8.dp, vertical = 7.dp),
            ) {
                BasicText("NO\nNETWORK\nREQUIRED", style = Type.MicroInk.copy(lineHeight = 13.sp))
            }
        }

        Spacer(Modifier.height(22.dp))
        HardRule()
        Spacer(Modifier.height(14.dp))

        SpecRow("MODEL", spec.name)
        SpecRow("QUANTISATION", spec.quantisation)
        SpecRow("FILE SIZE", spec.fileSize)
        SpecRow("RUNTIME", spec.runtime)
        SpecRow("BACKEND", spec.backend)
        SpecRow("CONTEXT", spec.context)
        SpecRow("PATH", spec.path, last = true)

        Spacer(Modifier.height(18.dp))
        Column(Modifier.padding(horizontal = GUTTER)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                BasicText("THROUGHPUT", style = Type.Micro)
                BasicText(
                    "${spec.observedRate} / ${spec.peakRate} tok/s",
                    style = Type.Code,
                )
            }
            Spacer(Modifier.height(7.dp))
            Row(
                Modifier.fillMaxWidth().height(16.dp).border(2.dp, Ink.Ground),
            ) {
                val ratio = (spec.observedRate / spec.peakRate).toFloat().coerceIn(0f, 1f)
                Box(Modifier.weight(ratio).fillMaxWidth().background(Ink.Acid).height(16.dp))
                Box(Modifier.weight(1f - ratio).height(16.dp))
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

/** Label and value with a dotted leader between them. */
@Composable
private fun SpecRow(label: String, value: String, last: Boolean = false) {
    Column(Modifier.padding(horizontal = GUTTER)) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            BasicText(label, style = Type.Micro)
            BasicText(value, style = Type.Code.copy(fontSize = 12.sp))
        }
        if (!last) DottedRule()
    }
}

@Composable
private fun DottedRule() {
    Row(Modifier.fillMaxWidth().height(1.dp)) {
        repeat(60) {
            Box(Modifier.weight(1f).height(1.dp).background(Ink.Dotted))
            Box(Modifier.weight(1f).height(1.dp))
        }
    }
}
