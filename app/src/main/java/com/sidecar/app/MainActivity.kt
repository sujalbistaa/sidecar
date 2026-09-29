package com.sidecar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.sidecar.app.model.SampleDiff
import com.sidecar.app.review.FakeEngine
import com.sidecar.app.review.GemmaEngine
import com.sidecar.app.review.ReviewController
import com.sidecar.app.ui.SidecarApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val controller = remember {
                // Use the real model when it has been pushed to the device,
                // otherwise fall back so every screen still works. The telemetry
                // footer names whichever engine is actually running.
                val loaded = GemmaEngine.isInstalled()
                ReviewController(
                    engine = if (loaded) {
                        GemmaEngine(applicationContext)
                    } else {
                        FakeEngine(SampleDiff.finding)
                    },
                    scope = scope,
                    hunk = SampleDiff.hunk,
                    modelLoaded = loaded,
                )
            }
            SidecarApp(controller)
        }
    }
}
