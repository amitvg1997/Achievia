# Achievia - Interfaces and Components List

This document provides a comprehensive list of all interfaces and components implemented in the Achievia Android application.

## Interfaces

**Note:** The codebase does not currently implement any explicit Kotlin interfaces. The architecture uses:
- Data classes for models
- Sealed classes for navigation
- Objects (singletons) for repositories
- Composable functions for UI components

## Components

### 1. Android Components

#### MainActivity
- **Type:** Class (extends `ComponentActivity`)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/MainActivity.kt`
- **Purpose:** Application entry point, initializes navigation and theme
- **Key Features:**
  - Sets up Jetpack Compose
  - Initializes navigation graph
  - Applies AchieviaTheme

---

### 2. Data Layer Components

#### GoalRepository
- **Type:** Object (Singleton)
- **Location:** `app/src/main/java/com/htw/proitd/achievia/data/GoalRepository.kt`
- **Purpose:** Centralized data repository managing goals and tasks
- **Public API:**
  - `goals: StateFlow<List<Goal>>` - Observable goals list
  - `addGoal(goal: Goal)` - Add new goal
  - `updateGoal(updatedGoal: Goal)` - Update existing goal
  - `deleteGoal(goalId: String)` - Delete goal by ID
  - `getGoal(goalId: String): Goal?` - Retrieve goal by ID
- **State Management:** Uses `MutableStateFlow` for reactive updates

---

### 3. Domain Model Components

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

---

### 4. Navigation Components

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

### 5. UI Screen Components (Composable Functions)

#### LoginScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/LoginScreen.kt`
- **Parameters:**
  - `onLoginSuccess: () -> Unit`
  - `onNavigateToRegister: () -> Unit`
- **Features:**
  - Email and password input fields
  - Password visibility toggle
  - Forgot password link
  - Navigation to registration screen

#### RegisterScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/RegisterScreen.kt`
- **Parameters:**
  - `onRegisterSuccess: () -> Unit`
  - `onNavigateToLogin: () -> Unit`
- **Features:**
  - Name, email, and password input fields
  - Password visibility toggle
  - Navigation to login screen

#### GoalsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/GoalsScreen.kt`
- **Parameters:**
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
  - `goalId: String?`
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Displays goal details and associated tasks
  - Task list with progress tracking
  - Log hours functionality for tasks
- **Sub-Components:**
  - `GoalDetailHeader()` - Top app bar with goal info
  - `GoalProgressIndicator()` - Progress display
  - `TaskCard()` - Individual task card
  - `LogHoursDialog()` - Dialog for logging hours

#### GoalInputScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/GoalInputScreen.kt`
- **Parameters:**
  - `goalId: String?` (optional, for edit mode)
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Create or edit goals
  - Add/remove tasks
  - Save or cancel changes
- **Sub-Components:**
  - `TaskItem()` - Task list item with delete option
  - `AddTaskDialog()` - Dialog for adding new tasks

#### FriendsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/FriendsScreen.kt`
- **Parameters:**
  - `onNavigateBack: () -> Unit`
- **Features:**
  - Display friends list
  - Add new friends
  - Remove friends
- **Sub-Components:**
  - `FriendCard()` - Individual friend card
  - `AddFriendDialog()` - Dialog for adding friends
- **Supporting Data:**
  - `FriendUi` - Data class for friend UI representation

#### NotificationsScreen
- **Type:** Composable Function
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/NotificationsScreen.kt`
- **Parameters:**
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
  - `NotificationType` - Enum for notification types (FriendRequest, SharedGoal, SharedTask)

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

### 6. Theme Components

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

### 7. Supporting Data Classes

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

#### NotificationType
- **Type:** Enum Class
- **Location:** `app/src/main/java/com/htw/proitd/achievia/ui/screens/NotificationsScreen.kt`
- **Values:**
  - `FriendRequest`
  - `SharedGoal`
  - `SharedTask`

---

## Component Summary

### By Type:
- **Classes:** 2 (MainActivity, Screen sealed class)
- **Objects:** 1 (GoalRepository singleton)
- **Data Classes:** 6 (User, Goal, Task, FriendUi, NotificationUi)
- **Enum Classes:** 1 (NotificationType)
- **Composable Functions:** 15+ (All UI screens and sub-components)
- **Sealed Class Objects:** 9 (Screen route objects)

### By Layer:
- **Presentation Layer:** 15+ components (Screens, Navigation, Theme)
- **Data Layer:** 1 component (GoalRepository)
- **Domain Layer:** 3 components (User, Goal, Task models)
- **Android Framework:** 1 component (MainActivity)

### Total Components: **30+**

---

## Architecture Notes

1. **No Explicit Interfaces:** The codebase uses composition and function parameters instead of interfaces for dependency injection and abstraction.

2. **Repository Pattern:** `GoalRepository` acts as a singleton repository, but doesn't implement an interface (could be abstracted in future).

3. **Composable Architecture:** All UI is built using Jetpack Compose composable functions, following a declarative UI pattern.

4. **State Management:** Uses Kotlin StateFlow for reactive state management.

5. **Navigation:** Type-safe navigation using sealed class with route objects.

6. **Future Enhancements:** Consider adding interfaces for:
   - Repository abstraction (IGoalRepository)
   - Authentication service
   - Notification service
   - Friend management service

