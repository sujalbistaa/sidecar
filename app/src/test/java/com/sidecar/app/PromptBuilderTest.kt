package com.sidecar.app

import com.sidecar.app.model.SampleDiff
import com.sidecar.app.review.PromptBuilder
import org.junit.Assert.assertTrue
import org.junit.Test

class PromptBuilderTest {

    @Test
    fun `carries the hunk and the file path`() {
        val p = PromptBuilder.build(SampleDiff.hunk)
        assertTrue(p.contains("auth/session.go"))
        assertTrue(p.contains("i <= len(s.tokens)"))
    }

    @Test
    fun `asks for both sections`() {
        val p = PromptBuilder.build(SampleDiff.hunk)
        assertTrue(p.contains("FINDING:"))
        assertTrue(p.contains("PATCH:"))
    }

    @Test
    fun `caps hunk length so context stays small`() {
        val long = SampleDiff.hunk.copy(
            lines = List(200) { SampleDiff.hunk.lines[0] },
        )
        val bodyLines = PromptBuilder.build(long).lines().count { it.startsWith(" func") }
        assertTrue("must truncate to $bodyLines", bodyLines <= PromptBuilder.MAX_HUNK_LINES)
    }
}
