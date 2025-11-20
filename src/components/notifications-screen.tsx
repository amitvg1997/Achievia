import { useState } from "react";
import { Button } from "./ui/button";
import { Menu, Check, X, Users, Target } from "lucide-react";
import { Avatar, AvatarFallback } from "./ui/avatar";

export interface Notification {
  id: string;
  type: "friend_request" | "shared_goal" | "shared_task";
  from: string;
  fromInitials: string;
  message: string;
  timestamp: string;
  goalOrTaskName?: string;
}

interface NotificationsScreenProps {
  onMenuClick: () => void;
}

export function NotificationsScreen({ onMenuClick }: NotificationsScreenProps) {
  const [notifications, setNotifications] = useState<Notification[]>([
    {
      id: "1",
      type: "friend_request",
      from: "Alex Martinez",
      fromInitials: "AM",
      message: "sent you a friend request",
      timestamp: "2 hours ago",
    },
    {
      id: "2",
      type: "shared_goal",
      from: "Sarah Johnson",
      fromInitials: "SJ",
      message: "added you to a shared goal",
      goalOrTaskName: "Complete React Course",
      timestamp: "5 hours ago",
    },
    {
      id: "3",
      type: "shared_task",
      from: "Mike Chen",
      fromInitials: "MC",
      message: "shared a task with you",
      goalOrTaskName: "Build Project Dashboard",
      timestamp: "1 day ago",
    },
  ]);

  const handleAccept = (id: string) => {
    console.log("Accepted notification:", id);
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };

  const handleReject = (id: string) => {
    console.log("Rejected notification:", id);
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };

  const getIcon = (type: Notification["type"]) => {
    switch (type) {
      case "friend_request":
        return <Users className="size-5" />;
      case "shared_goal":
      case "shared_task":
        return <Target className="size-5" />;
    }
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
        <h1 className="text-white mb-2">Notifications</h1>
        <p className="text-white/90">Stay updated with your activities</p>
      </div>

      {/* Notifications List */}
      <div className="flex-1 px-6 py-6 space-y-3 overflow-y-auto">
        {notifications.length === 0 ? (
          <div className="text-center py-12">
            <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-slate-100 flex items-center justify-center">
              <Target className="size-10 text-slate-400" />
            </div>
            <h3 className="text-slate-600 mb-2">No notifications</h3>
            <p className="text-slate-500 text-sm">You're all caught up!</p>
          </div>
        ) : (
          notifications.map((notification) => (
            <div
              key={notification.id}
              className="bg-white border-2 border-slate-100 rounded-2xl p-4 shadow-sm"
            >
              <div className="flex items-start gap-3">
                {/* Avatar */}
                <Avatar className="w-12 h-12 bg-gradient-to-br from-[#FF6B4A] to-[#FF8A6F] flex-shrink-0">
                  <AvatarFallback className="bg-transparent text-white">
                    {notification.fromInitials}
                  </AvatarFallback>
                </Avatar>

                {/* Notification Content */}
                <div className="flex-1 min-w-0">
                  <div className="flex items-start gap-2 mb-2">
                    <div className="mt-1 text-[#FF6B4A]">
                      {getIcon(notification.type)}
                    </div>
                    <div className="flex-1">
                      <p className="text-[#2C3E50]">
                        <span className="font-medium">{notification.from}</span>{" "}
                        {notification.message}
                      </p>
                      {notification.goalOrTaskName && (
                        <p className="text-[#FF6B4A] text-sm mt-1">
                          "{notification.goalOrTaskName}"
                        </p>
                      )}
                      <p className="text-slate-400 text-sm mt-1">
                        {notification.timestamp}
                      </p>
                    </div>
                  </div>

                  {/* Action Buttons */}
                  {notification.type === "friend_request" && (
                    <div className="flex gap-2 mt-3">
                      <Button
                        onClick={() => handleAccept(notification.id)}
                        className="flex-1 h-9 rounded-lg bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
                      >
                        <Check className="size-4 mr-1" />
                        Accept
                      </Button>
                      <Button
                        onClick={() => handleReject(notification.id)}
                        variant="outline"
                        className="flex-1 h-9 rounded-lg border-slate-200 text-slate-600"
                      >
                        <X className="size-4 mr-1" />
                        Decline
                      </Button>
                    </div>
                  )}
                  {(notification.type === "shared_goal" ||
                    notification.type === "shared_task") && (
                    <div className="flex gap-2 mt-3">
                      <Button
                        onClick={() => handleAccept(notification.id)}
                        className="flex-1 h-9 rounded-lg bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
                      >
                        View
                      </Button>
                      <Button
                        onClick={() => handleReject(notification.id)}
                        variant="outline"
                        className="h-9 rounded-lg border-slate-200 text-slate-600 px-3"
                      >
                        <X className="size-4" />
                      </Button>
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
