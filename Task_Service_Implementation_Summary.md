# Task Service and Hours Logging Implementation Summary

This document summarizes the new interfaces created for task management and hours logging functionality.

## Created Interface

### ITaskService
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/tasks/ITaskService.kt`

**Purpose:** Comprehensive interface for task management and hours logging operations.

## Interface Methods

### Task Management Operations

#### 1. `addTaskToGoal(goalId: String, task: Task): TaskResult`
- Adds a new task to a goal
- Validates that goal exists
- Prevents duplicate task IDs
- Returns `TaskResult.Success` or `TaskResult.Error`

#### 2. `updateTask(goalId: String, task: Task): TaskResult`
- Updates an existing task
- Validates goal and task existence
- Returns updated task on success

#### 3. `deleteTask(goalId: String, taskId: String): Boolean`
- Deletes a task from a goal
- Returns `true` if successful, `false` otherwise

#### 4. `getTask(goalId: String, taskId: String): Task?`
- Retrieves a specific task by ID
- Returns `null` if not found

#### 5. `getTasksForGoal(goalId: String): List<Task>`
- Gets all tasks for a specific goal
- Returns empty list if goal not found

### Hours Logging Operations

#### 6. `logHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult`
- **Adds** hours to existing logged hours (incremental)
- Validates hours are positive
- Validates total doesn't exceed allocated hours
- Automatically recalculates goal progress
- Returns `HoursLoggingResult.Success` with updated task and total hours

#### 7. `updateLoggedHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult`
- **Sets** logged hours to absolute value (replacement)
- Validates hours are not negative
- Validates hours don't exceed allocated hours
- Automatically recalculates goal progress
- Returns `HoursLoggingResult.Success` with updated task

### Task Status Operations

#### 8. `markTaskAsCompleted(goalId: String, taskId: String): TaskResult`
- Marks a task as completed
- Automatically recalculates goal progress
- Returns updated task on success

#### 9. `markTaskAsIncomplete(goalId: String, taskId: String): TaskResult`
- Marks a task as incomplete
- Automatically recalculates goal progress
- Returns updated task on success

### Utility Methods

#### 10. `calculateTaskProgress(task: Task): Int`
- Calculates task progress percentage (0-100)
- Based on loggedHours / allocatedHours ratio
- Returns 0 if no hours allocated

#### 11. `validateHoursToLog(task: Task, hours: Float): Boolean`
- Validates if hours can be logged
- Checks if total would exceed allocated hours
- Returns `true` if valid, `false` otherwise

## Result Types

### TaskResult
```kotlin
sealed class TaskResult {
    data class Success(val task: Task) : TaskResult()
    data class Error(val message: String) : TaskResult()
}
```

### HoursLoggingResult
```kotlin
sealed class HoursLoggingResult {
    data class Success(val task: Task, val totalLoggedHours: Float) : HoursLoggingResult()
    data class Error(val message: String) : HoursLoggingResult()
}
```

## Implementation

### MockTaskService
**Location:** `app/src/main/java/com/htw/proitd/achievia/data/tasks/MockTaskService.kt`

**Features:**
- ✅ Uses `IGoalRepository` to manage tasks as part of goals
- ✅ Simulates network delays (100-300ms)
- ✅ Validates all inputs
- ✅ Automatically recalculates goal progress after task updates
- ✅ Prevents invalid operations (negative hours, exceeding limits)
- ✅ Comprehensive error handling

**Goal Progress Calculation:**
- If tasks have allocated hours: Uses total logged hours / total allocated hours
- If no allocated hours: Uses task completion count
- Progress is always between 0-100

## Updated UI Screens

### 1. GoalInputScreen
**Changes:**
- Now accepts `ITaskService` parameter (default: `MockTaskService`)
- Uses `taskService.addTaskToGoal()` when adding tasks to existing goals
- Uses `taskService.deleteTask()` when deleting tasks from existing goals
- Shows error messages for failed operations
- For new goals (no goalId), still uses local state management

**Benefits:**
- Proper validation through service layer
- Consistent error handling
- Automatic goal progress updates

### 2. GoalDetailScreen
**Changes:**
- Now accepts `ITaskService` parameter (default: `MockTaskService`)
- Uses `taskService.logHours()` for hours logging
- Uses `taskService.calculateTaskProgress()` for progress display
- Shows error messages for failed operations
- Displays error dialog for validation failures

**Benefits:**
- Centralized hours logging logic
- Automatic goal progress recalculation
- Better error feedback to users

## Usage Examples

### Adding a Task
```kotlin
scope.launch {
    val result = taskService.addTaskToGoal(goalId, newTask)
    when (result) {
        is TaskResult.Success -> {
            // Task added successfully
            tasks = tasks + result.task
        }
        is TaskResult.Error -> {
            // Show error message
            errorMessage = result.message
        }
    }
}
```

### Logging Hours
```kotlin
scope.launch {
    val result = taskService.logHours(goalId, taskId, 2.5f)
    when (result) {
        is HoursLoggingResult.Success -> {
            // Hours logged successfully
            // Goal progress automatically updated
            println("Total logged: ${result.totalLoggedHours}")
        }
        is HoursLoggingResult.Error -> {
            // Show error message
            errorMessage = result.message
        }
    }
}
```

### Marking Task Complete
```kotlin
scope.launch {
    val result = taskService.markTaskAsCompleted(goalId, taskId)
    when (result) {
        is TaskResult.Success -> {
            // Task marked as completed
            // Goal progress automatically updated
        }
        is TaskResult.Error -> {
            errorMessage = result.message
        }
    }
}
```

## Validation Rules

### Hours Logging Validation
1. ✅ Hours must be greater than zero
2. ✅ Total logged hours cannot exceed allocated hours
3. ✅ Goal and task must exist
4. ✅ Hours cannot be negative

### Task Management Validation
1. ✅ Goal must exist before adding/updating/deleting tasks
2. ✅ Task must exist before updating/deleting
3. ✅ Duplicate task IDs are prevented

## Automatic Features

### Goal Progress Recalculation
The service automatically recalculates goal progress when:
- Hours are logged for a task
- Task is marked as completed/incomplete
- Task is updated

**Calculation Method:**
- **With allocated hours:** `(totalLoggedHours / totalAllocatedHours) * 100`
- **Without allocated hours:** `(completedTasks / totalTasks) * 100`
- Progress is always clamped between 0-100

## Architecture Benefits

### 1. Separation of Concerns
- UI screens handle presentation only
- Service layer handles business logic
- Repository layer handles data persistence

### 2. Testability
- Easy to mock `ITaskService` for unit tests
- Can test business logic independently
- Can test UI separately from service logic

### 3. Maintainability
- All task-related operations in one place
- Consistent error handling
- Easy to add new features

### 4. Future-Ready
- Ready for backend API integration
- Can add caching layer
- Can add offline support
- Can add analytics tracking

## Migration Path to Real Backend

### Step 1: Create Real Implementation
```kotlin
object RealTaskService : ITaskService {
    override suspend fun logHours(goalId: String, taskId: String, hours: Float): HoursLoggingResult {
        val response = apiClient.logTaskHours(goalId, taskId, hours)
        return if (response.isSuccess) {
            HoursLoggingResult.Success(response.task, response.totalHours)
        } else {
            HoursLoggingResult.Error(response.message)
        }
    }
    // ... other methods
}
```

### Step 2: Update Screens
```kotlin
GoalDetailScreen(
    taskService = RealTaskService, // Instead of MockTaskService
    ...
)
```

## Files Created

1. `ITaskService.kt` - Task service interface
2. `MockTaskService.kt` - Mock implementation

## Files Updated

1. `GoalInputScreen.kt` - Uses `ITaskService` for task management
2. `GoalDetailScreen.kt` - Uses `ITaskService` for hours logging

## Summary

✅ **1 New Interface Created:**
- `ITaskService` - Comprehensive task and hours logging interface

✅ **1 Mock Implementation:**
- `MockTaskService` - Full-featured mock with validation

✅ **2 UI Screens Updated:**
- `GoalInputScreen` - Task CRUD operations
- `GoalDetailScreen` - Hours logging functionality

✅ **Key Features:**
- Task CRUD operations
- Hours logging (incremental and absolute)
- Task completion status
- Automatic goal progress calculation
- Comprehensive validation
- Error handling
- Ready for backend integration

The task management and hours logging functionality is now properly abstracted through interfaces, making the codebase more maintainable, testable, and ready for future backend integration.

