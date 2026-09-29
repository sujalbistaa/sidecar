package com.sidecar.app.review

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Emits a canned review at a plausible rate so the UI, the streaming state
 * machine and the tok/s readout can be exercised without a device.
 *
 * Never shipped in the demo build — the telemetry footer must be truthful.
 */
class FakeEngine(
    private val response: String,
    private val msPerToken: Long = 55,
) : ReviewEngine {

    override val label = "fake"

    override fun stream(prompt: String): Flow<Token> = flow {
        val started = System.currentTimeMillis()
        // Split on whitespace boundaries so the output reads like real decoding.
        val pieces = Regex("(\\S+\\s*)").findAll(response).map { it.value }.toList()
        pieces.forEachIndexed { i, piece ->
            delay(msPerToken)
            emit(Token(piece, i, System.currentTimeMillis() - started))
        }
    }
}
