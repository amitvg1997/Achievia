package com.htw.proitd.achievia.data

import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Contract for managing friendships between users.
 */
interface FriendDataSource {
    fun observeFriends(userId: String): Flow<List<User>>
    suspend fun addFriend(userId: String, friendEmail: String)
    suspend fun removeFriend(userId: String, friendId: String)
}
