import { useState } from "react";
import { Button } from "./ui/button";
import { ChevronLeft, Clock, Users, User, Plus } from "lucide-react";
import type { Goal } from "./goals-screen";
import type { Task } from "./edit-goal-dialog";
import { LogHoursDialog } from "./log-hours-dialog";
import { CircularProgress } from "./circular-progress";

interface TaskListScreenProps {
  goal: Goal;
  onBack: () => void;
  onUpdateGoal: (goal: Goal) => void;
}

export function TaskListScreen({ goal, onBack, onUpdateGoal }: TaskListScreenProps) {
  const [loggingTask, setLoggingTask] = useState<Task | null>(null);

  const handleLogHours = (taskId: string, hours: number) => {
    const updatedTasks = goal.tasks?.map((task) => {
      if (task.id === taskId) {
        const newWorkedHours = task.workedHours + hours;
        return {
          ...task,
          workedHours: newWorkedHours,
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

  const tasks = goal.tasks || [];
  const totalAllocated = tasks.reduce((sum, t) => sum + t.allocatedHours, 0);
  const totalWorked = tasks.reduce((sum, t) => sum + t.workedHours, 0);

  return (
    <div className="min-h-screen bg-white flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-r from-[#FF6B4A] to-[#FF8A6F] px-6 pt-12 pb-6">
        <button onClick={onBack} className="text-white mb-6">
          <ChevronLeft className="size-6" />
        </button>
        <div className="flex items-start gap-4">
          <div className="flex-1">
            <h1 className="text-white mb-2">{goal.title}</h1>
            <p className="text-white/90 text-sm mb-4">{goal.description}</p>
            <div className="flex items-center gap-4 text-white/90 text-sm">
              <div className="flex items-center gap-1">
                <Clock className="size-4" />
                <span>{totalWorked}h / {totalAllocated}h</span>
              </div>
            </div>
          </div>
          <CircularProgress progress={goal.progress} size={60} />
        </div>
      </div>

      {/* Tasks List */}
      <div className="flex-1 px-6 py-6 space-y-3 overflow-y-auto">
        {tasks.length === 0 ? (
          <div className="text-center py-12">
            <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-slate-100 flex items-center justify-center">
              <Plus className="size-10 text-slate-400" />
            </div>
            <h3 className="text-slate-600 mb-2">No tasks yet</h3>
            <p className="text-slate-500 text-sm">Edit this goal to add tasks</p>
          </div>
        ) : (
          tasks.map((task) => {
            const taskProgress = task.allocatedHours > 0 
              ? Math.min(Math.round((task.workedHours / task.allocatedHours) * 100), 100)
              : 0;

            return (
              <div
                key={task.id}
                className="bg-white border-2 border-slate-100 rounded-2xl p-4 shadow-sm"
              >
                <div className="flex items-start justify-between mb-3">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <h3 className="text-[#2C3E50]">{task.title}</h3>
                      {task.isShared ? (
                        <div className="flex items-center gap-1 text-[#FF6B4A] text-xs">
                          <Users className="size-3" />
                          <span>Shared</span>
                        </div>
                      ) : (
                        <div className="flex items-center gap-1 text-slate-400 text-xs">
                          <User className="size-3" />
                          <span>Personal</span>
                        </div>
                      )}
                    </div>
                    <p className="text-slate-500 text-sm mb-2">{task.description}</p>
                    {task.sharedWith && (
                      <p className="text-blue-600 text-sm">
                        Shared with: {task.sharedWith.join(", ")}
                      </p>
                    )}
                  </div>
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
                    onClick={() => setLoggingTask(task)}
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
          task={loggingTask}
          onLog={(hours) => handleLogHours(loggingTask.id, hours)}
          onClose={() => setLoggingTask(null)}
        />
      )}
    </div>
  );
}