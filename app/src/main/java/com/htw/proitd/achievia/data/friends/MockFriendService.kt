package com.htw.proitd.achievia.data.friends

import com.htw.proitd.achievia.data.GoalRepository
import com.htw.proitd.achievia.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * Mock implementation of IFriendService.
 * Uses in-memory storage with StateFlow for reactive updates.
 */
object MockFriendService : IFriendService {
    private val _friends = MutableStateFlow<List<User>>(emptyList())
    override val friends: StateFlow<List<User>> = _friends.asStateFlow()

    // Map of friendId -> count of shared goals
    private val sharedGoalsCount = mutableMapOf<String, Int>()

    init {
        // Seed with some dummy friends
        val friend1 = User(id = "friend1", name = "Sarah Johnson", email = "sarah@example.com")
        val friend2 = User(id = "friend2", name = "Mike Chen", email = "mike@example.com")
        val friend3 = User(id = "friend3", name = "Emily Davis", email = "emily@example.com")
        
        _friends.value = listOf(friend1, friend2, friend3)
        sharedGoalsCount[friend1.id] = 3
        sharedGoalsCount[friend2.id] = 1
        sharedGoalsCount[friend3.id] = 2
    }

    override suspend fun addFriend(email: String): FriendResult {
        delay(300)

        // Check if friend already exists
        val existingFriend = _friends.value.find { it.email.equals(email, ignoreCase = true) }
        if (existingFriend != null) {
            return FriendResult.Error("Friend already added")
        }

        // Validate email
        if (email.isBlank() || !email.contains("@")) {
            return FriendResult.Error("Invalid email address")
        }

        // Create new friend (in real app, would search for user by email)
        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val friend = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email
        )

        _friends.update { it + friend }
        sharedGoalsCount[friend.id] = 0

        return FriendResult.Success(friend)
    }

    override suspend fun removeFriend(friendId: String) {
        delay(200)
        _friends.update { it.filter { friend -> friend.id != friendId } }
        sharedGoalsCount.remove(friendId)
    }

    override suspend fun getFriend(friendId: String): User? {
        delay(100)
        return _friends.value.find { it.id == friendId }
    }

    override suspend fun searchFriends(query: String): List<User> {
        delay(200)
        val lowerQuery = query.lowercase()
        return _friends.value.filter {
            it.name.lowercase().contains(lowerQuery) ||
            it.email.lowercase().contains(lowerQuery)
        }
    }

    override suspend fun getSharedGoalsCount(friendId: String): Int {
        delay(100)
        
        // Calculate actual shared goals from repository
        val friend = getFriend(friendId)
        if (friend == null) return 0

        val goals = GoalRepository.goals.value
        val count = goals.count { goal ->
            goal.sharedWith.any { it.equals(friend.name, ignoreCase = true) }
        }
        
        sharedGoalsCount[friendId] = count
        return count
    }
}

