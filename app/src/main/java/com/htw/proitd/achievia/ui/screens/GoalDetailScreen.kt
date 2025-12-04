
package com.htw.proitd.achievia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.htw.proitd.achievia.model.Goal
import com.htw.proitd.achievia.model.Task
import com.htw.proitd.achievia.data.GoalRepository
import com.htw.proitd.achievia.ui.theme.Orange500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    goalId: String?,
    onNavigateBack: () -> Unit
) {
    val goal by remember(goalId) {
        mutableStateOf(goalId?.let { GoalRepository.getGoal(it) })
    }

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
                items(goal!!.tasks) { task ->
                    TaskCard(task = task)
                }
            }
        } else {
            // Displaying a "Not Found" message is good practice.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Respect the scaffold's padding
                contentAlignment = Alignment.Center
            ) {
                Text("Goal not found.")
            }
        }
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
            progress = 1f,
            modifier = Modifier.fillMaxSize(),
            color = Color.White.copy(alpha = 0.3f),
            strokeWidth = 6.dp
        )
        CircularProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF2165F3),
            strokeWidth = 6.dp
        )
        Text(
            text = "$progress%",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TaskCard(task: Task) {
    // Derive a simple progress value from completion state for visual feedback.
    val progress = if (task.isCompleted) 100 else 30

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
                val icon = Icons.Default.Person
                val typeText = "Personal"
                Icon(
                    imageVector = icon,
                    contentDescription = typeText,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = typeText,
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
                progress = progress / 100f.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Orange500,
                trackColor = MaterialTheme.colorScheme.onSurface // Use onSurface for better contrast
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
                        text = "${task.hoursLogged}h / ${task.allocatedTime}h",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Button(
                    onClick = { /* TODO: Handle log hours */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Log Hours")
                }
            }
        }
    }
}

// Dummy data for preview is unchanged and correctly set up.
object DummyGoal {
    //...
}

@Preview(showBackground = true)
@Composable
fun GoalDetailScreenPreview() {
    //...
}
