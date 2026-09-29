package com.sidecar.app.review

import kotlinx.coroutines.flow.Flow

/** One token emitted by a model, plus the running rate at that moment. */
data class Token(val text: String, val index: Int, val elapsedMs: Long) {
    val tokensPerSec: Double
        get() = if (elapsedMs <= 0) 0.0 else (index + 1) * 1000.0 / elapsedMs
}

/**
 * A source of streamed review text.
 *
 * Two implementations: [FakeEngine] runs anywhere and is what the UI and its
 * tests are built against; GemmaEngine wraps MediaPipe and only runs on a real
 * device. Swapping one for the other is the whole of phone day.
 */
interface ReviewEngine {
    val label: String
    fun stream(prompt: String): Flow<Token>
    fun close() {}
}
