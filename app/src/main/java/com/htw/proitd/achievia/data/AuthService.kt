package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Authentication and account recovery.
 */
interface AuthService {
    suspend fun register(name: String, email: String, password: String): User
    suspend fun login(email: String, password: String): User
    suspend fun logout()
    fun currentUser(): Flow<User?>
    suspend fun sendPasswordReset(email: String)
}
