import { useState } from "react";
import { LoginScreen } from "./components/login-screen";
import { GoalsScreen } from "./components/goals-screen";
import { FriendsScreen } from "./components/friends-screen";
import { NotificationsScreen } from "./components/notifications-screen";
import { SettingsScreen } from "./components/settings-screen";
import { TaskListScreen } from "./components/task-list-screen";
import { MyTasksScreen } from "./components/my-tasks-screen";
import { SideMenu } from "./components/side-menu";
import type { Goal } from "./components/goals-screen";

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [currentScreen, setCurrentScreen] = useState<"goals" | "friends" | "notifications" | "settings" | "taskList" | "myTasks">("goals");
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [selectedGoal, setSelectedGoal] = useState<Goal | null>(null);
  
  // Initialize with sample goals including tasks
  const [goals, setGoals] = useState<Goal[]>([
    {
      id: "1",
      title: "Complete React Course",
      description: "Finish all modules and build final project",
      progress: 75,
      isShared: true,
      sharedWith: ["Sarah Johnson"],
      tasks: [
        {
          id: "t1",
          title: "Learn Hooks",
          description: "Complete useState, useEffect, useContext modules",
          allocatedHours: 10,
          workedHours: 10,
          isShared: false,
        },
        {
          id: "t2",
          title: "Build Dashboard",
          description: "Create responsive admin dashboard",
          allocatedHours: 20,
          workedHours: 12,
          isShared: true,
          sharedWith: ["Sarah Johnson"],
        },
        {
          id: "t3",
          title: "Testing",
          description: "Write unit tests for components",
          allocatedHours: 10,
          workedHours: 3,
          isShared: false,
        },
      ],
    },
    {
      id: "2",
      title: "Morning Workout Routine",
      description: "Exercise 5 days a week for 30 minutes",
      progress: 60,
      isShared: false,
      tasks: [
        {
          id: "t4",
          title: "Cardio Sessions",
          description: "Running and cycling workouts",
          allocatedHours: 15,
          workedHours: 9,
          isShared: false,
        },
      ],
    },
    {
      id: "3",
      title: "Read 12 Books This Year",
      description: "Read at least one book per month",
      progress: 42,
      isShared: true,
      sharedWith: ["Mike Chen", "Emily Davis"],
      tasks: [],
    },
    {
      id: "4",
      title: "Learn Spanish",
      description: "Practice daily on language learning app",
      progress: 25,
      isShared: false,
      tasks: [],
    },
    {
      id: "5",
      title: "Save $5000",
      description: "Build emergency fund by end of year",
      progress: 88,
      isShared: false,
      tasks: [],
    },
  ]);

  // Mock friends data
  const friends = [
    { id: "1", name: "Sarah Johnson" },
    { id: "2", name: "Mike Chen" },
    { id: "3", name: "Emily Davis" },
  ];

  const handleGoalClick = (goal: Goal) => {
    setSelectedGoal(goal);
    setCurrentScreen("taskList");
  };

  const handleUpdateGoal = (updatedGoal: Goal) => {
    setGoals(prevGoals => 
      prevGoals.map(g => g.id === updatedGoal.id ? updatedGoal : g)
    );
    if (selectedGoal && selectedGoal.id === updatedGoal.id) {
      setSelectedGoal(updatedGoal);
    }
  };

  const handleUpdateGoals = (updatedGoals: Goal[]) => {
    setGoals(updatedGoals);
  };

  const handleBackFromTaskList = () => {
    setCurrentScreen("goals");
    setSelectedGoal(null);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 p-4">
      {/* Mobile device frame */}
      <div className="w-full max-w-sm">
        {isLoggedIn ? (
          <>
            {currentScreen === "goals" && (
              <GoalsScreen 
                onMenuClick={() => setIsMenuOpen(true)}
                onGoalClick={handleGoalClick}
                friends={friends}
                goals={goals}
                onUpdateGoals={handleUpdateGoals}
              />
            )}
            {currentScreen === "myTasks" && (
              <MyTasksScreen
                onMenuClick={() => setIsMenuOpen(true)}
                goals={goals}
                onUpdateGoal={handleUpdateGoal}
                friends={friends}
              />
            )}
            {currentScreen === "friends" && (
              <FriendsScreen onMenuClick={() => setIsMenuOpen(true)} />
            )}
            {currentScreen === "notifications" && (
              <NotificationsScreen onMenuClick={() => setIsMenuOpen(true)} />
            )}
            {currentScreen === "settings" && (
              <SettingsScreen onMenuClick={() => setIsMenuOpen(true)} />
            )}
            {currentScreen === "taskList" && selectedGoal && (
              <TaskListScreen 
                goal={selectedGoal}
                onBack={handleBackFromTaskList}
                onUpdateGoal={handleUpdateGoal}
              />
            )}
            <SideMenu
              isOpen={isMenuOpen}
              onClose={() => setIsMenuOpen(false)}
              onNavigate={setCurrentScreen}
            />
          </>
        ) : (
          <LoginScreen onLogin={() => setIsLoggedIn(true)} />
        )}
      </div>
    </div>
  );
}
