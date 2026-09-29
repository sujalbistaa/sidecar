package com.sidecar.app.model

enum class LineKind { CTX, ADD, DEL }

data class DiffLine(
    val number: Int?,
    val kind: LineKind,
    val text: String,
) {
    val marker: String
        get() = when (kind) {
            LineKind.ADD -> "+"
            LineKind.DEL -> "-"
            LineKind.CTX -> " "
        }
}

data class Hunk(
    val path: String,
    val added: Int,
    val removed: Int,
    val lines: List<DiffLine>,
) {
    /** The hunk as plain unified-diff text, which is what the model sees. */
    fun asPatchText(): String =
        lines.joinToString("\n") { it.marker + it.text }
}

data class ReviewState(
    val hunk: Hunk,
    val output: String = "",
    val running: Boolean = false,
    val offline: Boolean = true,
    val tokensPerSec: Double = 0.0,
    val bytesSent: Int = 0,
    val model: String = "gemma3-1b-it · int4",
)

/**
 * Stub hunk used for previews and until the adb bridge lands on D3.
 * The bug is planted deliberately: `<=` walks one past the end of the slice.
 */
object SampleDiff {
    val hunk = Hunk(
        path = "auth/session.go",
        added = 4,
        removed = 2,
        lines = listOf(
            DiffLine(21, LineKind.CTX, "func (s *Store) Sweep(now int64) int {"),
            DiffLine(22, LineKind.CTX, "\tpurged := 0"),
            DiffLine(23, LineKind.DEL, "\tfor i := 0; i < len(s.tokens); i++ {"),
            DiffLine(24, LineKind.DEL, "\t\tif s.tokens[i].exp < now {"),
            DiffLine(23, LineKind.ADD, "\tfor i := 0; i <= len(s.tokens); i++ {"),
            DiffLine(24, LineKind.ADD, "\t\tt := s.tokens[i]"),
            DiffLine(25, LineKind.ADD, "\t\tif t.exp < now {"),
            DiffLine(26, LineKind.ADD, "\t\t\ts.drop(i)"),
            DiffLine(27, LineKind.CTX, "\t\t\tpurged++"),
            DiffLine(28, LineKind.CTX, "\t\t}"),
            DiffLine(29, LineKind.CTX, "\t}"),
            DiffLine(30, LineKind.CTX, "\treturn purged"),
        ),
    )

    val finding = """
        Off-by-one on line 23. `i <= len(s.tokens)` runs one iteration past
        the final index, so `s.tokens[i]` panics with index out of range on
        every non-empty sweep.

        Use `i < len(s.tokens)`.
    """.trimIndent()
}
