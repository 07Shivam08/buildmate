package com.skillMatcher.buildMate.domain.models

data class UserDataModels(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val userImage: String? = null,
)
