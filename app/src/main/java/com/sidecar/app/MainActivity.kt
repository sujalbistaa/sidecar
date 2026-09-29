package com.sidecar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sidecar.app.model.ReviewState
import com.sidecar.app.model.SampleDiff
import com.sidecar.app.ui.ReviewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Stub wiring until MediaPipe lands on D1-with-device.
            var state by remember { mutableStateOf(ReviewState(hunk = SampleDiff.hunk)) }
            ReviewScreen(
                state = state,
                onRun = {
                    state = state.copy(
                        output = SampleDiff.finding,
                        tokensPerSec = 18.4,
                    )
                },
            )
        }
    }
}
