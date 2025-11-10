package com.skillMatcher.buildMate.data.di


import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skillMatcher.buildMate.data.remote.GeminiApiService
import com.skillMatcher.buildMate.path.Gemini_Api_Key
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            // Set extended timeouts for Gemini API which can be slow
            .connectTimeout(30, TimeUnit.SECONDS)      // Connection timeout: 30s
            .readTimeout(60, TimeUnit.SECONDS)         // Read timeout: 60s (for AI responses)
            .writeTimeout(30, TimeUnit.SECONDS)        // Write timeout: 30s
            .callTimeout(120, TimeUnit.SECONDS)        // Total call timeout: 2 minutes
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("NetworkModule", "Full URL: ${request.url}")
                Log.d("NetworkModule", "Endpoint: ${request.url.pathSegments}")
                val startTime = System.nanoTime()
                try {
                    val response = chain.proceed(request)
                    val duration = System.nanoTime() - startTime
                    Log.d("NetworkModule", "Response Code: ${response.code} | Duration: ${duration / 1e6}ms")
                    response
                } catch (e: Exception) {
                    val duration = System.nanoTime() - startTime
                    Log.e("NetworkModule", "Request failed after ${duration / 1e6}ms: ${e.message}")
                    throw e
                }
            }
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideGeminiApi(retrofit: Retrofit): GeminiApiService =
        retrofit.create(GeminiApiService::class.java)

    // Provide API key via Hilt - replace with BuildConfig or safer store in prod
    @Provides
    @Singleton
    @Named("GEMINI_API_KEY")
    fun provideGeminiApiKey(): String = Gemini_Api_Key
}
