package com.sidecar.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sidecar.app.R

/**
 * Design tokens — see CLAUDE.md for the constraints these encode.
 *
 * Paper ground, ink type, one acid accent. Acid is NEVER used as a text colour:
 * on paper it fails contrast badly, so it only ever appears as a fill with ink
 * set on top. Same for blood, which is a wash and a mark, never small type.
 */
object Ink {
    val Paper = Color(0xFFEDEAE0)          // warm off-white ground
    val Ground = Color(0xFF111110)         // near-black — never pure #000
    val Acid = Color(0xFFD8FF3E)           // fills only, ink on top
    val Blood = Color(0xFFFF3B1F)          // marks and washes only

    val Smoke = Color(0xFF5C5B54)          // secondary text, 5.2:1 on Paper
    val Hairline = Color(0x2E111110)       // row dividers
    val Dotted = Color(0x59111110)         // spec-sheet leaders

    val AddWash = Color(0x8CD8FF3E)        // added diff lines
    val DelWash = Color(0x24FF3B1F)        // removed diff lines
    val Chip = Color(0x38FF3B1F)           // defect chips
}

object Fonts {
    val Mono = FontFamily(Font(R.font.departure_mono))
    val Serif = FontFamily(Font(R.font.instrument_serif))
}

object Type {

    /** Oversized serif numerals and verdicts. Size varies per screen. */
    fun display(size: TextUnit, line: TextUnit = size * 0.8f, track: TextUnit = (-1).sp) = TextStyle(
        fontFamily = Fonts.Serif,
        fontSize = size,
        lineHeight = line,
        letterSpacing = track,
        color = Ink.Ground,
    )

    /** 9sp tracked mono — the micro-labels doing most of the structural work. */
    val Micro = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 9.sp,
        lineHeight = 13.sp,
        letterSpacing = 1.6.sp,
        color = Ink.Smoke,
    )

    val MicroInk = Micro.copy(color = Ink.Ground)

    val Wordmark = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 2.sp,
        color = Ink.Ground,
    )

    val Body = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = Ink.Ground,
    )

    val Code = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 11.sp,
        lineHeight = 17.sp,
        color = Ink.Ground,
    )

    val Gutter = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 9.sp,
        lineHeight = 17.sp,
        color = Ink.Smoke,
    )

    val Action = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 2.4.sp,
        color = Ink.Ground,
    )

    val Tab = TextStyle(
        fontFamily = Fonts.Mono,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.6.sp,
    )
}
