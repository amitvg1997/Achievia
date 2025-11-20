import { useState } from "react";
import { Button } from "./ui/button";
import { Menu, UserPlus, Trash2 } from "lucide-react";
import { AddFriendDialog } from "./add-friend-dialog";
import { Avatar, AvatarFallback } from "./ui/avatar";

export interface Friend {
  id: string;
  name: string;
  email: string;
  initials: string;
  sharedGoals: number;
}

interface FriendsScreenProps {
  onMenuClick: () => void;
}

export function FriendsScreen({ onMenuClick }: FriendsScreenProps) {
  const [friends, setFriends] = useState<Friend[]>([
    {
      id: "1",
      name: "Sarah Johnson",
      email: "sarah@example.com",
      initials: "SJ",
      sharedGoals: 3,
    },
    {
      id: "2",
      name: "Mike Chen",
      email: "mike@example.com",
      initials: "MC",
      sharedGoals: 1,
    },
    {
      id: "3",
      name: "Emily Davis",
      email: "emily@example.com",
      initials: "ED",
      sharedGoals: 2,
    },
  ]);

  const [showAddDialog, setShowAddDialog] = useState(false);

  const handleAddFriend = (friend: Omit<Friend, "id" | "sharedGoals">) => {
    const newFriend: Friend = {
      ...friend,
      id: Date.now().toString(),
      sharedGoals: 0,
    };
    setFriends((prev) => [...prev, newFriend]);
    setShowAddDialog(false);
  };

  const handleRemoveFriend = (id: string) => {
    setFriends((prev) => prev.filter((f) => f.id !== id));
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
        <h1 className="text-white mb-2">Friends</h1>
        <p className="text-white/90">Collaborate and achieve together</p>
      </div>

      {/* Friends List */}
      <div className="flex-1 px-6 py-6 space-y-3 overflow-y-auto">
        {friends.length === 0 ? (
          <div className="text-center py-12">
            <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-slate-100 flex items-center justify-center">
              <UserPlus className="size-10 text-slate-400" />
            </div>
            <h3 className="text-slate-600 mb-2">No friends yet</h3>
            <p className="text-slate-500 text-sm">Add friends to share goals and track progress together</p>
          </div>
        ) : (
          friends.map((friend) => (
            <div
              key={friend.id}
              className="bg-white border-2 border-slate-100 rounded-2xl p-4 shadow-sm hover:shadow-md transition-shadow"
            >
              <div className="flex items-center gap-4">
                {/* Avatar */}
                <Avatar className="w-14 h-14 bg-gradient-to-br from-[#FF6B4A] to-[#FF8A6F]">
                  <AvatarFallback className="bg-transparent text-white">
                    {friend.initials}
                  </AvatarFallback>
                </Avatar>

                {/* Friend Info */}
                <div className="flex-1 min-w-0">
                  <h3 className="text-[#2C3E50] mb-1">{friend.name}</h3>
                  <p className="text-slate-500 text-sm truncate">{friend.email}</p>
                  <p className="text-[#FF6B4A] text-sm mt-1">
                    {friend.sharedGoals} shared {friend.sharedGoals === 1 ? "goal" : "goals"}
                  </p>
                </div>

                {/* Remove Button */}
                <Button
                  onClick={() => handleRemoveFriend(friend.id)}
                  variant="ghost"
                  size="icon"
                  className="flex-shrink-0 text-slate-400 hover:text-destructive hover:bg-red-50"
                >
                  <Trash2 className="size-5" />
                </Button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Add Friend Button */}
      <div className="p-6">
        <Button
          onClick={() => setShowAddDialog(true)}
          className="w-full h-14 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
        >
          <UserPlus className="size-5 mr-2" />
          Add Friend
        </Button>
      </div>

      {/* Add Friend Dialog */}
      {showAddDialog && (
        <AddFriendDialog
          onAdd={handleAddFriend}
          onClose={() => setShowAddDialog(false)}
        />
      )}
    </div>
  );
}
