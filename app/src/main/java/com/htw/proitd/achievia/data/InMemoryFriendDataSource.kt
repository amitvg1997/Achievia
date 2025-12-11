package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * In-memory friendship management.
 */
class InMemoryFriendDataSource(
    private val store: InMemoryBackendStore = InMemoryBackendStore
) : FriendDataSource {

    override fun observeFriends(userId: String): Flow<List<User>> {
        // Emits whenever the friend's map changes.
        return store.friendsState.map { it[userId].orEmpty() }
    }

    override suspend fun addFriend(userId: String, friendEmail: String) {
        // Create a placeholder user record if none exists for the email.
        val friend = store.usersByEmail.getOrPut(friendEmail) {
            User(id = UUID.randomUUID().toString(), name = friendEmail.substringBefore('@'), email = friendEmail)
        }
        store.friendsState.update { current ->
            val updatedList = current[userId].orEmpty() + friend
            current + (userId to updatedList)
        }
    }

    override suspend fun removeFriend(userId: String, friendId: String) {
        store.friendsState.update { current ->
            val updatedList = current[userId].orEmpty().filterNot { it.id == friendId }
            current + (userId to updatedList)
        }
    }
}
