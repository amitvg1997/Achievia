package com.htw.proitd.achievia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.htw.proitd.achievia.data.GoalRepository
import com.htw.proitd.achievia.model.Goal
import com.htw.proitd.achievia.model.Task
import java.util.UUID
import androidx.compose.ui.tooling.preview.Preview
import com.htw.proitd.achievia.ui.theme.AchieviaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalInputScreen(
    goalId: String? = null,
    onNavigateBack: () -> Unit
) {
    val existingGoal = remember(goalId) { goalId?.let { GoalRepository.getGoal(it) } }
    
    var title by remember { mutableStateOf(existingGoal?.title ?: "") }
    var description by remember { mutableStateOf(existingGoal?.description ?: "") }
    var tasks by remember { mutableStateOf(existingGoal?.tasks ?: emptyList()) }
    var showTaskDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (goalId == null) "New Goal" else "Edit Goal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tasks", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = { showTaskDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F43))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Task")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(task = task, onDelete = {
                        tasks = tasks.filter { it.id != task.id }
                    })
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val newGoal = Goal(
                            id = existingGoal?.id ?: UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            progress = existingGoal?.progress ?: 0,
                            tasks = tasks,
                            sharedWith = existingGoal?.sharedWith ?: emptyList()
                        )
                        if (existingGoal != null) {
                            GoalRepository.updateGoal(newGoal)
                        } else {
                            GoalRepository.addGoal(newGoal)
                        }
                        onNavigateBack()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F43))
                ) {
                    Text("Save Changes")
                }
            }
        }
    }

    if (showTaskDialog) {
        AddTaskDialog(
            onDismiss = { showTaskDialog = false },
            onAddTask = { newTask ->
                tasks = tasks + newTask
                showTaskDialog = false
            }
        )
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, fontWeight = FontWeight.Medium)
                if (task.description.isNotEmpty()) {
                    Text(text = task.description, fontSize = 12.sp, color = Color.Gray)
                }
                if (task.allocatedHours > 0f) {
                    Text(text = "${task.allocatedHours}h allocated", fontSize = 12.sp, color = Color.Gray)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAddTask: (Task) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var allocatedTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = allocatedTime,
                    onValueChange = { allocatedTime = it },
                    label = { Text("Time Allocation (e.g. 2.5)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotEmpty()) {
                        onAddTask(Task(title = title, description = description, allocatedHours = allocatedTime.toFloatOrNull() ?: 0f))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F43))
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GoalInputScreenPreview() {
    AchieviaTheme {
        GoalInputScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun EditGoalInputScreenPreview() {
    val sampleGoal = remember {
        Goal(
            id = "preview-id-edit",
            title = "Learn Jetpack Compose",
            description = "Finish all the official tutorials and build a sample app.",
            progress = 40,
            tasks = listOf(
                Task(title = "Complete Basic Layouts Codelab", allocatedHours = 5f, loggedHours = 5f, isCompleted = true),
                Task(title = "State in Compose", allocatedHours = 3f, loggedHours = 3f, isCompleted = true),
                Task(title = "Navigation Codelab", allocatedHours = 4f, isCompleted = false)
            )
        )
    }

    LaunchedEffect(sampleGoal.id) {
        GoalRepository.deleteGoal(sampleGoal.id) // ensure clean state for preview
        GoalRepository.addGoal(sampleGoal)
    }

    AchieviaTheme {
        GoalInputScreen(
            goalId = sampleGoal.id,
            onNavigateBack = {}
        )
    }
}