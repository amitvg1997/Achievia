package com.htw.proitd.achievia.interfaces.auth

import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

/**
 * Mock implementation of IAuthService.
 * Simulates authentication with in-memory user storage.
 */
object MockAuthService : IAuthService {
    private val _currentUser = MutableStateFlow<User?>(null)
    private val registeredUsers = mutableMapOf<String, Pair<String, String>>() // email -> (name, password)

    init {
        // Seed with a test user
        registeredUsers["test@example.com"] = Pair("Test User", "password123")
    }

    override suspend fun register(name: String, email: String, password: String): AuthResult {
        // Simulate network delay
        delay(500)

        // Check if user already exists
        if (registeredUsers.containsKey(email.lowercase())) {
            return AuthResult.Error("User with this email already exists")
        }

        // Validate input
        if (name.isBlank()) {
            return AuthResult.Error("Name cannot be empty")
        }
        if (email.isBlank() || !email.contains("@")) {
            return AuthResult.Error("Invalid email address")
        }
        if (password.length < 6) {
            return AuthResult.Error("Password must be at least 6 characters")
        }

        // Register user
        registeredUsers[email.lowercase()] = Pair(name, password)
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email
        )
        _currentUser.value = user
        return AuthResult.Success(user)
    }

    override suspend fun login(email: String, password: String): AuthResult {
        // Simulate network delay
        delay(500)

        val userData = registeredUsers[email.lowercase()]
        if (userData == null) {
            return AuthResult.Error("User not found")
        }

        if (userData.second != password) {
            return AuthResult.Error("Invalid password")
        }

        val user = User(
            id = UUID.randomUUID().toString(),
            name = userData.first,
            email = email
        )
        _currentUser.value = user
        return AuthResult.Success(user)
    }

    override suspend fun logout() {
        delay(200)
        _currentUser.value = null
    }

    override suspend fun getCurrentUser(): User? {
        delay(100)
        return _currentUser.value
    }

    override suspend fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}

