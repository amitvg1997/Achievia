# Achievia UML Diagrams Documentation

This directory contains comprehensive UML diagrams for the Achievia Android goal-tracking application.

## Overview

Achievia is an Android application built with Jetpack Compose that helps users track their goals, manage tasks, collaborate with friends, and monitor progress. The application follows a clean architecture pattern with clear separation between presentation, data, and domain layers.

## Diagram Files

### 1. Component Diagram
**File:** `UML_Component_Diagram.puml`

This diagram illustrates the overall architecture of the Achievia application, showing:
- **Presentation Layer**: All UI screens, navigation, and theming components
- **Data Layer**: Repository pattern for data management
- **Domain Layer**: Core data models (User, Goal, Task)
- **Android Framework**: Dependencies on Jetpack Compose, Navigation, StateFlow, and Coroutines

**Key Components:**
- `MainActivity`: Application entry point
- `AchieviaNavGraph`: Navigation controller managing screen transitions
- `GoalRepository`: Singleton repository managing in-memory state with StateFlow
- UI Screens: Login, Register, Goals, GoalDetail, GoalInput, Friends, Notifications, Settings

### 2. Sequence Diagrams

The following sequence diagrams detail the key user flows in the application:

#### 2.1 Registration Flow
**File:** `UML_Sequence_01_Registration.puml`
- User registration process
- Navigation from RegisterScreen to GoalsScreen
- Initial goal list loading

#### 2.2 Login Flow
**File:** `UML_Sequence_02_Login.puml`
- User authentication process
- Navigation to main GoalsScreen
- StateFlow observation for goals

#### 2.3 Create Goal Flow
**File:** `UML_Sequence_03_CreateGoal.puml`
- Creating a new goal with tasks
- Adding tasks through dialog
- Saving to repository and updating UI

#### 2.4 View Goal & Log Hours Flow
**File:** `UML_Sequence_04_ViewGoal_LogHours.puml`
- Viewing goal details and tasks
- Logging hours for tasks
- Updating progress indicators

#### 2.5 Edit Goal Flow
**File:** `UML_Sequence_05_EditGoal.puml`
- Editing existing goals
- Modifying tasks
- Updating repository and UI

#### 2.6 Friends Management Flow
**File:** `UML_Sequence_06_Friends.puml`
- Adding and removing friends
- Managing friend list (currently in-memory)

#### 2.7 Notifications Flow
**File:** `UML_Sequence_07_Notifications.puml`
- Viewing notifications
- Handling friend requests
- Viewing shared goals/tasks

#### 2.8 Logout Flow
**File:** `UML_Sequence_08_Logout.puml`
- User logout process
- Clearing navigation stack
- Returning to login screen

## How to View the Diagrams

### Option 1: Using PlantUML Online
1. Visit [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
2. Copy the contents of any `.puml` file
3. Paste into the online editor
4. The diagram will be rendered automatically

### Option 2: Using VS Code Extension
1. Install the "PlantUML" extension in VS Code
2. Open any `.puml` file
3. Press `Alt+D` (Windows/Linux) or `Option+D` (Mac) to preview

### Option 3: Using IntelliJ IDEA / Android Studio
1. Install the "PlantUML integration" plugin
2. Open any `.puml` file
3. Right-click and select "Preview Diagram"

### Option 4: Using Command Line
```bash
# Install PlantUML (requires Java)
# macOS: brew install plantuml
# Linux: sudo apt-get install plantuml

# Generate PNG from PlantUML file
plantuml UML_Component_Diagram.puml

# Generate all diagrams
plantuml *.puml
```

## Architecture Overview

### Presentation Layer
- **Technology**: Jetpack Compose
- **Navigation**: Jetpack Navigation Compose
- **State Management**: StateFlow from Kotlin Coroutines
- **Screens**: 8 main screens handling different user interactions

### Data Layer
- **Repository Pattern**: `GoalRepository` singleton
- **State Management**: MutableStateFlow for reactive updates
- **Current Implementation**: In-memory storage with dummy data
- **Future Enhancement**: Backend API integration for persistence

### Domain Layer
- **Models**: 
  - `User`: User information (id, name, email)
  - `Goal`: Goal entity with title, description, progress, tasks, sharedWith
  - `Task`: Task entity with title, description, allocatedHours, loggedHours

## Key Design Patterns

1. **Repository Pattern**: Centralized data access through GoalRepository
2. **Observer Pattern**: StateFlow for reactive UI updates
3. **Navigation Pattern**: Declarative navigation using Jetpack Navigation Compose
4. **MVVM-like Architecture**: Separation of UI (Screens) and data (Repository)

## Current Limitations & Future Enhancements

### Current State
- In-memory data storage (no persistence)
- Dummy authentication (no real backend)
- Local friend management
- Static notification data

### Future Enhancements
- Backend API integration for:
  - User authentication and authorization
  - Persistent goal and task storage
  - Real-time friend management
  - Push notifications
  - Goal sharing and collaboration
- Database integration (Room Database)
- Cloud synchronization
- Offline support

## Application Flow Summary

1. **Authentication**: User logs in or registers
2. **Main Screen**: GoalsScreen displays all user goals
3. **Goal Management**: Create, edit, view, and delete goals
4. **Task Tracking**: Add tasks to goals and log hours
5. **Social Features**: Manage friends and view shared goals
6. **Notifications**: View and respond to friend requests and shared content
7. **Settings**: Manage account preferences

## Notes

- All diagrams use PlantUML syntax
- Diagrams are designed to be self-documenting
- Sequence diagrams show both synchronous and asynchronous operations
- Component diagram shows dependencies and relationships
- Future enhancements are marked with "(Future: ...)" in sequence diagrams

## Contact

For questions or updates to these diagrams, please refer to the project documentation or contact the development team.

