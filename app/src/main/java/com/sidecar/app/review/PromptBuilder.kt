package com.sidecar.app.review

import com.sidecar.app.model.Hunk

/**
 * Builds the prompt handed to the on-device model.
 *
 * Kept deliberately narrow. A 1B model is unreliable at open-ended bug hunting
 * over long context, so the prompt gives it exactly one small hunk and asks for
 * a short, structured answer. See CLAUDE.md for why detection is meant to be
 * deterministic and the model's job is explanation plus patch.
 */
object PromptBuilder {

    /** Gemma 3 1B degrades badly past a few hundred tokens of code. */
    const val MAX_HUNK_LINES = 40

    fun build(hunk: Hunk): String {
        val body = hunk.lines
            .take(MAX_HUNK_LINES)
            .joinToString("\n") { it.marker + it.text }

        return """
            You are reviewing one hunk of a code change in ${hunk.path}.

            $body

            Answer in two short sections and nothing else:

            FINDING: one or two sentences naming the single most likely defect
            and why it fails. If the hunk looks correct, write "No defect found."

            PATCH: the corrected line only, prefixed with +. Omit this section
            entirely if there is no defect.
        """.trimIndent()
    }
}
