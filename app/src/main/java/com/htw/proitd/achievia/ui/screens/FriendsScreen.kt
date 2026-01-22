package com.htw.proitd.achievia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.htw.proitd.achievia.ui.theme.Orange500
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

data class FriendUi(
    val id: String,
    val name: String,
    val email: String,
    val sharedGoalsCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    friendService: com.htw.proitd.achievia.data.friends.IFriendService = com.htw.proitd.achievia.data.friends.MockFriendService,
    onNavigateBack: () -> Unit
) {
    val friendsFlow = friendService.friends.collectAsState()
    var showAddFriend by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Friends", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            "Collaborate and achieve together",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Orange500)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { showAddFriend = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500, contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Friend", fontSize = 16.sp)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FB))
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(friendsFlow.value, key = { it.id }) { user ->
                    var sharedCount by remember { mutableStateOf(0) }
                    LaunchedEffect(user.id) {
                        sharedCount = friendService.getSharedGoalsCount(user.id)
                    }
                    FriendCard(
                        friend = FriendUi(
                            id = user.id,
                            name = user.name,
                            email = user.email,
                            sharedGoalsCount = sharedCount
                        ),
                        onDelete = {
                            scope.launch {
                                friendService.removeFriend(user.id)
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddFriend) {
        AddFriendDialog(
            onDismiss = { 
                showAddFriend = false
                errorMessage = null
            },
            onAddFriend = { newFriend ->
                scope.launch {
                    val result = friendService.addFriend(newFriend.email)
                    when (result) {
                        is com.htw.proitd.achievia.data.friends.FriendResult.Success -> {
                            showAddFriend = false
                            errorMessage = null
                        }
                        is com.htw.proitd.achievia.data.friends.FriendResult.Error -> {
                            errorMessage = result.message
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun FriendCard(friend: FriendUi, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Orange500),
                contentAlignment = Alignment.Center
            ) {
                val initials = friend.name.split(" ")
                    .filter { it.isNotEmpty() }
                    .take(2)
                    .joinToString("") { it.first().uppercaseChar().toString() }
                Text(
                    initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(friend.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(friend.email, fontSize = 14.sp, color = Color(0xFF6B7280))
                Text(
                    "${friend.sharedGoalsCount} shared goal" + if (friend.sharedGoalsCount != 1) "s" else "",
                    fontSize = 14.sp,
                    color = Orange500,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove friend",
                    tint = Color(0xFF9CA3AF)
                )
            }
        }
    }
}

@Composable
private fun AddFriendDialog(
    onDismiss: () -> Unit,
    onAddFriend: (FriendUi) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add Friend", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onDismiss) {
                        Text("✕", fontSize = 18.sp, color = Color(0xFF9CA3AF))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Name", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Enter friend's name") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Email", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Enter friend's email") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (name.isNotBlank() && email.isNotBlank()) {
                            onAddFriend(
                                FriendUi(
                                    id = "",
                                    name = name.trim(),
                                    email = email.trim(),
                                    sharedGoalsCount = 0
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB6A1),
                        contentColor = Color.White
                    )
                ) {
                    Text("Add Friend", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = "Cancel",
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color(0xFF111827),
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}


