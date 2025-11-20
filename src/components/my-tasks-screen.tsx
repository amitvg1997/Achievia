import { useState } from "react";
import { Button } from "./ui/button";
import { Menu, Clock, Users, User, Edit2 } from "lucide-react";
import type { Goal } from "./goals-screen";
import type { Task } from "./edit-goal-dialog";
import { LogHoursDialog } from "./log-hours-dialog";
import { EditTaskDialog } from "./edit-task-dialog";

interface MyTasksScreenProps {
  onMenuClick: () => void;
  goals: Goal[];
  onUpdateGoal: (goal: Goal) => void;
  friends: Array<{ id: string; name: string }>;
}

export function MyTasksScreen({ onMenuClick, goals, onUpdateGoal, friends }: MyTasksScreenProps) {
  const [loggingTask, setLoggingTask] = useState<{ task: Task; goalId: string } | null>(null);
  const [editingTask, setEditingTask] = useState<{ task: Task; goalId: string } | null>(null);

  // Get all tasks from all goals
  const allTasks = goals.flatMap((goal) =>
    (goal.tasks || []).map((task) => ({ task, goalId: goal.id, goalTitle: goal.title }))
  );

  const handleLogHours = (hours: number) => {
    if (!loggingTask) return;

    const goal = goals.find((g) => g.id === loggingTask.goalId);
    if (!goal) return;

    const updatedTasks = goal.tasks?.map((task) => {
      if (task.id === loggingTask.task.id) {
        return {
          ...task,
          workedHours: task.workedHours + hours,
        };
      }
      return task;
    });

    // Calculate overall progress
    const totalAllocated = updatedTasks?.reduce((sum, t) => sum + t.allocatedHours, 0) || 0;
    const totalWorked = updatedTasks?.reduce((sum, t) => sum + t.workedHours, 0) || 0;
    const progress = totalAllocated > 0 ? Math.min(Math.round((totalWorked / totalAllocated) * 100), 100) : 0;

    onUpdateGoal({
      ...goal,
      tasks: updatedTasks,
      progress,
    });

    setLoggingTask(null);
  };

  const handleSaveTask = (updatedTask: Task) => {
    if (!editingTask) return;

    const goal = goals.find((g) => g.id === editingTask.goalId);
    if (!goal) return;

    const updatedTasks = goal.tasks?.map((task) => {
      if (task.id === updatedTask.id) {
        return updatedTask;
      }
      return task;
    });

    // Recalculate progress
    const totalAllocated = updatedTasks?.reduce((sum, t) => sum + t.allocatedHours, 0) || 0;
    const totalWorked = updatedTasks?.reduce((sum, t) => sum + t.workedHours, 0) || 0;
    const progress = totalAllocated > 0 ? Math.min(Math.round((totalWorked / totalAllocated) * 100), 100) : 0;

    onUpdateGoal({
      ...goal,
      tasks: updatedTasks,
      progress,
    });

    setEditingTask(null);
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
        <h1 className="text-white mb-2">My Tasks</h1>
        <p className="text-white/90">All your assigned tasks in one place</p>
      </div>

      {/* Tasks List */}
      <div className="flex-1 px-6 py-6 space-y-4 overflow-y-auto">
        {allTasks.length === 0 ? (
          <div className="text-center py-12">
            <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-slate-100 flex items-center justify-center">
              <Clock className="size-10 text-slate-400" />
            </div>
            <h3 className="text-slate-600 mb-2">No tasks yet</h3>
            <p className="text-slate-500 text-sm">Create goals and add tasks to get started</p>
          </div>
        ) : (
          allTasks.map(({ task, goalId, goalTitle }) => {
            const taskProgress = task.allocatedHours > 0 
              ? Math.min(Math.round((task.workedHours / task.allocatedHours) * 100), 100)
              : 0;

            return (
              <div
                key={`${goalId}-${task.id}`}
                className="bg-white border-2 border-slate-100 rounded-2xl p-4 shadow-sm"
              >
                {/* Goal Tag */}
                <div className="mb-3">
                  <span className="inline-block px-3 py-1 bg-slate-100 text-slate-600 text-xs rounded-full">
                    {goalTitle}
                  </span>
                </div>

                <div className="flex items-start justify-between mb-3">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <h3 className="text-[#2C3E50]">{task.title}</h3>
                      {task.isShared ? (
                        <div className="flex items-center gap-1 text-blue-600 text-xs">
                          <Users className="size-4" />
                          <span>Shared</span>
                        </div>
                      ) : (
                        <div className="flex items-center gap-1 text-slate-400 text-xs">
                          <User className="size-4" />
                          <span>Personal</span>
                        </div>
                      )}
                    </div>
                    <p className="text-slate-500 text-sm mb-2">{task.description}</p>
                    {task.sharedWith && task.sharedWith.length > 0 && (
                      <p className="text-blue-600 text-sm">
                        Shared with: {task.sharedWith.join(", ")}
                      </p>
                    )}
                  </div>
                  <Button
                    onClick={() => setEditingTask({ task, goalId })}
                    variant="ghost"
                    size="icon"
                    className="flex-shrink-0 text-slate-400 hover:text-[#FF6B4A] hover:bg-orange-50"
                  >
                    <Edit2 className="size-5" />
                  </Button>
                </div>

                {/* Progress Bar */}
                <div className="mb-3">
                  <div className="flex items-center justify-between text-sm mb-2">
                    <span className="text-slate-600">Progress</span>
                    <span className="text-[#FF6B4A]">{taskProgress}%</span>
                  </div>
                  <div className="h-2.5 bg-slate-100 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-gradient-to-r from-[#FF6B4A] to-[#f97316] transition-all duration-700 ease-out rounded-full"
                      style={{ width: `${taskProgress}%` }}
                    />
                  </div>
                </div>

                {/* Time Info and Log Button */}
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-1 text-slate-600 text-sm">
                    <Clock className="size-4" />
                    <span>{task.workedHours}h / {task.allocatedHours}h</span>
                  </div>
                  <Button
                    onClick={() => setLoggingTask({ task, goalId })}
                    size="sm"
                    className="h-8 rounded-lg bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
                  >
                    Log Hours
                  </Button>
                </div>
              </div>
            );
          })
        )}
      </div>

      {/* Log Hours Dialog */}
      {loggingTask && (
        <LogHoursDialog
          task={loggingTask.task}
          onLog={handleLogHours}
          onClose={() => setLoggingTask(null)}
        />
      )}

      {/* Edit Task Dialog */}
      {editingTask && (
        <EditTaskDialog
          task={editingTask.task}
          onSave={handleSaveTask}
          onClose={() => setEditingTask(null)}
          friends={friends}
        />
      )}
    </div>
  );
}
