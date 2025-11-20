import { CircularProgress } from "./circular-progress";
import { Button } from "./ui/button";
import { Edit2, Users, User } from "lucide-react";
import type { Goal } from "./goals-screen";

interface GoalItemProps {
  goal: Goal;
  onEdit: (goal: Goal) => void;
  onClick: () => void;
}

export function GoalItem({ goal, onEdit, onClick }: GoalItemProps) {
  return (
    <div
      onClick={onClick}
      className="bg-white border-2 border-slate-100 rounded-2xl p-4 shadow-sm hover:shadow-md transition-shadow cursor-pointer"
    >
      <div className="flex items-center gap-4">
        {/* Circular Progress */}
        <div className="flex-shrink-0">
          <CircularProgress progress={goal.progress} size={70} />
        </div>

        {/* Goal Content */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 mb-1">
            <h3 className="text-[#2C3E50] truncate">{goal.title}</h3>
            {goal.isShared ? (
              <div className="flex items-center gap-1 text-blue-600 text-xs">
                <Users className="size-4" />
              </div>
            ) : (
              <div className="flex items-center gap-1 text-slate-400 text-xs">
                <User className="size-4" />
              </div>
            )}
          </div>
          <p className="text-slate-500 text-sm line-clamp-2">{goal.description}</p>
          {goal.isShared && goal.sharedWith && goal.sharedWith.length > 0 && (
            <p className="text-blue-600 text-xs mt-1">
              Shared with {goal.sharedWith.join(", ")}
            </p>
          )}
        </div>

        {/* Edit Button */}
        <Button
          onClick={(e) => {
            e.stopPropagation();
            onEdit(goal);
          }}
          variant="ghost"
          size="icon"
          className="flex-shrink-0 text-slate-400 hover:text-[#FF6B4A] hover:bg-orange-50"
        >
          <Edit2 className="size-5" />
        </Button>
      </div>
    </div>
  );
}