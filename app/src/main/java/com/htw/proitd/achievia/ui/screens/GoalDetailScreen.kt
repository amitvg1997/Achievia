package com.htw.proitd.achievia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.htw.proitd.achievia.data.GoalRepository
import com.htw.proitd.achievia.data.IGoalRepository
import com.htw.proitd.achievia.data.tasks.ITaskService
import com.htw.proitd.achievia.data.tasks.MockTaskService
import com.htw.proitd.achievia.model.Goal
import com.htw.proitd.achievia.model.Task
import kotlinx.coroutines.launch
import com.htw.proitd.achievia.ui.theme.AchieviaTheme

private val Orange500 = Color(0xFFFF6F43)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    goalRepository: IGoalRepository = GoalRepository,
    taskService: ITaskService = MockTaskService,
    goalId: String?,
    onNavigateBack: () -> Unit
) {
    val goals by goalRepository.goals.collectAsState()
    val goal = remember(goalId, goals) {
        goalId?.let { id -> goals.find { it.id == id } }
    }
    var taskToLogHours by remember { mutableStateOf<Task?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        GoalDetailHeader(goal = goal, onNavigateBack = onNavigateBack)
    }) { paddingValues ->
        if (goal != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.surface),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(goal.tasks) { task ->
                    TaskCard(
                        task = task,
                        taskService = taskService,
                        onLogHoursClick = { taskToLogHours = task }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Goal not found.")
            }
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                Button(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }

    taskToLogHours?.let { task ->
        LogHoursDialog(
            task = task,
            onDismiss = { taskToLogHours = null },
            onLogHours = { hoursToAdd ->
                if (goalId != null) {
                    scope.launch {
                        val result = taskService.logHours(goalId, task.id, hoursToAdd)
                        when (result) {
                            is com.htw.proitd.achievia.data.tasks.HoursLoggingResult.Success -> {
                                taskToLogHours = null
                                errorMessage = null
                            }
                            is com.htw.proitd.achievia.data.tasks.HoursLoggingResult.Error -> {
                                errorMessage = result.message
                            }
                        }
                    }
                } else {
                    errorMessage = "Goal ID is required"
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailHeader(goal: Goal?, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            if (goal != null) {
                Column {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = goal.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        actions = {
            if (goal != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    GoalProgressIndicator(progress = goal.progress)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange500,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
fun GoalProgressIndicator(progress: Int) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = Color.White.copy(alpha = 0.3f),
            strokeWidth = 6.dp
        )
        CircularProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF2165F3),
            strokeWidth = 6.dp
        )
        Text(
            text = "$progress%",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TaskCard(
    task: Task,
    taskService: ITaskService = MockTaskService,
    onLogHoursClick: () -> Unit
) {
    val progress = taskService.calculateTaskProgress(task)

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Personal",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Personal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Progress", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "$progress%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Orange500,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Orange500,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${task.loggedHours}h / ${task.allocatedHours}h completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Button(
                    onClick = onLogHoursClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Log Hours")
                }
            }
        }
    }
}

@Composable
fun LogHoursDialog(
    task: Task,
    onDismiss: () -> Unit,
    onLogHours: (Float) -> Unit
) {
    var hoursString by remember { mutableStateOf("") }
    val maxHours = task.allocatedHours - task.loggedHours

    val onValueChange: (String) -> Unit = { newValue ->
        if (newValue.isEmpty()) {
            hoursString = ""
        } else {
            if (newValue.count { it == '.' } <= 1 && newValue.all { it.isDigit() || it == '.' }) {
                val floatValue = newValue.toFloatOrNull()
                if (floatValue != null) {
                    if (floatValue <= maxHours) {
                        hoursString = newValue
                    } else {
                        hoursString = maxHours.toString().removeSuffix(".0")
                    }
                } else if (newValue == "." || newValue.endsWith(".")) {
                    hoursString = newValue
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Work Hours") },
        text = {
            Column {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(task.title, fontWeight = FontWeight.Bold)
                            Text(
                                "${task.loggedHours}h / ${task.allocatedHours}h completed",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = hoursString,
                    onValueChange = onValueChange,
                    label = { Text("Hours Worked") },
                    placeholder = { Text("Enter hours (e.g., 2.5)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Column {
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = {
                                    val currentHours = hoursString.toFloatOrNull() ?: 0f
                                    val newHours = (currentHours + 0.5f).coerceAtMost(maxHours)
                                    onValueChange(newHours.toString())
                                }
                            ) {
                                Icon(Icons.Default.ArrowDropUp, "Increase")
                            }
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = {
                                    val currentHours = hoursString.toFloatOrNull() ?: 0f
                                    val newHours = (currentHours - 0.5f).coerceAtLeast(0f)
                                    onValueChange(newHours.toString())
                                }
                            ) {
                                Icon(Icons.Default.ArrowDropDown, "Decrease")
                            }
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onLogHours(hoursString.toFloatOrNull() ?: 0f) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F43))
            ) {
                Text("Log Hours")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GoalDetailScreenPreview() {
    val sampleGoal = remember {
        Goal(
            id = "preview-goal-detail",
            title = "Master Jetpack Compose",
            description = "Become a pro at building beautiful UIs in Android.",
            progress = 65,
            tasks = listOf(
                Task(id = "1", title = "Complete Compose Basics", description = "Codelab on basic layouts.", isCompleted = true, allocatedHours = 8f, loggedHours = 8f),
                Task(id = "2", title = "State Management", description = "Learn about `remember` and `mutableStateOf`.", isCompleted = true, allocatedHours = 4f, loggedHours = 2f),
                Task(id = "3", title = "Compose Navigation", description = "Understand NavGraphs and navigation.", isCompleted = false, allocatedHours = 6f, loggedHours = 1f)
            )
        )
    }

    LaunchedEffect(sampleGoal.id) {
        GoalRepository.deleteGoal(sampleGoal.id)
        GoalRepository.addGoal(sampleGoal)
    }

    AchieviaTheme {
        GoalDetailScreen(
            goalId = sampleGoal.id,
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogHoursDialogPreview() {
    val task = Task(id = "preview-task", title = "Cardio Sessions", allocatedHours = 15f, loggedHours = 9f)
    AchieviaTheme {
        LogHoursDialog(task = task, onDismiss = {}, onLogHours = {})
    }
}
