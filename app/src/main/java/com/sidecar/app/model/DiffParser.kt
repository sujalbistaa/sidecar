package com.sidecar.app.model

/**
 * Parses unified diff text (what `git diff --cached -U3` emits) into hunks.
 *
 * Deliberately small: it handles the shape git actually produces for a staged
 * change and ignores the rest. Binary files, renames without content changes
 * and mode-only changes yield no hunks, which is correct — there is nothing
 * for the model to review.
 */
object DiffParser {

    private val FILE_HEADER = Regex("^\\+\\+\\+ b/(.+)$")
    private val HUNK_HEADER = Regex("^@@ -\\d+(?:,\\d+)? \\+(\\d+)(?:,\\d+)? @@")

    fun parse(diff: String): List<Hunk> {
        val hunks = mutableListOf<Hunk>()
        var path: String? = null

        var lines = mutableListOf<DiffLine>()
        var newLineNo = 0
        var added = 0
        var removed = 0
        var open = false

        fun flush() {
            if (open && path != null && lines.isNotEmpty()) {
                hunks += Hunk(path!!, added, removed, lines.toList())
            }
            lines = mutableListOf()
            added = 0
            removed = 0
            open = false
        }

        for (raw in diff.lineSequence()) {
            val fileHeader = FILE_HEADER.find(raw)
            if (fileHeader != null) {
                flush()
                path = fileHeader.groupValues[1]
                continue
            }

            val hunkStart = HUNK_HEADER.find(raw)
            if (hunkStart != null) {
                flush()
                newLineNo = hunkStart.groupValues[1].toInt()
                open = true
                continue
            }

            if (!open) continue

            when {
                raw.startsWith("+++") || raw.startsWith("---") -> Unit

                raw.startsWith("+") -> {
                    lines += DiffLine(newLineNo, LineKind.ADD, raw.substring(1))
                    newLineNo++
                    added++
                }

                raw.startsWith("-") -> {
                    // Removed lines do not advance the new-file line counter.
                    lines += DiffLine(null, LineKind.DEL, raw.substring(1))
                    removed++
                }

                raw.startsWith(" ") -> {
                    lines += DiffLine(newLineNo, LineKind.CTX, raw.substring(1))
                    newLineNo++
                }

                // "\ No newline at end of file" and any trailing junk.
                else -> Unit
            }
        }
        flush()
        return hunks
    }
}
