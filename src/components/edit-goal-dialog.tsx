import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Textarea } from "./ui/textarea";
import { X, Plus, Trash2, Users } from "lucide-react";
import type { Goal } from "./goals-screen";
import { Checkbox } from "./ui/checkbox";
import { Badge } from "./ui/badge";

export interface Task {
  id: string;
  title: string;
  description: string;
  allocatedHours: number;
  workedHours: number;
  isShared: boolean;
  sharedWith?: string[];
}

interface EditGoalDialogProps {
  goal: Goal;
  isNew: boolean;
  onSave: (goal: Goal) => void;
  onClose: () => void;
  friends: Array<{ id: string; name: string }>;
}

export function EditGoalDialog({ goal, isNew, onSave, onClose, friends }: EditGoalDialogProps) {
  const [title, setTitle] = useState(goal.title);
  const [description, setDescription] = useState(goal.description);
  const [tasks, setTasks] = useState<Task[]>(goal.tasks || []);
  const [showTaskForm, setShowTaskForm] = useState(false);
  const [isGoalShared, setIsGoalShared] = useState(goal.isShared || false);
  const [selectedGoalFriends, setSelectedGoalFriends] = useState<string[]>(goal.sharedWith || []);

  // Task form state
  const [taskTitle, setTaskTitle] = useState("");
  const [taskDescription, setTaskDescription] = useState("");
  const [taskHours, setTaskHours] = useState("");
  const [isTaskShared, setIsTaskShared] = useState(false);
  const [selectedTaskFriends, setSelectedTaskFriends] = useState<string[]>([]);
  const [editingTaskId, setEditingTaskId] = useState<string | null>(null);

  const toggleGoalFriend = (friendName: string) => {
    setSelectedGoalFriends((prev) =>
      prev.includes(friendName)
        ? prev.filter((f) => f !== friendName)
        : [...prev, friendName]
    );
  };

  const toggleTaskFriend = (friendName: string) => {
    setSelectedTaskFriends((prev) =>
      prev.includes(friendName)
        ? prev.filter((f) => f !== friendName)
        : [...prev, friendName]
    );
  };

  const handleAddTask = () => {
    if (taskTitle.trim() && taskHours) {
      const newTask: Task = {
        id: Date.now().toString(),
        title: taskTitle,
        description: taskDescription,
        allocatedHours: parseFloat(taskHours),
        workedHours: 0,
        isShared: isTaskShared,
        sharedWith: isTaskShared ? selectedTaskFriends : undefined,
      };
      setTasks([...tasks, newTask]);
      resetTaskForm();
    }
  };

  const handleUpdateTask = () => {
    if (editingTaskId && taskTitle.trim() && taskHours) {
      setTasks(
        tasks.map((task) =>
          task.id === editingTaskId
            ? {
                ...task,
                title: taskTitle,
                description: taskDescription,
                allocatedHours: parseFloat(taskHours),
                isShared: isTaskShared,
                sharedWith: isTaskShared ? selectedTaskFriends : undefined,
              }
            : task
        )
      );
      resetTaskForm();
    }
  };

  const handleEditTask = (task: Task) => {
    setTaskTitle(task.title);
    setTaskDescription(task.description);
    setTaskHours(task.allocatedHours.toString());
    setIsTaskShared(task.isShared);
    setSelectedTaskFriends(task.sharedWith || []);
    setEditingTaskId(task.id);
    setShowTaskForm(true);
  };

  const handleDeleteTask = (taskId: string) => {
    setTasks(tasks.filter((t) => t.id !== taskId));
  };

  const resetTaskForm = () => {
    setTaskTitle("");
    setTaskDescription("");
    setTaskHours("");
    setIsTaskShared(false);
    setSelectedTaskFriends([]);
    setEditingTaskId(null);
    setShowTaskForm(false);
  };

  const handleSave = () => {
    // Calculate progress based on tasks
    const totalAllocated = tasks.reduce((sum, t) => sum + t.allocatedHours, 0);
    const totalWorked = tasks.reduce((sum, t) => sum + t.workedHours, 0);
    const progress = totalAllocated > 0 ? Math.min(Math.round((totalWorked / totalAllocated) * 100), 100) : 0;

    onSave({
      ...goal,
      title,
      description,
      tasks,
      progress,
      isShared: isGoalShared,
      sharedWith: isGoalShared ? selectedGoalFriends : undefined,
    });
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-end sm:items-center justify-center z-50 animate-in fade-in duration-200">
      <div className="bg-white w-full sm:max-w-md sm:rounded-3xl rounded-t-3xl max-h-[90vh] overflow-y-auto animate-in slide-in-from-bottom duration-300 sm:animate-in sm:zoom-in-95">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-100 sticky top-0 bg-white z-10">
          <h2 className="text-[#2C3E50]">{isNew ? "New Goal" : "Edit Goal"}</h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600"
          >
            <X className="size-6" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Title */}
          <div className="space-y-2">
            <Label htmlFor="title" className="text-[#2C3E50]">Goal Title</Label>
            <Input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter goal title"
              className="h-12 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
            />
          </div>

          {/* Description */}
          <div className="space-y-2">
            <Label htmlFor="description" className="text-[#2C3E50]">Description</Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Describe your goal"
              rows={3}
              className="rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A] resize-none"
            />
          </div>

          {/* Share Goal Option */}
          <div className="space-y-3">
            <div className="flex items-center space-x-2">
              <Checkbox
                id="share-goal"
                checked={isGoalShared}
                onCheckedChange={(checked) => {
                  setIsGoalShared(checked as boolean);
                  if (!checked) setSelectedGoalFriends([]);
                }}
              />
              <Label htmlFor="share-goal" className="cursor-pointer">
                Share this goal with friends
              </Label>
            </div>

            {isGoalShared && friends.length > 0 && (
              <div className="bg-slate-50 rounded-xl p-4 space-y-2">
                <Label className="text-sm text-[#2C3E50]">Select Friends</Label>
                <div className="flex flex-wrap gap-2">
                  {friends.map((friend) => (
                    <Badge
                      key={friend.id}
                      variant={selectedGoalFriends.includes(friend.name) ? "default" : "outline"}
                      className={`cursor-pointer ${
                        selectedGoalFriends.includes(friend.name)
                          ? "bg-[#FF6B4A] hover:bg-[#FF5A39]"
                          : "border-slate-300 hover:border-[#FF6B4A]"
                      }`}
                      onClick={() => toggleGoalFriend(friend.name)}
                    >
                      {friend.name}
                    </Badge>
                  ))}
                </div>
              </div>
            )}

            {isGoalShared && friends.length === 0 && (
              <p className="text-sm text-slate-500">Add friends to share goals</p>
            )}
          </div>

          {/* Tasks Section */}
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <Label className="text-[#2C3E50]">Tasks</Label>
              {!showTaskForm && (
                <Button
                  onClick={() => setShowTaskForm(true)}
                  size="sm"
                  className="h-8 rounded-lg bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
                >
                  <Plus className="size-4 mr-1" />
                  Add Task
                </Button>
              )}
            </div>

            {/* Task Form */}
            {showTaskForm && (
              <div className="bg-slate-50 rounded-xl p-4 space-y-4">
                <Input
                  value={taskTitle}
                  onChange={(e) => setTaskTitle(e.target.value)}
                  placeholder="Task title"
                  className="h-10 rounded-lg border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
                />
                <Textarea
                  value={taskDescription}
                  onChange={(e) => setTaskDescription(e.target.value)}
                  placeholder="Task description"
                  rows={2}
                  className="rounded-lg border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A] resize-none"
                />
                <Input
                  type="number"
                  step="0.5"
                  min="0"
                  value={taskHours}
                  onChange={(e) => setTaskHours(e.target.value)}
                  placeholder="Allocated hours (e.g., 10)"
                  className="h-10 rounded-lg border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
                />

                {/* Share Task Option */}
                <div className="space-y-3">
                  <div className="flex items-center space-x-2">
                    <Checkbox
                      id="share-task"
                      checked={isTaskShared}
                      onCheckedChange={(checked) => {
                        setIsTaskShared(checked as boolean);
                        if (!checked) setSelectedTaskFriends([]);
                      }}
                    />
                    <Label htmlFor="share-task" className="text-sm cursor-pointer">
                      Share this task with friends
                    </Label>
                  </div>

                  {isTaskShared && friends.length > 0 && (
                    <div className="space-y-2">
                      <Label className="text-sm text-[#2C3E50]">Select Friends</Label>
                      <div className="flex flex-wrap gap-2">
                        {friends.map((friend) => (
                          <Badge
                            key={friend.id}
                            variant={selectedTaskFriends.includes(friend.name) ? "default" : "outline"}
                            className={`cursor-pointer ${
                              selectedTaskFriends.includes(friend.name)
                                ? "bg-[#FF6B4A] hover:bg-[#FF5A39]"
                                : "border-slate-300 hover:border-[#FF6B4A]"
                            }`}
                            onClick={() => toggleTaskFriend(friend.name)}
                          >
                            {friend.name}
                          </Badge>
                        ))}
                      </div>
                    </div>
                  )}

                  {isTaskShared && friends.length === 0 && (
                    <p className="text-sm text-slate-500">Add friends to share tasks</p>
                  )}
                </div>

                <div className="flex gap-2">
                  <Button
                    onClick={editingTaskId ? handleUpdateTask : handleAddTask}
                    size="sm"
                    className="flex-1 h-9 rounded-lg bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
                  >
                    {editingTaskId ? "Update" : "Add"}
                  </Button>
                  <Button
                    onClick={resetTaskForm}
                    size="sm"
                    variant="outline"
                    className="flex-1 h-9 rounded-lg border-slate-200"
                  >
                    Cancel
                  </Button>
                </div>
              </div>
            )}

            {/* Tasks List */}
            <div className="space-y-2">
              {tasks.map((task) => (
                <div
                  key={task.id}
                  className="bg-white border-2 border-slate-100 rounded-xl p-3 flex items-start gap-3"
                >
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <h4 className="text-[#2C3E50] text-sm">{task.title}</h4>
                      {task.isShared && (
                        <Badge variant="secondary" className="bg-blue-100 text-blue-700 text-xs">
                          <Users className="size-3 mr-1" />
                          Shared
                        </Badge>
                      )}
                    </div>
                    {task.description && (
                      <p className="text-slate-500 text-xs mb-1">{task.description}</p>
                    )}
                    <p className="text-slate-600 text-xs">
                      {task.allocatedHours}h allocated
                      {task.sharedWith && task.sharedWith.length > 0 && (
                        <span className="text-[#FF6B4A]">
                          {" • "}{task.sharedWith.join(", ")}
                        </span>
                      )}
                    </p>
                  </div>
                  <div className="flex gap-1">
                    <Button
                      onClick={() => handleEditTask(task)}
                      size="sm"
                      variant="ghost"
                      className="h-8 w-8 p-0 text-slate-400 hover:text-[#FF6B4A]"
                    >
                      <Plus className="size-4" />
                    </Button>
                    <Button
                      onClick={() => handleDeleteTask(task.id)}
                      size="sm"
                      variant="ghost"
                      className="h-8 w-8 p-0 text-slate-400 hover:text-destructive"
                    >
                      <Trash2 className="size-4" />
                    </Button>
                  </div>
                </div>
              ))}
              {tasks.length === 0 && !showTaskForm && (
                <p className="text-slate-500 text-sm text-center py-4">
                  No tasks yet. Add tasks to track progress.
                </p>
              )}
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-slate-100 space-y-3 sticky bottom-0 bg-white">
          <Button
            onClick={handleSave}
            className="w-full h-12 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
          >
            {isNew ? "Create Goal" : "Save Changes"}
          </Button>
          <Button
            onClick={onClose}
            variant="outline"
            className="w-full h-12 rounded-xl border-slate-200 text-slate-600 hover:bg-slate-50"
          >
            Cancel
          </Button>
        </div>
      </div>
    </div>
  );
}
