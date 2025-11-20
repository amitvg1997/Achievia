import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { X } from "lucide-react";
import type { Friend } from "./friends-screen";

interface AddFriendDialogProps {
  onAdd: (friend: Omit<Friend, "id" | "sharedGoals">) => void;
  onClose: () => void;
}

export function AddFriendDialog({ onAdd, onClose }: AddFriendDialogProps) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");

  const handleAdd = () => {
    if (name.trim() && email.trim()) {
      const initials = name
        .split(" ")
        .map((n) => n[0])
        .join("")
        .toUpperCase()
        .slice(0, 2);

      onAdd({
        name,
        email,
        initials,
      });
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-end sm:items-center justify-center z-50 animate-in fade-in duration-200">
      <div className="bg-white w-full sm:max-w-md sm:rounded-3xl rounded-t-3xl overflow-hidden animate-in slide-in-from-bottom duration-300 sm:animate-in sm:zoom-in-95">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-100">
          <h2 className="text-[#2C3E50]">Add Friend</h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600"
          >
            <X className="size-6" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-5">
          {/* Name */}
          <div className="space-y-2">
            <Label htmlFor="friend-name" className="text-[#2C3E50]">Name</Label>
            <Input
              id="friend-name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter friend's name"
              className="h-12 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
            />
          </div>

          {/* Email */}
          <div className="space-y-2">
            <Label htmlFor="friend-email" className="text-[#2C3E50]">Email</Label>
            <Input
              id="friend-email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter friend's email"
              className="h-12 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
            />
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-slate-100 space-y-3">
          <Button
            onClick={handleAdd}
            disabled={!name.trim() || !email.trim()}
            className="w-full h-12 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white disabled:opacity-50"
          >
            Add Friend
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
