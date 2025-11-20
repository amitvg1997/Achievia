import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Textarea } from "./ui/textarea";
import { Badge } from "./ui/badge";
import { Checkbox } from "./ui/checkbox";
import { X } from "lucide-react";
import type { Task } from "./edit-goal-dialog";

interface EditTaskDialogProps {
  task: Task;
  onSave: (task: Task) => void;
  onClose: () => void;
  friends: Array<{ id: string; name: string }>;
}

export function EditTaskDialog({ task, onSave, onClose, friends }: EditTaskDialogProps) {
  const [title, setTitle] = useState(task.title);
  const [description, setDescription] = useState(task.description);
  const [allocatedHours, setAllocatedHours] = useState(task.allocatedHours.toString());
  const [isShared, setIsShared] = useState(task.isShared);
  const [selectedFriends, setSelectedFriends] = useState<string[]>(task.sharedWith || []);

  const toggleFriend = (friendName: string) => {
    setSelectedFriends((prev) =>
      prev.includes(friendName)
        ? prev.filter((f) => f !== friendName)
        : [...prev, friendName]
    );
  };

  const handleSave = () => {
    onSave({
      ...task,
      title,
      description,
      allocatedHours: parseFloat(allocatedHours),
      isShared,
      sharedWith: isShared ? selectedFriends : undefined,
    });
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-end sm:items-center justify-center z-50 animate-in fade-in duration-200">
      <div className="bg-white w-full sm:max-w-md sm:rounded-3xl rounded-t-3xl overflow-hidden animate-in slide-in-from-bottom duration-300 sm:animate-in sm:zoom-in-95">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-100">
          <h2 className="text-[#2C3E50]">Edit Task</h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600"
          >
            <X className="size-6" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-5">
          <div className="space-y-2">
            <Label htmlFor="task-title" className="text-[#2C3E50]">Task Title</Label>
            <Input
              id="task-title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter task title"
              className="h-12 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="task-description" className="text-[#2C3E50]">Description</Label>
            <Textarea
              id="task-description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Task description"
              rows={3}
              className="rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A] resize-none"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="allocated-hours" className="text-[#2C3E50]">
              Allocated Hours
            </Label>
            <Input
              id="allocated-hours"
              type="number"
              step="0.5"
              min="0"
              value={allocatedHours}
              onChange={(e) => setAllocatedHours(e.target.value)}
              placeholder="Hours allocated"
              className="h-12 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
            />
          </div>

          {/* Share Task Option */}
          <div className="space-y-3">
            <div className="flex items-center space-x-2">
              <Checkbox
                id="share-task-edit"
                checked={isShared}
                onCheckedChange={(checked) => {
                  setIsShared(checked as boolean);
                  if (!checked) setSelectedFriends([]);
                }}
              />
              <Label htmlFor="share-task-edit" className="cursor-pointer">
                Share this task with friends
              </Label>
            </div>

            {isShared && friends.length > 0 && (
              <div className="bg-slate-50 rounded-xl p-4 space-y-2">
                <Label className="text-sm text-[#2C3E50]">Select Friends</Label>
                <div className="flex flex-wrap gap-2">
                  {friends.map((friend) => (
                    <Badge
                      key={friend.id}
                      variant={selectedFriends.includes(friend.name) ? "default" : "outline"}
                      className={`cursor-pointer ${
                        selectedFriends.includes(friend.name)
                          ? "bg-[#FF6B4A] hover:bg-[#FF5A39]"
                          : "border-slate-300 hover:border-[#FF6B4A]"
                      }`}
                      onClick={() => toggleFriend(friend.name)}
                    >
                      {friend.name}
                    </Badge>
                  ))}
                </div>
              </div>
            )}

            {isShared && friends.length === 0 && (
              <p className="text-sm text-slate-500">Add friends to share tasks</p>
            )}
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-slate-100 space-y-3">
          <Button
            onClick={handleSave}
            disabled={!title.trim() || !allocatedHours}
            className="w-full h-12 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white disabled:opacity-50"
          >
            Save Changes
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
