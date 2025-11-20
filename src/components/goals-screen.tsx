import { useState } from "react";
import { GoalItem } from "./goal-item";
import { EditGoalDialog, Task } from "./edit-goal-dialog";
import { Button } from "./ui/button";
import { Plus, Menu } from "lucide-react";

export interface Goal {
  id: string;
  title: string;
  description: string;
  progress: number;
  tasks?: Task[];
  isShared?: boolean;
  sharedWith?: string[];
}

interface GoalsScreenProps {
  onMenuClick: () => void;
  onGoalClick: (goal: Goal) => void;
  friends: Array<{ id: string; name: string }>;
  goals: Goal[];
  onUpdateGoals: (goals: Goal[]) => void;
}

export function GoalsScreen({ onMenuClick, onGoalClick, friends, goals, onUpdateGoals }: GoalsScreenProps) {
  const [editingGoal, setEditingGoal] = useState<Goal | null>(null);

  const handleEditGoal = (goal: Goal) => {
    setEditingGoal(goal);
  };

  const handleSaveGoal = (updatedGoal: Goal) => {
    onUpdateGoals(goals.map((g) => (g.id === updatedGoal.id ? updatedGoal : g)));
    setEditingGoal(null);
  };

  const handleAddGoal = () => {
    const newGoal: Goal = {
      id: Date.now().toString(),
      title: "New Goal",
      description: "Add description here",
      progress: 0,
      tasks: [],
      isShared: false,
    };
    setEditingGoal(newGoal);
  };

  const handleSaveNewGoal = (newGoal: Goal) => {
    onUpdateGoals([...goals, newGoal]);
    setEditingGoal(null);
  };

  return (
    <div className="min-h-screen bg-white flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#FF6B4A] to-[#FF8A6F] px-6 pt-12 pb-6">
        <div className="flex items-center justify-between mb-6">
          <button onClick={onMenuClick} className="text-white">
            <Menu className="size-6" />
          </button>
        </div>
        <h1 className="text-white mb-2">My Goals</h1>
        <p className="text-white/90">Keep pushing towards your dreams</p>
      </div>

      {/* Goals List */}
      <div className="flex-1 px-6 py-6 space-y-4 overflow-y-auto">
        {goals.map((goal) => (
          <GoalItem
            key={goal.id}
            goal={goal}
            onEdit={handleEditGoal}
            onClick={() => onGoalClick(goal)}
          />
        ))}
      </div>

      {/* Add Goal Button */}
      <div className="p-6">
        <Button
          onClick={handleAddGoal}
          className="w-full h-14 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
        >
          <Plus className="size-5 mr-2" />
          Add New Goal
        </Button>
      </div>

      {/* Edit Dialog */}
      {editingGoal && (
        <EditGoalDialog
          goal={editingGoal}
          isNew={!goals.find((g) => g.id === editingGoal.id)}
          onSave={
            goals.find((g) => g.id === editingGoal.id)
              ? handleSaveGoal
              : handleSaveNewGoal
          }
          onClose={() => setEditingGoal(null)}
          friends={friends}
        />
      )}
    </div>
  );
}