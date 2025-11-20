import { Sheet, SheetContent, SheetHeader, SheetTitle } from "./ui/sheet";
import { Button } from "./ui/button";
import { Users, Target, Settings, LogOut, Bell, CheckSquare } from "lucide-react";

interface SideMenuProps {
  isOpen: boolean;
  onClose: () => void;
  onNavigate: (screen: "goals" | "friends" | "notifications" | "settings" | "myTasks") => void;
}

export function SideMenu({ isOpen, onClose, onNavigate }: SideMenuProps) {
  const handleNavigation = (screen: "goals" | "friends" | "notifications" | "settings" | "myTasks") => {
    onNavigate(screen);
    onClose();
  };

  return (
    <Sheet open={isOpen} onOpenChange={onClose}>
      <SheetContent side="left" className="w-[280px] sm:w-[320px] p-0">
        <div className="flex flex-col h-full">
          {/* Header */}
          <SheetHeader className="bg-gradient-to-r from-[#FF6B4A] to-[#FF8A6F] px-6 py-8">
            <div className="flex items-center gap-3">
              <div className="w-16 h-16 rounded-full bg-white/20 flex items-center justify-center">
                <span className="text-white text-xl">JD</span>
              </div>
              <div>
                <SheetTitle className="text-white text-left">John Doe</SheetTitle>
                <p className="text-white/90 text-sm">john@example.com</p>
              </div>
            </div>
          </SheetHeader>

          {/* Menu Items */}
          <div className="flex-1 py-4">
            <Button
              variant="ghost"
              className="w-full justify-start px-6 py-6 text-[#2C3E50] hover:bg-orange-50 hover:text-[#FF6B4A]"
              onClick={() => handleNavigation("goals")}
            >
              <Target className="size-5 mr-3" />
              My Goals
            </Button>

            <Button
              variant="ghost"
              className="w-full justify-start px-6 py-6 text-[#2C3E50] hover:bg-orange-50 hover:text-[#FF6B4A]"
              onClick={() => handleNavigation("myTasks")}
            >
              <CheckSquare className="size-5 mr-3" />
              My Tasks
            </Button>

            <Button
              variant="ghost"
              className="w-full justify-start px-6 py-6 text-[#2C3E50] hover:bg-orange-50 hover:text-[#FF6B4A]"
              onClick={() => handleNavigation("friends")}
            >
              <Users className="size-5 mr-3" />
              Friends
            </Button>

            <Button
              variant="ghost"
              className="w-full justify-start px-6 py-6 text-[#2C3E50] hover:bg-orange-50 hover:text-[#FF6B4A]"
              onClick={() => handleNavigation("notifications")}
            >
              <Bell className="size-5 mr-3" />
              Notifications
            </Button>

            <Button
              variant="ghost"
              className="w-full justify-start px-6 py-6 text-[#2C3E50] hover:bg-orange-50 hover:text-[#FF6B4A]"
              onClick={() => handleNavigation("settings")}
            >
              <Settings className="size-5 mr-3" />
              Settings
            </Button>
          </div>

          {/* Logout */}
          <div className="p-4 border-t border-slate-100">
            <Button
              variant="ghost"
              className="w-full justify-start px-6 py-3 text-destructive hover:bg-red-50"
            >
              <LogOut className="size-5 mr-3" />
              Logout
            </Button>
          </div>
        </div>
      </SheetContent>
    </Sheet>
  );
}