package com.sidecar.app.review

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

/**
 * On-device inference via MediaPipe's LLM Inference API.
 *
 * The model is pushed with adb to [MODEL_PATH] rather than bundled — see
 * CLAUDE.md. /data/local/tmp is traversable by apps, which is why Google's own
 * guide uses it for development.
 *
 * Not verified on hardware yet. First run on the device is where a wrong path,
 * an unsupported backend or an OOM will surface.
 *
 * LlmInference is deprecated in 0.10.35 in favour of LiteRT-LM, which is at
 * 0.0.0-alpha05 and cannot be tested here. Deprecated is not broken, and this
 * is still the API Google's own Android guide uses. Staying put.
 */
@Suppress("DEPRECATION")
class GemmaEngine(
    context: Context,
    modelPath: String = MODEL_PATH,
) : ReviewEngine {

    companion object {
        const val MODEL_PATH = "/data/local/tmp/llm/model.task"
        fun isInstalled(path: String = MODEL_PATH) = File(path).exists()
    }

    override val label = "gemma3-1b-it · int4"

    private val engine: LlmInference = LlmInference.createFromOptions(
        context,
        LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(1024)
            .build(),
    )

    override fun stream(prompt: String): Flow<Token> = callbackFlow {
        val session = LlmInferenceSession.createFromOptions(
            engine,
            LlmInferenceSession.LlmInferenceSessionOptions.builder()
                .setTopK(40)
                // Low but non-zero: review text should be steady, not creative.
                .setTemperature(0.2f)
                .build(),
        )

        val started = System.currentTimeMillis()
        var index = 0

        session.addQueryChunk(prompt)
        session.generateResponseAsync { partial, done ->
            trySend(Token(partial, index, System.currentTimeMillis() - started))
            index++
            if (done) close()
        }

        awaitClose { session.close() }
    }

    override fun close() = engine.close()
}
