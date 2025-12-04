package com.htw.proitd.achievia.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String
)

data class Goal(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val progress: Int = 0, // 0 to 100
    val sharedWith: List<String> = emptyList(), // List of names
    val tasks: List<Task> = emptyList()
)

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val allocatedTime: String = "" // e.g., "10h allocated"
)
