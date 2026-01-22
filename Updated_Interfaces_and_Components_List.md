# Achievia - Complete Interfaces and Components List

This document provides a comprehensive, up-to-date list of all interfaces and components implemented in the Achievia Android application.

---

## Interfaces

The codebase now implements **5 interfaces** for clean architecture and abstraction:

### 1. IGoalRepository
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/IGoalRepository.kt`

**Purpose:** Abstraction for goal data management operations.

**Methods:**
- `val goals: StateFlow<List<Goal>>` - Observable flow of all goals
- `suspend fun addGoal(goal: Goal)` - Add a new goal
- `suspend fun updateGoal(updatedGoal: Goal)` - Update an existing goal
- `suspend fun deleteGoal(goalId: String)` - Delete a goal by ID
- `suspend fun getGoal(goalId: String): Goal?` - Get a goal by ID

**Implementation:** `GoalRepository` (object singleton)

**Used in:**
- `GoalsScreen` - Observes goals list
- `GoalInputScreen` - Creates and updates goals
- `GoalDetailScreen` - Retrieves goal details

---

### 2. IAuthService
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/auth/IAuthService.kt`

**Purpose:** Abstraction for user authentication and registration.

**Methods:**
- `suspend fun register(name: String, email: String, password: String): AuthResult`
- `suspend fun login(email: String, password: String): AuthResult`
- `suspend fun logout()`
- `suspend fun getCurrentUser(): User?`
- `suspend fun isLoggedIn(): Boolean`

**Result Types:**
- `AuthResult.Success(user: User)`
- `AuthResult.Error(message: String)`

**Implementation:** `MockAuthService` (object singleton)

**Used in:**
- `LoginScreen` - User authentication
- `RegisterScreen` - User registration

---

### 3. IFriendService
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/friends/IFriendService.kt`

**Purpose:** Abstraction for friend management operations.

**Methods:**
- `val friends: StateFlow<List<User>>` - Observable flow of all friends
- `suspend fun addFriend(email: String): FriendResult`
- `suspend fun removeFriend(friendId: String)`
- `suspend fun getFriend(friendId: String): User?`
- `suspend fun searchFriends(query: String): List<User>`
- `suspend fun getSharedGoalsCount(friendId: String): Int`

**Result Types:**
- `FriendResult.Success(friend: User)`
- `FriendResult.Error(message: String)`

**Implementation:** `MockFriendService` (object singleton)

**Used in:**
- `FriendsScreen` - Friend management

---

### 4. INotificationService
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/notifications/INotificationService.kt`

**Purpose:** Abstraction for notification management.

**Methods:**
- `val notifications: StateFlow<List<Notification>>` - Observable flow of notifications
- `val unreadCount: StateFlow<Int>` - Observable unread count
- `suspend fun markAsRead(notificationId: String)`
- `suspend fun markAllAsRead()`
- `suspend fun deleteNotification(notificationId: String)`
- `suspend fun acceptFriendRequest(notificationId: String): Boolean`
- `suspend fun declineFriendRequest(notificationId: String)`

**Implementation:** `MockNotificationService` (object singleton)

**Used in:**
- `NotificationsScreen` - Notification handling

---

### 5. ITaskService ⭐ NEW
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/tasks/ITaskService.kt`

**Purpose:** Comprehensive interface for task management and hours logging.

**Task Management Methods:**
- `suspend fun addTaskToGoal(goalId: String, task: Task): TaskResult`
- `suspend fun updateTask(goalId: String, task: Task): TaskResult`
- `suspend fun deleteTask(goalId: String, taskId: String): Boolean`
- `suspend fun getTask(goalId: String, taskId: String): Task?`
- `suspend fun getTasksForGoal(goalId: String): List<Task>`

**Hours Logging Methods:**
- `suspend fun logHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult`
- `suspend fun updateLoggedHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult`

**Task Status Methods:**
- `suspend fun markTaskAsCompleted(goalId: String, taskId: String): TaskResult`
- `suspend fun markTaskAsIncomplete(goalId: String, taskId: String): TaskResult`

**Utility Methods:**
- `fun calculateTaskProgress(task: Task): Int`
- `fun validateHoursToLog(task: Task, hours: Float): Boolean`

**Result Types:**
- `TaskResult.Success(task: Task)` / `TaskResult.Error(message: String)`
- `HoursLoggingResult.Success(task: Task, totalLoggedHours: Float)` / `HoursLoggingResult.Error(message: String)`

**Implementation:** `MockTaskService` (object singleton)

**Used in:**
- `GoalInputScreen` - Task CRUD operations
- `GoalDetailScreen` - Hours logging and task management

---

## Components

### Android Components

#### MainActivity
- **Type:** Class (extends `ComponentActivity`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/MainActivity.kt`
- **Purpose:** Application entry point, initializes navigation and theme
- **Key Features:**
  - Sets up Jetpack Compose
  - Initializes navigation graph
  - Applies AchieviaTheme

---

### Data Layer Components

#### GoalRepository
- **Type:** Object (Singleton, implements `IGoalRepository`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/GoalRepository.kt`
- **Purpose:** Centralized data repository managing goals and tasks
- **Public API:**
  - `goals: StateFlow<List<Goal>>` - Observable goals list
  - `suspend fun addGoal(goal: Goal)`
  - `suspend fun updateGoal(updatedGoal: Goal)`
  - `suspend fun deleteGoal(goalId: String)`
  - `suspend fun getGoal(goalId: String): Goal?`
- **State Management:** Uses `MutableStateFlow` for reactive updates

#### MockAuthService
- **Type:** Object (Singleton, implements `IAuthService`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/auth/MockAuthService.kt`
- **Purpose:** Mock authentication service with validation
- **Features:**
  - User registration and login
  - Password validation
  - Email validation
  - Session management

#### MockFriendService
- **Type:** Object (Singleton, implements `IFriendService`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/friends/MockFriendService.kt`
- **Purpose:** Mock friend management service
- **Features:**
  - Add/remove friends
  - Search friends
  - Calculate shared goals count

#### MockNotificationService
- **Type:** Object (Singleton, implements `INotificationService`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/notifications/MockNotificationService.kt`
- **Purpose:** Mock notification service
- **Features:**
  - Notification management
  - Read/unread tracking
  - Friend request handling

#### MockTaskService ⭐ NEW
- **Type:** Object (Singleton, implements `ITaskService`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/tasks/MockTaskService.kt`
- **Purpose:** Mock task management and hours logging service
- **Features:**
  - Task CRUD operations
  - Hours logging (incremental and absolute)
  - Task completion status
  - Automatic goal progress calculation
  - Comprehensive validation

---

### Domain Model Components

#### User
- **Type:** Data Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/model/Models.kt`
- **Properties:**
  - `id: String` (auto-generated UUID)
  - `name: String`
  - `email: String`

#### Goal
- **Type:** Data Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/model/Models.kt`
- **Properties:**
  - `id: String` (auto-generated UUID)
  - `title: String`
  - `description: String`
  - `progress: Int` (0-100)
  - `sharedWith: List<String>` - List of friend names
  - `tasks: List<Task>` - Associated tasks

#### Task
- **Type:** Data Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/model/Models.kt`
- **Properties:**
  - `id: String` (auto-generated UUID)
  - `title: String`
  - `description: String`
  - `isCompleted: Boolean`
  - `allocatedHours: Float`
  - `loggedHours: Float`

#### Notification
- **Type:** Data Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/notifications/INotificationService.kt`
- **Properties:**
  - `id: String`
  - `userId: String`
  - `userName: String`
  - `userInitials: String`
  - `message: String`
  - `timestamp: Long`
  - `type: NotificationType`
  - `relatedId: String?`

#### NotificationType
- **Type:** Enum Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/notifications/INotificationService.kt`
- **Values:**
  - `FriendRequest`
  - `SharedGoal`
  - `SharedTask`
  - `GoalUpdate`
  - `TaskCompleted`

---

### Navigation Components

#### Screen (Sealed Class)
- **Type:** Sealed Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/navigation/Screen.kt`
- **Purpose:** Type-safe navigation routes
- **Screen Objects:**
  - `Login` - Login screen route
  - `Register` - Registration screen route
  - `Goals` - Main goals list screen route
  - `CreateGoal` - Create new goal screen route
  - `EditGoal` - Edit goal screen route (parameterized with goalId)
  - `GoalDetail` - Goal detail screen route (parameterized with goalId)
  - `Friends` - Friends management screen route
  - `Notifications` - Notifications screen route
  - `Settings` - Settings screen route

#### AchieviaNavGraph
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/navigation/AchieviaNavGraph.kt`
- **Purpose:** Navigation graph defining all screen routes and transitions
- **Features:**
  - Handles navigation between all screens
  - Manages navigation stack
  - Passes parameters between screens

---

### UI Screen Components (Composable Functions)

#### LoginScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/LoginScreen.kt`
- **Parameters:**
  - `authService: IAuthService = MockAuthService`
  - `onLoginSuccess: () -> Unit`
  - `onNavigateToRegister: () -> Unit`
- **Features:**
  - Email and password input fields
  - Password visibility toggle
  - Error message display
  - Loading state during authentication
  - Navigation to registration screen

#### RegisterScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/RegisterScreen.kt`
- **Parameters:**
  - `authService: IAuthService = MockAuthService`
  - `onRegisterSuccess: () -> Unit`
  - `onNavigateToLogin: () -> Unit`
- **Features:**
  - Name, email, and password input fields
  - Password visibility toggle
  - Error message display
  - Loading state during registration
  - Navigation to login screen

#### GoalsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/GoalsScreen.kt`
- **Parameters:**
  - `goalRepository: IGoalRepository = GoalRepository`
  - `onNavigateToCreateGoal: () -> Unit`
  - `onNavigateToEditGoal: (String) -> Unit`
  - `onNavigateToGoalDetail: (String) -> Unit`
  - `onNavigateToFriends: () -> Unit`
  - `onNavigateToNotifications: () -> Unit`
  - `onNavigateToSettings: () -> Unit`
  - `onLogout: () -> Unit`
- **Features:**
  - Displays list of goals with progress indicators
  - Navigation drawer with menu items
  - Floating action button to create new goal
  - Goal cards with edit functionality
- **Sub-Components:**
  - `DrawerHeader()` - Navigation drawer header
  - `GoalItem()` - Individual goal card component
  - `CircularProgress()` - Circular progress indicator

#### GoalDetailScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/GoalDetailScreen.kt`
- **Parameters:**
  - `goalRepository: IGoalRepository = GoalRepository`
  - `taskService: ITaskService = MockTaskService` ⭐ NEW
  - `goalId: String?`
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Displays goal details and associated tasks
  - Task list with progress tracking
  - Log hours functionality for tasks
  - Error message display
- **Sub-Components:**
  - `GoalDetailHeader()` - Top app bar with goal info
  - `GoalProgressIndicator()` - Progress display
  - `TaskCard()` - Individual task card (uses ITaskService)
  - `LogHoursDialog()` - Dialog for logging hours

#### GoalInputScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/GoalInputScreen.kt`
- **Parameters:**
  - `goalRepository: IGoalRepository = GoalRepository`
  - `taskService: ITaskService = MockTaskService` ⭐ NEW
  - `goalId: String?` (optional, for edit mode)
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Create or edit goals
  - Add/remove tasks using ITaskService
  - Save or cancel changes
  - Error message display
- **Sub-Components:**
  - `TaskItem()` - Task list item with delete option
  - `AddTaskDialog()` - Dialog for adding new tasks

#### FriendsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/FriendsScreen.kt`
- **Parameters:**
  - `friendService: IFriendService = MockFriendService`
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Display friends list
  - Add new friends
  - Remove friends
  - Error message display
- **Sub-Components:**
  - `FriendCard()` - Individual friend card
  - `AddFriendDialog()` - Dialog for adding friends
- **Supporting Data:**
  - `FriendUi` - Data class for friend UI representation

#### NotificationsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/NotificationsScreen.kt`
- **Parameters:**
  - `notificationService: INotificationService = MockNotificationService`
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Display notifications list
  - Handle friend requests (accept/decline)
  - View shared goals/tasks
  - Dismiss notifications
- **Sub-Components:**
  - `NotificationCard()` - Individual notification card
- **Supporting Data:**
  - `NotificationUi` - Data class for notification UI representation
  - `NotificationType` - Enum for notification types (UI version)

#### SettingsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/SettingsScreen.kt`
- **Parameters:**
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Profile settings
  - Change password
  - Notification preferences
  - Delete profile (danger zone)
- **Sub-Components:**
  - `SettingsItem()` - Reusable settings item component

---

### Theme Components

#### AchieviaTheme
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/theme/Theme.kt`
- **Purpose:** Application theme wrapper
- **Features:**
  - Supports light/dark mode
  - Dynamic color support (Android 12+)
  - Material 3 theming

#### Color Theme
- **Type:** Color Constants
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/theme/Color.kt`
- **Constants:**
  - `Orange500` - Primary brand color (#FF6F43)
  - Material 3 color scheme colors

#### Typography
- **Type:** Typography Configuration
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/theme/Type.kt`
- **Purpose:** Text styling configuration

---

### Supporting Data Classes

#### FriendUi
- **Type:** Data Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/FriendsScreen.kt`
- **Properties:**
  - `id: String`
  - `name: String`
  - `email: String`
  - `sharedGoalsCount: Int`

#### NotificationUi
- **Type:** Data Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/NotificationsScreen.kt`
- **Properties:**
  - `id: String`
  - `initials: String`
  - `name: String`
  - `message: String`
  - `timeAgo: String`
  - `type: NotificationType`

#### NotificationType (UI)
- **Type:** Enum Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/NotificationsScreen.kt`
- **Values:**
  - `FriendRequest`
  - `SharedGoal`
  - `SharedTask`

---

## Component Summary

### By Type:
- **Interfaces:** 5
  - IGoalRepository
  - IAuthService
  - IFriendService
  - INotificationService
  - ITaskService ⭐ NEW
- **Classes:** 2 (MainActivity, Screen sealed class)
- **Objects (Singletons):** 5 (GoalRepository, MockAuthService, MockFriendService, MockNotificationService, MockTaskService)
- **Data Classes:** 7 (User, Goal, Task, Notification, FriendUi, NotificationUi)
- **Enum Classes:** 2 (NotificationType - service, NotificationType - UI)
- **Composable Functions:** 20+ (All UI screens and sub-components)
- **Sealed Class Objects:** 9 (Screen route objects)

### By Layer:
- **Presentation Layer:** 20+ components (Screens, Navigation, Theme)
- **Data Layer:** 5 service implementations (GoalRepository + 4 Mock Services)
- **Domain Layer:** 3 components (User, Goal, Task models)
- **Android Framework:** 1 component (MainActivity)

### Total Components: **40+**

---

## Interface Implementation Summary

| Interface | Implementation | Used In |
|-----------|---------------|---------|
| `IGoalRepository` | `GoalRepository` | GoalsScreen, GoalInputScreen, GoalDetailScreen |
| `IAuthService` | `MockAuthService` | LoginScreen, RegisterScreen |
| `IFriendService` | `MockFriendService` | FriendsScreen |
| `INotificationService` | `MockNotificationService` | NotificationsScreen |
| `ITaskService` ⭐ | `MockTaskService` | GoalInputScreen, GoalDetailScreen |

---

## Architecture Notes

1. **Interface-Based Design:** All major functionality is abstracted through interfaces, enabling:
   - Easy testing with mock implementations
   - Future backend integration without UI changes
   - Dependency injection support

2. **Repository Pattern:** `GoalRepository` acts as a singleton repository implementing `IGoalRepository`

3. **Service Pattern:** All business logic is encapsulated in service interfaces with mock implementations:
   - Authentication service
   - Friend management service
   - Notification service
   - Task management service ⭐ NEW

4. **Composable Architecture:** All UI is built using Jetpack Compose composable functions, following a declarative UI pattern

5. **State Management:** Uses Kotlin StateFlow for reactive state management across all services

6. **Navigation:** Type-safe navigation using sealed class with route objects

7. **Async Operations:** All service methods are `suspend` functions, ready for:
   - Network calls
   - Database operations
   - Background processing

---

## Future Enhancements

Consider adding interfaces for:
- Settings management (ISettingsService)
- Analytics tracking (IAnalyticsService)
- File/Image management (IFileService)
- Offline sync (ISyncService)
- Push notifications (IPushNotificationService)

---

## Summary

✅ **5 Interfaces Created:**
- IGoalRepository
- IAuthService
- IFriendService
- INotificationService
- ITaskService ⭐ NEW

✅ **5 Service Implementations:**
- GoalRepository (implements IGoalRepository)
- MockAuthService
- MockFriendService
- MockNotificationService
- MockTaskService ⭐ NEW

✅ **8 UI Screens:**
- All screens now use service interfaces
- Default parameters allow easy testing
- Ready for dependency injection

✅ **Benefits:**
- Clean architecture
- Testable code
- Easy migration to real backend
- Separation of concerns
- Reactive state management
- Comprehensive task and hours logging functionality ⭐ NEW

