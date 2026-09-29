package com.sidecar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.sidecar.app.model.SampleDiff
import com.sidecar.app.review.FakeEngine
import com.sidecar.app.review.ReviewController
import com.sidecar.app.ui.ReviewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val controller = remember {
                // Swapped for GemmaEngine once the device is in hand. Until then
                // the telemetry footer says "fake" rather than naming a model
                // that is not running.
                ReviewController(
                    engine = FakeEngine(SampleDiff.finding),
                    scope = scope,
                    hunk = SampleDiff.hunk,
                )
            }
            ReviewScreen(state = controller.state, onRun = controller::run)
        }
    }
}
