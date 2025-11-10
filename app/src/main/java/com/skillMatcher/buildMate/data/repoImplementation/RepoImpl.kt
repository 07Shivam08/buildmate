package com.skillMatcher.buildMate.data.repoImplementation

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.skillMatcher.buildMate.data.dao.IdeaDao
import com.skillMatcher.buildMate.data.dao.UserSkillDao
import com.skillMatcher.buildMate.data.remote.GeminiApiService
import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.domain.repo.Repo
import com.skillMatcher.buildMate.path.User_Path
import com.skillMatcher.buildMate.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val ideaDao: IdeaDao,
    private val userSkillDao: UserSkillDao,
    private val firebaseAuth: FirebaseAuth,
    private val geminiApi: GeminiApiService,
) : Repo {

    override fun registerUserWithEmailAndPassword(userData: UserDataModels): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener {

                    Log.d("TAG", "registerUserWithEmailAndPassword: ${it.user!!.uid}")


                    // this is dne to store same copy of uid in firestore

                    fireStore.collection(User_Path).document(it.user!!.uid).set(userData)
                        .addOnSuccessListener {

                            Log.d(
                                "TAG",
                                "registerUserWithEmailAndPassword: User Registered Successfully"
                            )
                            trySend(ResultState.Success("User Registered Successfully"))
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.message.toString()))
                        }

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }

        }

    override fun loginUserWithEmailAndPassword(userData: UserDataModels): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            if (userData.email.isBlank() || userData.password.isBlank()) {
                trySend(ResultState.Error("Email or Password cannot be empty"))
                close()
                return@callbackFlow
            }


            // Show loading state
            trySend(ResultState.Loading)

            // Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener {
                    Log.d("TAG", "loginUserWithEmailAndPassword: ${it.user!!.uid}")


                    trySend(ResultState.Success("User Logged In Successfully"))
                }
                .addOnFailureListener { exception ->
                    Log.e("LoginError", "Error: ${exception.message}", exception)
                    trySend(ResultState.Error("Login Failed: ${exception.localizedMessage}"))
                }

            awaitClose {
                close()
            }
        }

    override fun updateUserDetails(userData: UserDataModels): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userRef = fireStore.collection(User_Path).document(currentUser.uid)

            val updateMap = mutableMapOf<String, Any>()

            userData.username?.takeIf { it.isNotBlank() }?.let { updateMap["username"] = it }
            userData.email?.takeIf { it.isNotBlank() }?.let { updateMap["email"] = it }
            userData.password?.takeIf { it.isNotBlank() }?.let { updateMap["password"] = it }
            userData.phoneNumber?.takeIf { it.isNotBlank() }?.let { updateMap["phoneNumber"] = it }

            if (updateMap.isEmpty()) {
                trySend(ResultState.Error("Please provide at least one field to update."))
                close()
                return@callbackFlow
            }

            userRef.update(updateMap)
                .addOnSuccessListener {
                    trySend(ResultState.Success("User details updated successfully."))
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Error("Error updating details: ${e.message}"))
                }

        } else {
            trySend(ResultState.Error("User not authenticated"))
        }

        awaitClose { close() }
    }

    override fun fetchUserName(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userRef = fireStore.collection(User_Path).document(currentUser.uid)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.toObject(UserDataModels::class.java)
                    if (userData != null) {
                        trySend(ResultState.Success(userData.username))
                    } else {
                        trySend(ResultState.Error("User data not found"))
                    }
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.message.toString()))
                }
        } else {
            trySend(ResultState.Error("User not authenticated"))

        }
        awaitClose{

            close()
        }


    }

    override fun fetchUserData(): Flow<ResultState<UserDataModels>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userRef = fireStore.collection(User_Path).document(currentUser.uid)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.toObject(UserDataModels::class.java)
                    if (userData != null) {
                        trySend(ResultState.Success(userData))
                    } else {
                        trySend(ResultState.Error("User data not found"))
                    }
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Error("Error fetching user: ${e.message}"))
                }
        } else {
            trySend(ResultState.Error("User not authenticated"))
        }

        awaitClose {
            close()
        }
    }

    override fun signOut(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseAuth.signOut()
        trySend(ResultState.Success("User Logged Out Successfully"))
        awaitClose {
            close()
        }
    }

    override fun deleteAccount(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            trySend(ResultState.Error("No user logged in"))
            close()
            return@callbackFlow
        }

        val userId = currentUser.uid

        try {
            // Delete user data from Firestore collections
            // 1. Delete user profile
            fireStore.collection(User_Path).document(userId).delete().await()

            // 4. Delete the Firebase Auth user
            currentUser.delete().await()

            // If all operations succeed, return success
            trySend(ResultState.Success("Account successfully deleted"))
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Failed to delete account"))
        }

        close()
    }



}