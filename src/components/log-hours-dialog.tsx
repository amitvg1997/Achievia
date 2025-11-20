import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { X, Clock } from "lucide-react";
import type { Task } from "./edit-goal-dialog";

interface LogHoursDialogProps {
  task: Task;
  onLog: (hours: number) => void;
  onClose: () => void;
}

export function LogHoursDialog({ task, onLog, onClose }: LogHoursDialogProps) {
  const [hours, setHours] = useState("");

  const handleLog = () => {
    const hoursNum = parseFloat(hours);
    if (hoursNum > 0) {
      onLog(hoursNum);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-end sm:items-center justify-center z-50 animate-in fade-in duration-200">
      <div className="bg-white w-full sm:max-w-md sm:rounded-3xl rounded-t-3xl overflow-hidden animate-in slide-in-from-bottom duration-300 sm:animate-in sm:zoom-in-95">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-100">
          <h2 className="text-[#2C3E50]">Log Work Hours</h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600"
          >
            <X className="size-6" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-5">
          <div className="bg-slate-50 rounded-xl p-4">
            <h3 className="text-[#2C3E50] mb-2">{task.title}</h3>
            <div className="flex items-center gap-2 text-slate-600 text-sm">
              <Clock className="size-4" />
              <span>
                {task.workedHours}h / {task.allocatedHours}h completed
              </span>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="hours" className="text-[#2C3E50]">
              Hours Worked
            </Label>
            <Input
              id="hours"
              type="number"
              step="0.5"
              min="0"
              value={hours}
              onChange={(e) => setHours(e.target.value)}
              placeholder="Enter hours (e.g., 2.5)"
              className="h-12 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
            />
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-slate-100 space-y-3">
          <Button
            onClick={handleLog}
            disabled={!hours || parseFloat(hours) <= 0}
            className="w-full h-12 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white disabled:opacity-50"
          >
            Log Hours
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
