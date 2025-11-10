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
import com.skillMatcher.buildMate.domain.usecases.SaveSkillUseCase
import com.skillMatcher.buildMate.domain.usecases.SaveIdeaUseCase
import com.skillMatcher.buildMate.domain.usecases.FetchAllIdeasUseCase
import com.skillMatcher.buildMate.domain.usecases.FetchIdeaByIdUseCase
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
    private val saveSkillUseCase: SaveSkillUseCase,
    private val saveIdeaUseCase: SaveIdeaUseCase,
    private val fetchAllIdeasUseCase: FetchAllIdeasUseCase,
    private val fetchIdeaByIdUseCase: FetchIdeaByIdUseCase,
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

    // ==================== SKILL MANAGEMENT ====================

    private val _saveSkillState = MutableStateFlow(SaveSkillState())
    val saveSkillState = _saveSkillState.asStateFlow()

    fun saveSkill(skill: UserSkillEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _saveSkillState.value = SaveSkillState(isLoading = true)
                android.util.Log.d("MyViewModel", "Saving skill: ${skill.techStack}")

                val savedId = saveSkillUseCase.saveSkillUseCase(skill)
                val skillWithId = skill.copy(skillId = savedId.toInt())

                _saveSkillState.value = SaveSkillState(
                    isLoading = false,
                    isSuccess = skillWithId,
                    isError = null
                )

                android.util.Log.d("MyViewModel", "Skill saved with ID: $savedId")
                
                // Auto-generate ideas for this skill
                generateIdeas(skillWithId)
                
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error saving skill", e)
                _saveSkillState.value = SaveSkillState(
                    isLoading = false,
                    isSuccess = null,
                    isError = e.message ?: "Failed to save skill"
                )
            }
        }
    }

    // ==================== IDEA GENERATION & MANAGEMENT ====================

    private val _generateIdeaState = MutableStateFlow(GenerateIdeaState())
    val generateIdeaState = _generateIdeaState.asStateFlow()

    private val _currentIdea = MutableStateFlow<IdeaEntity?>(null)
    val currentIdea = _currentIdea.asStateFlow()

    fun generateIdeas(skill: UserSkillEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _generateIdeaState.value = GenerateIdeaState(isLoading = true)
                android.util.Log.d("MyViewModel", "Generating ideas for skill: ${skill.techStack}")

                val result = generateIdeaUseCase(skill)

                when (result) {
                    is ResultState.Success -> {
                        if (result.data.isNotEmpty()) {
                            _generateIdeaState.value = GenerateIdeaState(
                                isLoading = false,
                                isSuccess = result.data,
                                isError = null
                            )
                            
                            // Save first idea to database
                            val firstIdea = result.data.first()
                            val savedId = saveIdeaUseCase.saveIdeaUseCase(firstIdea)
                            val ideaWithId = firstIdea.copy(ideaId = savedId.toInt())
                            
                            _currentIdea.value = ideaWithId
                            
                            // Add to all ideas list
                            val currentList = _allIdeas.value.toMutableList()
                            currentList.add(0, ideaWithId)
                            _allIdeas.value = currentList
                            
                            android.util.Log.d("MyViewModel", "Ideas generated and saved: ${result.data.size}")
                        } else {
                            _generateIdeaState.value = GenerateIdeaState(
                                isLoading = false,
                                isSuccess = null,
                                isError = "No ideas generated. Try different inputs."
                            )
                        }
                    }
                    is ResultState.Error -> {
                        _generateIdeaState.value = GenerateIdeaState(
                            isLoading = false,
                            isSuccess = null,
                            isError = result.message
                        )
                    }
                    is ResultState.Loading -> {
                        _generateIdeaState.value = GenerateIdeaState(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Exception generating ideas", e)
                _generateIdeaState.value = GenerateIdeaState(
                    isLoading = false,
                    isSuccess = null,
                    isError = e.message ?: "Error generating ideas"
                )
            }
        }
    }

    fun resetGenerateIdeaState() {
        _generateIdeaState.value = GenerateIdeaState()
    }

    // ==================== SAVE IDEA MANAGEMENT ====================

    private val _saveIdeaState = MutableStateFlow(SaveIdeaState())
    val saveIdeaState = _saveIdeaState.asStateFlow()

    fun saveIdea(idea: IdeaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _saveIdeaState.value = SaveIdeaState(isLoading = true)
                android.util.Log.d("MyViewModel", "Saving idea: ${idea.ideaTitle}")

                val savedId = saveIdeaUseCase.saveIdeaUseCase(idea)
                val ideaWithId = idea.copy(ideaId = savedId.toInt())

                _saveIdeaState.value = SaveIdeaState(
                    isLoading = false,
                    isSuccess = ideaWithId,
                    isError = null
                )

                // Add to all ideas list
                val currentList = _allIdeas.value.toMutableList()
                if (!currentList.any { it.ideaId == savedId.toInt() }) {
                    currentList.add(0, ideaWithId)
                    _allIdeas.value = currentList
                }

                android.util.Log.d("MyViewModel", "Idea saved with ID: $savedId")
                
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error saving idea", e)
                _saveIdeaState.value = SaveIdeaState(
                    isLoading = false,
                    isSuccess = null,
                    isError = e.message ?: "Failed to save idea"
                )
            }
        }
    }

    // ==================== FETCH IDEA BY ID MANAGEMENT ====================

    private val _fetchIdeaByIdState = MutableStateFlow(FetchIdeaByIdState())
    val fetchIdeaByIdState = _fetchIdeaByIdState.asStateFlow()

    fun fetchIdeaById(userId: String, ideaId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _fetchIdeaByIdState.value = FetchIdeaByIdState(isLoading = true)
                android.util.Log.d("MyViewModel", "Fetching idea ID: $ideaId")

                val ideas = fetchIdeaByIdUseCase.fetchIdeaByIdUseCase(userId, ideaId)

                if (ideas.isNotEmpty()) {
                    _fetchIdeaByIdState.value = FetchIdeaByIdState(
                        isLoading = false,
                        isSuccess = ideas.first(),
                        isError = null
                    )
                    _currentIdea.value = ideas.first()
                    android.util.Log.d("MyViewModel", "Idea fetched: ${ideas.first().ideaTitle}")
                } else {
                    _fetchIdeaByIdState.value = FetchIdeaByIdState(
                        isLoading = false,
                        isSuccess = null,
                        isError = "Idea not found"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error fetching idea", e)
                _fetchIdeaByIdState.value = FetchIdeaByIdState(
                    isLoading = false,
                    isSuccess = null,
                    isError = e.message ?: "Failed to fetch idea"
                )
            }
        }
    }

    // ==================== FETCH ALL IDEAS MANAGEMENT ====================

    private val _fetchAllIdeasState = MutableStateFlow(FetchAllIdeasState())
    val fetchAllIdeasState = _fetchAllIdeasState.asStateFlow()

    private val _allIdeas = MutableStateFlow<List<IdeaEntity>>(emptyList())
    val allIdeas = _allIdeas.asStateFlow()

    fun fetchAllIdeas(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _fetchAllIdeasState.value = FetchAllIdeasState(isLoading = true)
                android.util.Log.d("MyViewModel", "Fetching all ideas for user: $userId")

                val ideas = ideaRepository.getAllIdeas(userId)
                val sortedIdeas = ideas.sortedByDescending { it.createdAt }

                _fetchAllIdeasState.value = FetchAllIdeasState(
                    isLoading = false,
                    isSuccess = sortedIdeas,
                    isError = null
                )
                _allIdeas.value = sortedIdeas

                android.util.Log.d("MyViewModel", "Fetched ${ideas.size} ideas")
                
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error fetching all ideas", e)
                _fetchAllIdeasState.value = FetchAllIdeasState(
                    isLoading = false,
                    isSuccess = null,
                    isError = e.message ?: "Failed to fetch ideas"
                )
            }
        }
    }

    // ==================== SEARCH & FILTER IDEAS ====================

    fun searchIdeas(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (query.isBlank()) {
                    _allIdeas.value = _fetchAllIdeasState.value.isSuccess ?: emptyList()
                } else {
                    val filteredIdeas = (_fetchAllIdeasState.value.isSuccess ?: emptyList()).filter { idea ->
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

    fun filterIdeasByDifficulty(difficulty: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (difficulty.isEmpty()) {
                    _allIdeas.value = _fetchAllIdeasState.value.isSuccess ?: emptyList()
                } else {
                    val filtered = (_fetchAllIdeasState.value.isSuccess ?: emptyList()).filter { idea ->
                        idea.difficulty.equals(difficulty, ignoreCase = true)
                    }
                    _allIdeas.value = filtered
                    android.util.Log.d("MyViewModel", "Difficulty filter found ${filtered.size} ideas")
                }
            } catch (e: Exception) {
                android.util.Log.e("MyViewModel", "Error filtering by difficulty", e)
            }
        }
    }

    fun filterIdeasByDate(startDate: Long?, endDate: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val filtered = (_fetchAllIdeasState.value.isSuccess ?: emptyList()).filter { idea ->
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

data class SaveSkillState(
    val isLoading: Boolean = false,
    val isSuccess: UserSkillEntity? = null,
    val isError: String? = null
)

data class GenerateIdeaState(
    val isLoading: Boolean = false,
    val isSuccess: List<IdeaEntity>? = null,
    val isError: String? = null
)

data class SaveIdeaState(
    val isLoading: Boolean = false,
    val isSuccess: IdeaEntity? = null,
    val isError: String? = null
)

data class FetchIdeaByIdState(
    val isLoading: Boolean = false,
    val isSuccess: IdeaEntity? = null,
    val isError: String? = null
)

data class FetchAllIdeasState(
    val isLoading: Boolean = false,
    val isSuccess: List<IdeaEntity>? = null,
    val isError: String? = null
)
