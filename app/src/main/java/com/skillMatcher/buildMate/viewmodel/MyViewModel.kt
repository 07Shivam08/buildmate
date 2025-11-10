package com.skillMatcher.buildMate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillMatcher.buildMate.data.dao.UserSkillDao
import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.domain.usecases.CreateUserUseCase
import com.skillMatcher.buildMate.domain.usecases.DeleteAccountUseCase
import com.skillMatcher.buildMate.domain.usecases.FetchUserDataUseCase
import com.skillMatcher.buildMate.domain.usecases.FetchUserNameUseCase
import com.skillMatcher.buildMate.domain.usecases.GenerateIdeaUseCase
import com.skillMatcher.buildMate.domain.usecases.LoginUserUseCase
import com.skillMatcher.buildMate.domain.usecases.SignOutUseCase
import com.skillMatcher.buildMate.domain.usecases.UpdateUserUseCase
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import com.skillMatcher.buildMate.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserNameUseCase: FetchUserNameUseCase,
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val generateIdeaUseCase: GenerateIdeaUseCase,
    private val userSkillDao: UserSkillDao,
    private val ideaRepository: IdeaRepository
) : ViewModel() {


    private val _createUserState = MutableStateFlow(CreateUserState())
    val createUserState = _createUserState.asStateFlow()

    fun createUser(userData: UserDataModels) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.createUserUseCase(userData).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _createUserState.value = CreateUserState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _createUserState.value = CreateUserState(isSuccess = it.data)
                    }

                    is ResultState.Error -> {
                        _createUserState.value = CreateUserState(isError = it.message)
                    }
                }
            }

        }
    }

    private val _loginUserState = MutableStateFlow(LoginUserState())
    val loginUserState = _loginUserState.asStateFlow()


    fun loginUser(userData: UserDataModels) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUserUseCase.loginUserUseCase(userData).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _loginUserState.value = LoginUserState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _loginUserState.value = LoginUserState(isSuccess = true)
                    }

                    is ResultState.Error -> {
                        _loginUserState.value = LoginUserState(isError = it.message)
                    }
                }
            }

        }

    }

    private val _updateUserState = MutableStateFlow(UpdateUserState())
    val updateUserState = _updateUserState.asStateFlow()

    fun updateUser(userData: UserDataModels) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserUseCase.updateUserUseCase(userData).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _updateUserState.value = UpdateUserState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _updateUserState.value = UpdateUserState(isSuccess = it.data)
                    }

                    is ResultState.Error -> {
                        _updateUserState.value = UpdateUserState(isError = it.message)
                    }
                }

            }
        }
    }

    private val _deleteAccountState = MutableStateFlow(DeleteAccountState())
    val deleteAccountState = _deleteAccountState.asStateFlow()

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAccountUseCase.deleteAccountUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _deleteAccountState.value = DeleteAccountState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _deleteAccountState.value = DeleteAccountState(isSuccess = it.data)
                    }
                    is ResultState.Error -> {
                        _deleteAccountState.value = DeleteAccountState(isError = it.message)
                    }
                }
            }
        }
    }

    private val _signOutState = MutableStateFlow(SignOutState(false))
    val signOutState = _signOutState.asStateFlow()

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            signOutUseCase.signOutUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _signOutState.value = SignOutState(false)
                    }

                    is ResultState.Success -> {
                        _signOutState.value = SignOutState(true)
                    }

                    is ResultState.Error -> {
                        _signOutState.value = SignOutState(false)
                    }
                }
            }
        }
    }

    private val _fetchUserDataState = MutableStateFlow(FetchUserDataState())
    val fetchUserDataState = _fetchUserDataState.asStateFlow()

    fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchUserDataUseCase.fetchUserDataUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _fetchUserDataState.value = FetchUserDataState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _fetchUserDataState.value = FetchUserDataState(isSuccess = it.data)
                    }

                    is ResultState.Error -> {
                        _fetchUserDataState.value = FetchUserDataState(isError = it.message)
                    }
                }
            }
        }
    }

    private val _userName = MutableStateFlow(FetchUserNameState())
    val userName = _userName.asStateFlow()


    fun fetchUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserNameUseCase.fetchUserNameUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _userName.value = FetchUserNameState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _userName.value = FetchUserNameState(isSuccess = it.data)
                    }

                    is ResultState.Error -> {
                        _userName.value = FetchUserNameState(isError = it.message)
                    }
                }
            }
        }
    }

    // ==================== SKILL & IDEA MANAGEMENT ====================


    
    private val _lastGeneratedIdeaId = MutableStateFlow(-1)
    val lastGeneratedIdeaId = _lastGeneratedIdeaId.asStateFlow()
    
    private val _currentIdea = MutableStateFlow<IdeaEntity?>(null)
    val currentIdea = _currentIdea.asStateFlow()

    private val _generateIdeaState = MutableStateFlow(GenerateIdeaState())
    val generateIdeaState = _generateIdeaState.asStateFlow()
    fun generateIdeas(skill: UserSkillEntity) {
        viewModelScope.launch {
            try {
                // Set loading state
                android.util.Log.d("MyViewModel", "Starting generateIdeas for skill: ${skill.skillId}")
                _generateIdeaState.value = GenerateIdeaState(isLoading = true, isError = null)
                
                // Call use case (it handles its own dispatcher)
                val result = generateIdeaUseCase(skill)
                
                // Handle result based on type
                when (result) {
                    is ResultState.Loading -> {
                        android.util.Log.d("MyViewModel", "Result.Loading received")
                        _generateIdeaState.value = GenerateIdeaState(isLoading = true, isError = null)
                    }
                    is ResultState.Success -> {
                        // Validate we got ideas
                        android.util.Log.d("MyViewModel", "Result.Success received with ${result.data.size} ideas")
                        if (result.data.isNotEmpty()) {
                            android.util.Log.d("MyViewModel", "Setting state with ${result.data.size} ideas")
                            _generateIdeaState.value = GenerateIdeaState(
                                isLoading = false,
                                isSuccess = result.data,
                                isError = null
                            )
                            
                            // Save the first idea immediately to database
                            val firstIdea = result.data.first()
                            android.util.Log.d("MyViewModel", "Saving idea: ${firstIdea.ideaTitle}")
                            try {
                                val savedId = ideaRepository.saveIdea(firstIdea)
                                // Set the current idea with the actual saved ID
                                val ideaWithId = firstIdea.copy(ideaId = savedId.toInt())
                                _currentIdea.value = ideaWithId
                                _lastGeneratedIdeaId.value = savedId.toInt()
                                
                                // IMPORTANT: Add to _allIdeas list so it appears in GetAllIdeaScreen
                                val currentList = _allIdeas.value.toMutableList()
                                currentList.add(0, ideaWithId) // Add to the beginning (newest first)
                                _allIdeas.value = currentList
                                
                                android.util.Log.d("MyViewModel", "Idea saved with ID: $savedId and added to allIdeas list")
                            } catch (e: Exception) {
                                android.util.Log.e("MyViewModel", "Error saving idea", e)
                            }
                            
                            android.util.Log.d("MyViewModel", "State updated: ${_generateIdeaState.value.isSuccess?.size} ideas in state")
                        } else {
                            android.util.Log.d("MyViewModel", "No ideas in result")
                            _generateIdeaState.value = GenerateIdeaState(
                                isLoading = false,
                                isSuccess = null,
                                isError = "No ideas were generated. Please try with different inputs."
                            )
                        }
                    }
                    is ResultState.Error -> {
                        android.util.Log.d("MyViewModel", "Result.Error: ${result.message}")
                        _generateIdeaState.value = GenerateIdeaState(
                            isLoading = false,
                            isSuccess = null,
                            isError = result.message
                        )
                    }
                }
            } catch (e: Exception) {
                // Catch any unhandled exceptions
                android.util.Log.e("MyViewModel", "Exception in generateIdeas", e)
                _generateIdeaState.value = GenerateIdeaState(
                    isLoading = false,
                    isSuccess = null,
                    isError = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun insertSkill(skill: UserSkillEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                android.util.Log.d("MyViewModel", "Inserting skill...")
                val savedId = userSkillDao.insertUserSkill(skill)
                
                // Create skill with actual saved ID
                val skillWithId = skill.copy(skillId = savedId.toInt())
                android.util.Log.d("MyViewModel", "Skill saved with ID: $savedId, now generating ideas...")
                
                // Now generate ideas with the skill that has the correct ID
                generateIdeas(skillWithId)
            } catch (e: Exception) {
                // Log error but don't crash
                android.util.Log.e("MyViewModel", "Error saving skill", e)
                _generateIdeaState.value = GenerateIdeaState(
                    isLoading = false,
                    isSuccess = null,
                    isError = "Failed to save skill: ${e.message}"
                )
            }
        }
    }

    fun resetIdeaState() {
        _generateIdeaState.value = GenerateIdeaState()
    }

    fun saveIdea(idea: IdeaEntity) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val savedId = ideaRepository.saveIdea(idea)
                android.util.Log.d("MyViewModel", "Idea saved from UI with ID: $savedId")
                
                // Add the newly saved idea to the _allIdeas list
                val currentList = _allIdeas.value.toMutableList()
                val ideaWithId = idea.copy(ideaId = savedId.toInt())
                currentList.add(0, ideaWithId) // Add to the beginning (newest first)
                _allIdeas.value = currentList
                
                android.util.Log.d("MyViewModel", "Updated allIdeas list with newly saved idea")
            } catch (e: Exception) {
                // Log error but don't crash
                android.util.Log.e("MyViewModel", "Error saving idea from UI", e)
                _generateIdeaState.value = GenerateIdeaState(
                    isLoading = false,
                    isSuccess = null,
                    isError = "Failed to save idea: ${e.message}"
                )
            }
        }
    }

    fun fetchIdeaById(userId: String, ideaId: Int) {
        viewModelScope.launch {
            try {
                android.util.Log.d("MyViewModel", "Fetching idea with ID: $ideaId for user: $userId")
                val ideas = ideaRepository.getIdeasByIdeaId(userId, ideaId)
                if (ideas.isNotEmpty()) {
                    _currentIdea.value = ideas.first()
                    android.util.Log.d("MyViewModel", "Idea fetched: ${ideas.first().ideaTitle}")
                } else {
                    android.util.Log.d("MyViewModel", "No idea found with ID: $ideaId")
                }
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error fetching idea", e)
            }
        }
    }

    // ==================== ALL IDEAS MANAGEMENT ====================

    private val _allIdeas = MutableStateFlow<List<IdeaEntity>>(emptyList())
    val allIdeas = _allIdeas.asStateFlow()

    private val _allIdeasLoading = MutableStateFlow(false)
    val allIdeasLoading = _allIdeasLoading.asStateFlow()

    private val _allIdeasError = MutableStateFlow<String?>(null)
    val allIdeasError = _allIdeasError.asStateFlow()

    fun fetchAllIdeas(userId: String) {
        viewModelScope.launch {
            try {
                _allIdeasLoading.value = true
                _allIdeasError.value = null
                android.util.Log.d("MyViewModel", "Fetching all ideas for user: $userId")
                
                val ideas = ideaRepository.getAllIdeas(userId)
                // Sort by createdAt descending (newest first)
                _allIdeas.value = ideas.sortedByDescending { it.createdAt }
                
                android.util.Log.d("MyViewModel", "Fetched ${ideas.size} ideas")
                _allIdeasLoading.value = false
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error fetching all ideas", e)
                _allIdeasError.value = e.message ?: "Error loading ideas"
                _allIdeasLoading.value = false
            }
        }
    }

    fun searchIdeas(query: String, userId: String) {
        viewModelScope.launch {
            try {
                if (query.isBlank()) {
                    // If search is empty, fetch all ideas
                    fetchAllIdeas(userId)
                } else {
                    // Filter ideas by title or description containing query
                    val filteredIdeas = _allIdeas.value.filter { idea ->
                        idea.ideaTitle.contains(query, ignoreCase = true) ||
                        idea.description.contains(query, ignoreCase = true)
                    }
                    _allIdeas.value = filteredIdeas
                    android.util.Log.d("MyViewModel", "Search found ${filteredIdeas.size} ideas")
                }
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error searching ideas", e)
            }
        }
    }

    fun filterIdeasByDate(startDate: Long?, endDate: Long?) {
        viewModelScope.launch {
            try {
                val filtered = _allIdeas.value.filter { idea ->
                    val ideaTime = idea.createdAt
                    val afterStart = startDate == null || ideaTime >= startDate
                    val beforeEnd = endDate == null || ideaTime <= endDate
                    afterStart && beforeEnd
                }
                _allIdeas.value = filtered.sortedByDescending { it.createdAt }
                android.util.Log.d("MyViewModel", "Date filter found ${filtered.size} ideas")
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error filtering by date", e)
            }
        }
    }


}

data class CreateUserState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)

data class LoginUserState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = null
)

data class DeleteAccountState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)

data class FetchUserNameState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)

data class FetchUserDataState(
    val isLoading: Boolean = false,
    val isSuccess: UserDataModels? = null,
    val isError: String? = null
)


data class SignOutState(
    val isSuccess: Boolean,
    val isError: String? = null,
    val isLoading: Boolean = false
)

data class UpdateUserState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)

data class GenerateIdeaState(
    val isLoading: Boolean = false,
    val isSuccess: List<IdeaEntity>? = null,
    val isError: String? = null
)
