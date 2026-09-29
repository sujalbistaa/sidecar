package com.sidecar.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sidecar.app.model.SampleData
import com.sidecar.app.model.SessionStats
import com.sidecar.app.review.ReviewController
import com.sidecar.app.ui.theme.Ink

/**
 * Root. Four tabs plus one drill-down (REVIEW -> finding), held in plain state
 * rather than a navigation graph.
 */
@Composable
fun SidecarApp(controller: ReviewController) {
    var tab by remember { mutableStateOf(Tab.REVIEW) }
    var showFinding by remember { mutableStateOf(false) }

    val stats: SessionStats = SampleData.session.copy(bytesSent = controller.state.bytesSent)

    Column(Modifier.fillMaxSize().background(Ink.Paper)) {
        Screen(current = tab, onSelect = { tab = it; showFinding = false }) {
            when (tab) {
                Tab.REVIEW ->
                    if (showFinding) {
                        FindingScreen(
                            state = controller.state,
                            onBack = { showFinding = false },
                            onApply = { showFinding = false },
                        )
                    } else {
                        ReviewScreen(
                            state = controller.state,
                            onRun = {
                                controller.run()
                                showFinding = true
                            },
                        )
                    }

                Tab.LOG -> LogScreen(SampleData.log) { showFinding = true; tab = Tab.REVIEW }
                Tab.MODEL -> ModelScreen(SampleData.model, loaded = controller.modelLoaded)
                Tab.SESSION -> SessionScreen(stats)
            }
        }
    }
}
