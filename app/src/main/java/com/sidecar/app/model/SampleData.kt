package com.sidecar.app.model

/** One past review, as the LOG tab lists it. */
data class ReviewRecord(
    val path: String,
    val verdict: String,
    val clean: Boolean,
    val ago: String,
    val rate: String,
)

data class ModelSpec(
    val name: String,
    val quantisation: String,
    val fileSize: String,
    val runtime: String,
    val backend: String,
    val context: String,
    val path: String,
    val observedRate: Double,
    val peakRate: Double,
)

data class SessionStats(
    val bytesSent: Int,
    val diffsReviewed: Int,
    val defectsCaught: Int,
    val tokensGenerated: String,
    val averageRate: String,
    val since: String,
)

object SampleData {

    val log = listOf(
        ReviewRecord("auth/session.go", "OFF-BY-ONE", false, "2M AGO", "18.4 TOK/S"),
        ReviewRecord("cache/lru.go", "CLEAN", true, "14M AGO", "21.0 TOK/S"),
        ReviewRecord("api/handler.go", "NIL DEREF", false, "1H AGO", "17.2 TOK/S"),
        ReviewRecord("store/migrate.go", "CLEAN", true, "1H AGO", "19.6 TOK/S"),
        ReviewRecord("auth/token.go", "UNCHECKED ERR", false, "3H AGO", "18.9 TOK/S"),
    )

    val model = ModelSpec(
        name = "gemma-3-1b-it",
        quantisation = "int4",
        fileSize = "555 MB",
        runtime = "MediaPipe LLM",
        backend = "GPU",
        context = "1024 tok",
        path = "/data/local/tmp/llm",
        observedRate = 18.4,
        peakRate = 24.0,
    )

    val session = SessionStats(
        bytesSent = 0,
        diffsReviewed = 17,
        defectsCaught = 5,
        tokensGenerated = "12.4k",
        averageRate = "19.1",
        since = "SINCE 09:14",
    )
}
