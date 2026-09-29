package com.sidecar.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidecar.app.ui.theme.Ink
import com.sidecar.app.ui.theme.Type

val GUTTER = 20.dp

/** Wordmark plus a status badge. Acid fill, ink text — never acid lettering. */
@Composable
fun Masthead(badge: String, showDot: Boolean = true, lead: String = "SIDECAR") {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = GUTTER),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(lead, style = Type.Wordmark)
        Row(
            Modifier.background(Ink.Acid).padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showDot) {
                Box(Modifier.size(5.dp).background(Ink.Ground))
                Spacer(Modifier.width(6.dp))
            }
            BasicText(badge, style = Type.MicroInk)
        }
    }
}

/** Oversized serif numeral with a two-line mono caption sitting on its baseline. */
@Composable
fun BigStat(value: String, caption: String, size: TextUnit) {
    Row(
        Modifier.padding(horizontal = GUTTER),
        verticalAlignment = Alignment.Bottom,
    ) {
        BasicText(value, style = Type.display(size, track = (-4).sp))
        Spacer(Modifier.width(14.dp))
        BasicText(
            caption,
            style = Type.Micro.copy(lineHeight = 15.sp),
            modifier = Modifier.padding(bottom = 12.dp),
        )
    }
}

@Composable
fun HardRule(thickness: Dp = 2.dp) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = GUTTER)
            .height(thickness)
            .background(Ink.Ground),
    )
}

@Composable
fun Hairline() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(Ink.Hairline))
}

/** A hard rectangle behind text. Chips are never pills — zero radius throughout. */
@Composable
fun Chip(label: String, fill: Color) {
    Box(Modifier.background(fill).padding(horizontal = 5.dp, vertical = 2.dp)) {
        BasicText(label, style = Type.Micro.copy(color = Ink.Ground, letterSpacing = 1.2.sp))
    }
}

/** Full-bleed acid button with a 2dp ink border. */
@Composable
fun AcidAction(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = GUTTER)
            .background(if (enabled) Ink.Acid else Ink.Hairline)
            .border(2.dp, Ink.Ground)
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(label, style = Type.Action)
    }
}

/**
 * The byte counter running above the tab bar on every screen. It is decoration
 * that happens to be true, which is the point.
 */
@Composable
fun Ticker(text: String = "0 BYTES SENT") {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Ink.Ground)
            .height(2.dp),
    )
    Box(
        Modifier
            .fillMaxWidth()
            .clipToBounds()
            .padding(vertical = 6.dp),
    ) {
        BasicText(
            text = List(8) { text }.joinToString("  ·  "),
            style = Type.Micro.copy(letterSpacing = 2.sp),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.padding(start = GUTTER),
        )
    }
    Hairline()
}

@Composable
fun TabBar(current: Tab, onSelect: (Tab) -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Tab.entries.forEach { tab ->
            val active = tab == current
            Box(
                Modifier
                    .weight(1f)
                    .background(if (active) Ink.Ground else Color.Transparent)
                    .clickable { onSelect(tab) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    tab.label,
                    style = Type.Tab.copy(color = if (active) Ink.Paper else Ink.Smoke),
                )
            }
        }
    }
}

/** Page scaffold: paper ground, content, ticker, tabs. */
@Composable
fun Screen(
    current: Tab,
    onSelect: (Tab) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxWidth().background(Ink.Paper)) {
        Spacer(Modifier.height(52.dp))
        Column(Modifier.weight(1f)) { content() }
        Ticker()
        TabBar(current, onSelect)
    }
}

/** Marker-pen swipe behind a word — a hard band, not a gradient wash. */
fun Modifier.highlight(color: Color = Ink.Acid, coverage: Float = 0.52f): Modifier =
    drawBehind {
        drawRect(
            color = color,
            topLeft = Offset(0f, size.height * (1f - coverage) - size.height * 0.08f),
            size = Size(size.width, size.height * coverage),
        )
    }
