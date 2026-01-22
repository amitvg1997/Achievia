package com.htw.proitd.achievia.data.friends

import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.flow.StateFlow

/**
 * Result of friend operations
 */
sealed class FriendResult {
    data class Success(val friend: User) : FriendResult()
    data class Error(val message: String) : FriendResult()
}

/**
 * Interface for friend management operations.
 * Provides abstraction for friend-related functionality.
 */
interface IFriendService {
    /**
     * Observable flow of all friends
     */
    val friends: StateFlow<List<User>>

    /**
     * Add a friend by email
     * @param email The email of the friend to add
     * @return FriendResult indicating success or failure
     */
    suspend fun addFriend(email: String): FriendResult

    /**
     * Remove a friend by ID
     * @param friendId The ID of the friend to remove
     */
    suspend fun removeFriend(friendId: String)

    /**
     * Get a friend by ID
     * @param friendId The ID of the friend to retrieve
     * @return The friend if found, null otherwise
     */
    suspend fun getFriend(friendId: String): User?

    /**
     * Get friends by name (search)
     * @param query The search query
     * @return List of matching friends
     */
    suspend fun searchFriends(query: String): List<User>

    /**
     * Get the number of shared goals with a friend
     * @param friendId The ID of the friend
     * @return The number of shared goals
     */
    suspend fun getSharedGoalsCount(friendId: String): Int
}

