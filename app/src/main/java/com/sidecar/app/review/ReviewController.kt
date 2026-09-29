package com.sidecar.app.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sidecar.app.model.Hunk
import com.sidecar.app.model.ReviewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Holds the screen's state and drives one review at a time.
 *
 * The engine is injected so the whole flow runs against [FakeEngine] off-device
 * and against MediaPipe on-device without the UI knowing which it has.
 */
class ReviewController(
    private val engine: ReviewEngine,
    private val scope: CoroutineScope,
    hunk: Hunk,
) {
    var state by mutableStateOf(ReviewState(hunk = hunk, model = engine.label))
        private set

    fun run() {
        if (state.running) return
        state = state.copy(running = true, output = "", tokensPerSec = 0.0)

        scope.launch {
            val buffer = StringBuilder()
            try {
                engine.stream(PromptBuilder.build(state.hunk)).collect { token ->
                    buffer.append(token.text)
                    state = state.copy(
                        output = buffer.toString(),
                        tokensPerSec = token.tokensPerSec,
                    )
                }
            } catch (t: Throwable) {
                // Surface failures in the UI rather than dying silently — on
                // device this is where a missing model file shows up.
                state = state.copy(output = "engine error: ${t.message ?: t::class.simpleName}")
            } finally {
                state = state.copy(running = false)
            }
        }
    }
}
