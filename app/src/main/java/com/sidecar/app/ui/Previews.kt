package com.sidecar.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sidecar.app.model.ReviewState
import com.sidecar.app.model.SampleData
import com.sidecar.app.model.SampleDiff

/**
 * Device-free previews. Open any of these in Android Studio and hit Split —
 * MediaPipe never loads here, so the whole UI is inspectable without hardware.
 */
private const val W = 390
private const val H = 844

@Preview(name = "1 · Review", widthDp = W, heightDp = H, showBackground = true)
@Composable
private fun PreviewReview() {
    Screen(current = Tab.REVIEW, onSelect = {}) {
        ReviewScreen(state = ReviewState(hunk = SampleDiff.hunk), onRun = {})
    }
}

@Preview(name = "2 · Finding", widthDp = W, heightDp = H, showBackground = true)
@Composable
private fun PreviewFinding() {
    Screen(current = Tab.REVIEW, onSelect = {}) {
        FindingScreen(
            state = ReviewState(
                hunk = SampleDiff.hunk,
                output = SampleDiff.finding,
                tokensPerSec = 18.4,
                elapsedMs = 2100,
            ),
            onBack = {},
            onApply = {},
        )
    }
}

@Preview(name = "3 · Log", widthDp = W, heightDp = H, showBackground = true)
@Composable
private fun PreviewLog() {
    Screen(current = Tab.LOG, onSelect = {}) {
        LogScreen(SampleData.log) {}
    }
}

@Preview(name = "4 · Model", widthDp = W, heightDp = H, showBackground = true)
@Composable
private fun PreviewModel() {
    Screen(current = Tab.MODEL, onSelect = {}) {
        ModelScreen(SampleData.model, loaded = true)
    }
}

@Preview(name = "5 · Session", widthDp = W, heightDp = H, showBackground = true)
@Composable
private fun PreviewSession() {
    Screen(current = Tab.SESSION, onSelect = {}) {
        SessionScreen(SampleData.session)
    }
}
