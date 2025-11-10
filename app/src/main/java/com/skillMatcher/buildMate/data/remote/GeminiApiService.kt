package com.skillMatcher.buildMate.data.remote


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    // Using API key as query param for Google GenerativeLanguage API
    // Using gemini-2.5-flash - the latest available model (gemini-1.5-flash returned 404)
    @POST("v1/models/gemini-2.5-flash:generateContent")
    suspend fun generateIdeas(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}