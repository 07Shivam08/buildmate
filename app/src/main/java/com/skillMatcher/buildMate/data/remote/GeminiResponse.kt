package com.skillMatcher.buildMate.data.remote

data class GeminiResponse(
    val candidates: List<Candidate>?,
    val promptFeedback: PromptFeedback?
)

data class Candidate(
    val content: Content?,
    val finishReason: String?,
    val index: Int?
)

data class Content(
    val parts: List<Part>?,
    val role: String?
)

data class Part(
    val text: String?
)

data class PromptFeedback(
    val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    val category: String?,
    val probability: String?
)
