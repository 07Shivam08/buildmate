package com.skillMatcher.buildMate.data.repoImplementation

import android.util.Log
import com.skillMatcher.buildMate.data.dao.IdeaDao
import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.data.remote.GeminiApiService
import com.skillMatcher.buildMate.data.remote.GeminiContent
import com.skillMatcher.buildMate.data.remote.GeminiPart
import com.skillMatcher.buildMate.data.remote.GeminiRequest
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import com.skillMatcher.buildMate.utils.ResultState
import javax.inject.Inject
import javax.inject.Named

class IdeaRepositoryImpl @Inject constructor(
    private val geminiApi: GeminiApiService,
    private val ideaDao: IdeaDao,
    @Named("GEMINI_API_KEY") private val apiKey: String
) : IdeaRepository
{

    companion object {
        private const val TAG = "IdeaRepositoryImpl"
    }

    override suspend fun generateIdeasFromSkill(skill: UserSkillEntity): ResultState<List<IdeaEntity>> {
        return try {
            // Validate input
            if (skill.techStack.isBlank()) {
                Log.e(TAG, "Tech stack is empty")
                return ResultState.Error("Tech stack cannot be empty. Please enter your main technology.")
            }

            if (skill.goal.isBlank()) {
                Log.e(TAG, "Goal is empty")
                return ResultState.Error("Goal cannot be empty. Please describe what you want to build.")
            }

            Log.d(TAG, "Building prompt for skill: ${skill.skillId}")
            val prompt = buildPrompt(skill)
            
            Log.d(TAG, "Calling Gemini API...")
            Log.d(TAG, "API Key: ${apiKey.take(10)}... (truncated)")
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(prompt))
                    )
                )
            )
            
            Log.d(TAG, "Request ready, calling: v1/models/gemini-1.5-flash:generateContent")
            val response = try {
                geminiApi.generateIdeas(apiKey, request)
            } catch (e: Exception) {
                Log.e(TAG, "Network error calling Gemini API: ${e.message}", e)
                return when (e) {
                    is java.net.SocketTimeoutException -> {
                        ResultState.Error("Connection timeout. Please check your internet and try again.")
                    }

                    is java.net.ConnectException -> {
                        ResultState.Error("Cannot connect to server. Please check your internet connection.")
                    }

                    is java.io.IOException -> {
                        ResultState.Error("Network error: ${e.message ?: "Unable to connect"}")
                    }

                    else -> {
                        ResultState.Error("Network error: ${e.message ?: "Unknown error"}")
                    }
                }
            }

            Log.d(TAG, "Response received with code: ${response.code()}")
            // Check response success
            if (!response.isSuccessful) {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid request. Please check your input and try again."
                    401, 403 -> "API authentication failed. Please check your API key configuration."
                    404 -> "API endpoint not found. Please check the endpoint configuration."
                    429 -> "API rate limit exceeded. Please wait a moment and try again."
                    500, 502, 503 -> "Server error. Please try again in a moment."
                    else -> "API error: ${response.code()} ${response.message()}"
                }
                Log.e(TAG, "API returned error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                return ResultState.Error(errorMessage)
            }

            // Parse response body
            val responseBody = response.body()
            if (responseBody == null) {
                Log.e(TAG, "API returned null response body")
                return ResultState.Error("Empty response from server. Please try again.")
            }

            // Extract text from response
            val text = responseBody.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

            if (text.isNullOrBlank()) {
                Log.e(TAG, "No text content in API response")
                return ResultState.Error("No ideas generated. Please try again with different inputs.")
            }

            Log.d(TAG, "Successfully received API response. Parsing ideas...")
            Log.d(TAG, "Raw response: $text")

            // Parse into IdeaEntity list
            val ideas = parseGeminiIdeasText(text, userId = skill.userId, skillId = skill.skillId)
            
            if (ideas.isEmpty()) {
                Log.e(TAG, "Failed to parse any ideas from response")
                return ResultState.Error("Could not parse ideas from response. Please try again.")
            }

            Log.d(TAG, "Successfully generated ${ideas.size} ideas")
            ResultState.Success(ideas)

        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in generateIdeasFromSkill", e)
            val errorMessage = when (e) {
                is IllegalStateException -> "Invalid configuration. Please contact support."
                is NullPointerException -> "Data parsing error. Please try again."
                else -> e.message ?: "An unexpected error occurred"
            }
            ResultState.Error(errorMessage)
        }
    }

    override suspend fun saveIdea(idea: IdeaEntity): Long {
        return try {
            Log.d(TAG, "Saving idea: ${idea.ideaTitle}")
            val rowId = ideaDao.insertIdea(idea)
            Log.d(TAG, "Idea saved successfully with ID: $rowId")
            rowId
        } catch (e: Exception) {
            Log.e(TAG, "Error saving idea", e)
            throw e
        }
    }

    override suspend fun getAllIdeas(userId: String): List<IdeaEntity> {
        return try {
            Log.d(TAG, "Fetching all ideas for user: $userId")
            val ideas = ideaDao.getAllIdeas(userId)
            Log.d(TAG, "Retrieved ${ideas.size} ideas")
            ideas
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching ideas", e)
            emptyList()
        }
    }

    override suspend fun getIdeasByIdeaId(userId: String, ideaId: Int): List<IdeaEntity> {
        return try {
            Log.d(TAG, "Fetching idea for user: $userId with ideaId: $ideaId")
            val ideas = ideaDao.getIdeasByIdeaId(userId, ideaId)
            Log.d(TAG, "Retrieved ${ideas.size} idea(s)")
            ideas
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching idea by ideaId", e)
            emptyList()
        }
    }

    // Build the prompt instructing Gemini to return structured text
    private fun buildPrompt(skill: UserSkillEntity): String {
        return """
            You are an expert Software developer who suggests innovative project ideas for learners.
            
            User Profile:
            - Tech Stack: ${skill.techStack}
            - Libraries/Tools Known: ${if (skill.libraries.isNotEmpty()) skill.libraries.joinToString(", ") else "None specified"}
            - Experience Level: ${skill.experienceLevel}
            - Learning Goal: ${skill.goal} even if learning goal is not mention you give them a learning goal according to their skills which he mentioned.
            - Additional Context: ${skill.additionalNotes ?: "None"}
            
            Your task: Generate exactly 1 unique, practical, and engaging any software project like android app, cross platform app , website , api , webpages, Animations , automations , use in hardware devices etc. idea that:
            1. Matches their experience level (${skill.experienceLevel})
            2. Uses their preferred tech stack and libraries
            3. Helps them achieve their learning goal
            
            Output EXACTLY this format (with these exact labels):
            Title: [Creative app name]
            Description: [2-3 sentences explaining what the app does and why it's useful]
            Difficulty: [Easy/Intermediate/Hard]
            TechUsed: [List the specific libraries, frameworks, and technologies from their stack that would be used]
            LearningFocus: [The key skills and concepts they'll learn from building this project]
            
            Make the idea practical, achievable, and perfectly aligned with their stated goal.
        """.trimIndent()
    }

    // Robust parser converting Gemini text to IdeaEntity list
    private fun parseGeminiIdeasText(rawText: String, userId: String, skillId: Int): List<IdeaEntity> {
        if (rawText.isBlank()) {
            Log.w(TAG, "Raw text is blank, returning empty list")
            return emptyList()
        }

        Log.d(TAG, "Parsing ideas from text (length: ${rawText.length})")
        
        // Split ideas by blank lines
        val blocks = rawText.split(Regex("\\n\\s*\\n+"))
        Log.d(TAG, "Found ${blocks.size} potential idea blocks")
        
        val result = mutableListOf<IdeaEntity>()

        for ((index, block) in blocks.withIndex()) {
            try {
                val lines = block.lines()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                
                if (lines.isEmpty()) {
                    Log.d(TAG, "Skipping empty block $index")
                    continue
                }

                Log.d(TAG, "Processing block $index with ${lines.size} lines")

                var title = ""
                var description = ""
                var difficulty = ""
                var techUsed = ""
                var learningFocus = ""

                // Parse each field
                for (line in lines) {
                    when {
                        line.startsWith("Title:", ignoreCase = true) -> {
                            title = line.substringAfter(":").trim()
                            Log.d(TAG, "Found title: $title")
                        }
                        line.startsWith("Description:", ignoreCase = true) -> {
                            description = line.substringAfter(":").trim()
                            Log.d(TAG, "Found description: ${description.take(50)}...")
                        }
                        line.startsWith("Difficulty:", ignoreCase = true) -> {
                            difficulty = line.substringAfter(":").trim()
                            Log.d(TAG, "Found difficulty: $difficulty")
                        }
                        line.startsWith("TechUsed:", ignoreCase = true) || 
                        line.startsWith("Tech Used:", ignoreCase = true) -> {
                            techUsed = line.substringAfter(":").trim()
                            Log.d(TAG, "Found tech used: $techUsed")
                        }
                        line.startsWith("LearningFocus:", ignoreCase = true) ||
                        line.startsWith("Learning Focus:", ignoreCase = true) -> {
                            learningFocus = line.substringAfter(":").trim()
                            Log.d(TAG, "Found learning focus: ${learningFocus.take(50)}...")
                        }
                    }
                }

                // Validate and create entity
                if (title.isBlank()) {
                    Log.w(TAG, "Block $index has no title, using first line as fallback")
                    title = lines.firstOrNull() ?: "Untitled Idea"
                }

                if (description.isBlank()) {
                    Log.w(TAG, "Block $index has no description, using second line as fallback")
                    description = lines.getOrNull(1) ?: "No description available"
                }

                // Validate difficulty
                val validDifficulty = when {
                    difficulty.contains("Easy", ignoreCase = true) -> "Easy"
                    difficulty.contains("Intermediate", ignoreCase = true) -> "Intermediate"
                    difficulty.contains("Hard", ignoreCase = true) -> "Hard"
                    else -> {
                        Log.w(TAG, "Invalid difficulty '$difficulty', defaulting to Intermediate")
                        "Intermediate"
                    }
                }

                val idea = IdeaEntity(
                    userId = userId,
                    skillId = skillId,
                    ideaTitle = title,
                    description = description,
                    difficulty = validDifficulty,
                    techUsed = techUsed.ifBlank { "Not specified" },
                    learningFocus = learningFocus.ifBlank { "General Android development" }
                )

                Log.d(TAG, "Successfully parsed idea $index: ${idea.ideaTitle}")
                result.add(idea)

            } catch (e: Exception) {
                Log.e(TAG, "Error parsing block $index", e)
                // Continue to next block instead of failing
            }
        }

        Log.d(TAG, "Successfully parsed ${result.size} ideas total")
        return result
    }
}
