package com.sidecar.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.sidecar.app.R

/**
 * Terminal-brutalist design tokens.
 *
 * Hard constraints — see CLAUDE.md. No gradients, no shadows, no blur,
 * no corner radii above 2dp, no icon libraries, no emoji.
 */
object Ink {
    val Ground = Color(0xFF0C0C0B)   // near-black, never pure #000
    val Paper  = Color(0xFFEDEAE0)   // warm off-white
    val Acid   = Color(0xFFD8FF3E)   // the one accent
    val Blood  = Color(0xFFFF3B1F)   // errors / the found bug

    val Dim    = Color(0xFF8A887F)   // secondary text (5.6:1 on Ground)
    val Faint  = Color(0xFF2A2A27)   // 1px rules, inactive borders
    val AddBg  = Color(0x1AD8FF3E)   // added line wash
    val DelBg  = Color(0x14FF3B1F)   // removed line wash
}

private val Mono  = FontFamily(Font(R.font.departure_mono))
private val Serif = FontFamily(Font(R.font.instrument_serif))

object Type {
    /** The one big statement line per screen. */
    val Display = TextStyle(
        fontFamily = Serif,
        fontSize = 62.sp,
        lineHeight = 58.sp,
        color = Ink.Paper,
    )

    /** Section headings — mono, wide-tracked, uppercase by convention. */
    val Label = TextStyle(
        fontFamily = Mono,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.6.sp,
        color = Ink.Dim,
    )

    val Body = TextStyle(
        fontFamily = Mono,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = Ink.Paper,
    )

    /** Diff and code. Tighter than body so hunks stay dense. */
    val Code = TextStyle(
        fontFamily = Mono,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        color = Ink.Paper,
    )

    val Gutter = TextStyle(
        fontFamily = Mono,
        fontSize = 10.sp,
        lineHeight = 17.sp,
        color = Ink.Dim,
    )

    /** Telemetry footer — small, dim, always truthful. */
    val Meta = TextStyle(
        fontFamily = Mono,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.8.sp,
        color = Ink.Dim,
        textDecoration = TextDecoration.None,
    )

    val Action = TextStyle(
        fontFamily = Mono,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 2.sp,
        color = Ink.Ground,
    )
}
