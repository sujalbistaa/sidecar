package com.sidecar.app

import com.sidecar.app.model.DiffParser
import com.sidecar.app.model.LineKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DiffParserTest {

    private val staged = """
        diff --git a/auth/session.go b/auth/session.go
        index 1a2b3c4..5d6e7f8 100644
        --- a/auth/session.go
        +++ b/auth/session.go
        @@ -21,8 +21,10 @@ func (s *Store) Sweep(now int64) int {
         func (s *Store) Sweep(now int64) int {
         	purged := 0
        -	for i := 0; i < len(s.tokens); i++ {
        -		if s.tokens[i].exp < now {
        +	for i := 0; i <= len(s.tokens); i++ {
        +		t := s.tokens[i]
        +		if t.exp < now {
        +			s.drop(i)
         			purged++
         		}
         	}
    """.trimIndent()

    @Test
    fun `extracts path and counts`() {
        val hunks = DiffParser.parse(staged)
        assertEquals(1, hunks.size)
        assertEquals("auth/session.go", hunks[0].path)
        assertEquals(4, hunks[0].added)
        assertEquals(2, hunks[0].removed)
    }

    @Test
    fun `numbers lines against the new file`() {
        val lines = DiffParser.parse(staged)[0].lines
        // Two context lines open the hunk at 21 and 22.
        assertEquals(21, lines[0].number)
        assertEquals(22, lines[1].number)
        // Removals do not consume new-file line numbers.
        assertEquals(null, lines[2].number)
        assertEquals(null, lines[3].number)
        // Additions resume at 23.
        assertEquals(23, lines[4].number)
        assertEquals(26, lines[7].number)
        // Trailing context continues from there.
        assertEquals(27, lines[8].number)
    }

    @Test
    fun `preserves indentation`() {
        val add = DiffParser.parse(staged)[0].lines.first { it.kind == LineKind.ADD }
        assertTrue("leading tab must survive", add.text.startsWith("\t"))
        assertTrue(add.text.contains("i <= len(s.tokens)"))
    }

    @Test
    fun `header lines are never treated as content`() {
        val lines = DiffParser.parse(staged)[0].lines
        assertTrue(lines.none { it.text.startsWith("++ b/") })
        assertTrue(lines.none { it.text.contains("git a/") })
    }

    @Test
    fun `splits multiple files`() {
        val two = staged + "\n" + staged.replace("auth/session.go", "cache/lru.go")
        val hunks = DiffParser.parse(two)
        assertEquals(2, hunks.size)
        assertEquals("auth/session.go", hunks[0].path)
        assertEquals("cache/lru.go", hunks[1].path)
    }

    @Test
    fun `empty and binary diffs yield nothing`() {
        assertTrue(DiffParser.parse("").isEmpty())
        assertTrue(
            DiffParser.parse(
                "diff --git a/logo.png b/logo.png\nBinary files a/logo.png and b/logo.png differ",
            ).isEmpty(),
        )
    }
}
