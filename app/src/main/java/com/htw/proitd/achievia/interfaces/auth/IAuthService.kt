package com.htw.proitd.achievia.interfaces.auth

import com.htw.proitd.achievia.model.User

/**
 * Result of authentication operations
 */
sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

/**
 * Interface for authentication operations.
 * Provides abstraction for user authentication and registration.
 */
interface IAuthService {
    /**
     * Register a new user
     * @param name User's full name
     * @param email User's email address
     * @param password User's password
     * @return AuthResult indicating success or failure
     */
    suspend fun register(name: String, email: String, password: String): AuthResult

    /**
     * Login an existing user
     * @param email User's email address
     * @param password User's password
     * @return AuthResult indicating success or failure
     */
    suspend fun login(email: String, password: String): AuthResult

    /**
     * Logout the current user
     */
    suspend fun logout()

    /**
     * Get the currently logged in user
     * @return The current user, or null if not logged in
     */
    suspend fun getCurrentUser(): User?

    /**
     * Check if a user is currently logged in
     * @return true if user is logged in, false otherwise
     */
    suspend fun isLoggedIn(): Boolean
}

