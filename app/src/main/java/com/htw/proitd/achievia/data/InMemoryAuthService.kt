package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Lightweight in-memory auth simulation.
 * Not secure; replace with Firebase Auth in production.
 */
class InMemoryAuthService(
    private val store: InMemoryBackendStore = InMemoryBackendStore
) : AuthService {

    override suspend fun register(name: String, email: String, password: String): User {
        // Password is ignored here; auth backend should handle securely.
        val user = User(id = UUID.randomUUID().toString(), name = name, email = email)
        store.usersByEmail[email] = user
        store.currentUserState.value = user
        return user
    }

    override suspend fun login(email: String, password: String): User {
        // Fetch existing user or simulate registration on first login.
        val user = store.usersByEmail.getOrPut(email) {
            User(id = UUID.randomUUID().toString(), name = email.substringBefore('@'), email = email)
        }
        store.currentUserState.value = user
        return user
    }

    override suspend fun logout() {
        store.currentUserState.value = null
    }

    override fun currentUser(): Flow<User?> {
        return store.currentUserState.map { it }
    }

    override suspend fun sendPasswordReset(email: String) {
        // No-op for in-memory; real impl would trigger Firebase password reset email.
    }
}
