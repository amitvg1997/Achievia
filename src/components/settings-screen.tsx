import { useState } from "react";
import { Button } from "./ui/button";
import { Menu, User, Bell, Lock, Trash2, ChevronRight } from "lucide-react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";

interface SettingsScreenProps {
  onMenuClick: () => void;
}

export function SettingsScreen({ onMenuClick }: SettingsScreenProps) {
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

  const handleDeleteProfile = () => {
    console.log("Profile deleted");
    setShowDeleteDialog(false);
    // Add logout logic here
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
        <h1 className="text-white mb-2">Settings</h1>
        <p className="text-white/90">Manage your preferences</p>
      </div>

      {/* Settings Options */}
      <div className="flex-1 px-6 py-6 space-y-6">
        {/* Account Section */}
        <div>
          <h3 className="text-slate-500 text-sm mb-3 px-2">Account</h3>
          <div className="space-y-2">
            <button className="w-full flex items-center justify-between p-4 bg-white border-2 border-slate-100 rounded-xl hover:bg-slate-50 transition-colors">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center">
                  <User className="size-5 text-slate-600" />
                </div>
                <div className="text-left">
                  <p className="text-[#2C3E50]">Profile Settings</p>
                  <p className="text-slate-500 text-sm">Update your information</p>
                </div>
              </div>
              <ChevronRight className="size-5 text-slate-400" />
            </button>

            <button className="w-full flex items-center justify-between p-4 bg-white border-2 border-slate-100 rounded-xl hover:bg-slate-50 transition-colors">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center">
                  <Lock className="size-5 text-slate-600" />
                </div>
                <div className="text-left">
                  <p className="text-[#2C3E50]">Change Password</p>
                  <p className="text-slate-500 text-sm">Update your password</p>
                </div>
              </div>
              <ChevronRight className="size-5 text-slate-400" />
            </button>
          </div>
        </div>

        {/* Notifications Section */}
        <div>
          <h3 className="text-slate-500 text-sm mb-3 px-2">Notifications</h3>
          <div className="space-y-2">
            <button className="w-full flex items-center justify-between p-4 bg-white border-2 border-slate-100 rounded-xl hover:bg-slate-50 transition-colors">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center">
                  <Bell className="size-5 text-slate-600" />
                </div>
                <div className="text-left">
                  <p className="text-[#2C3E50]">Notification Preferences</p>
                  <p className="text-slate-500 text-sm">Manage notifications</p>
                </div>
              </div>
              <ChevronRight className="size-5 text-slate-400" />
            </button>
          </div>
        </div>

        {/* Danger Zone */}
        <div>
          <h3 className="text-slate-500 text-sm mb-3 px-2">Danger Zone</h3>
          <button
            onClick={() => setShowDeleteDialog(true)}
            className="w-full flex items-center justify-between p-4 bg-red-50 border-2 border-red-100 rounded-xl hover:bg-red-100 transition-colors"
          >
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                <Trash2 className="size-5 text-destructive" />
              </div>
              <div className="text-left">
                <p className="text-destructive">Delete Profile</p>
                <p className="text-red-600 text-sm">Permanently delete your account</p>
              </div>
            </div>
            <ChevronRight className="size-5 text-red-400" />
          </button>
        </div>
      </div>

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent className="max-w-[90%] sm:max-w-md rounded-2xl">
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Profile</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete your profile? This action cannot be undone. All your goals, tasks, and data will be permanently removed.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter className="flex-col sm:flex-row gap-2">
            <AlertDialogCancel className="h-11 rounded-xl">Cancel</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDeleteProfile}
              className="h-11 rounded-xl bg-destructive hover:bg-destructive/90"
            >
              Delete Profile
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
