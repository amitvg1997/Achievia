package com.htw.proitd.achievia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.htw.proitd.achievia.data.GoalRepository
import com.htw.proitd.achievia.model.Goal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onNavigateToCreateGoal: () -> Unit,
    onNavigateToEditGoal: (String) -> Unit,
    onLogout: () -> Unit
) {
    val goals by GoalRepository.goals.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("My Goals") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.TrackChanges, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("My Tasks") },
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Task, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Friends") },
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Group, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Notifications") },
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Logout", color = Color.Red) },
                    selected = false,
                    onClick = onLogout,
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My Goals", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFF6F43)
                    )
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToCreateGoal,
                    containerColor = Color(0xFFFF6F43),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Goal")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add New Goal")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF6F43))
                        .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                ) {
                    Text(
                        text = "Keep pushing towards your dreams",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(goals) { goal ->
                        GoalItem(goal = goal, onEditClick = { onNavigateToEditGoal(goal.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF6F43))
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text("JD", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("John Doe", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("john@example.com", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
    }
}

@Composable
fun GoalItem(goal: Goal, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgress(percentage = goal.progress)
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = goal.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    if (goal.sharedWith.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Group,
                            contentDescription = "Shared",
                            tint = Color(0xFF4285F4),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = goal.description,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                if (goal.sharedWith.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Shared with ${goal.sharedWith.joinToString(", ")}",
                        color = Color(0xFF4285F4),
                        fontSize = 12.sp
                    )
                }
            }
            
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun CircularProgress(percentage: Int) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(60.dp)) {
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = if (percentage < 50) Color(0xFFFF6F43) else Color(0xFF00C853),
                startAngle = -90f,
                sweepAngle = 360 * (percentage / 100f),
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$percentage%",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
