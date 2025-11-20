import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Mail, Lock, Eye, EyeOff } from "lucide-react";
import logo from "figma:asset/988bc6bf887494f584024d499c5091c242257ca4.png";

interface LoginScreenProps {
  onLogin: () => void;
}

export function LoginScreen({ onLogin }: LoginScreenProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Login:", { email, password });
    onLogin();
  };

  const handleForgotPassword = () => {
    console.log("Navigate to forgot password");
  };

  const handleRegister = () => {
    console.log("Navigate to register");
  };

  return (
    <div className="min-h-screen bg-white flex flex-col">
      {/* Header Section with Logo */}
      <div className="flex-1 flex flex-col items-center justify-center px-6 pt-12 pb-8">
        <div className="w-24 h-24 mb-8">
          <img src={logo} alt="Achievia Logo" className="w-full h-full object-contain" />
        </div>
        <h1 className="text-[#2C3E50] mb-2 text-center">Welcome to Achievia</h1>
        <p className="text-slate-500 text-center">Sign in to continue</p>
      </div>

      {/* Form Section */}
      <div className="flex-1 px-6 pb-8">
        <form onSubmit={handleLogin} className="space-y-5">
          {/* Email Input */}
          <div className="space-y-2">
            <Label htmlFor="email" className="text-[#2C3E50]">Email</Label>
            <div className="relative">
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 size-5 text-slate-400" />
              <Input
                id="email"
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="pl-12 h-14 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
                required
              />
            </div>
          </div>

          {/* Password Input */}
          <div className="space-y-2">
            <Label htmlFor="password" className="text-[#2C3E50]">Password</Label>
            <div className="relative">
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 size-5 text-slate-400" />
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="pl-12 pr-12 h-14 rounded-xl border-slate-200 focus:border-[#FF6B4A] focus:ring-[#FF6B4A]"
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
              >
                {showPassword ? (
                  <EyeOff className="size-5" />
                ) : (
                  <Eye className="size-5" />
                )}
              </button>
            </div>
          </div>

          {/* Forgot Password Link */}
          <div className="flex justify-end">
            <button
              type="button"
              onClick={handleForgotPassword}
              className="text-[#FF6B4A] hover:underline"
            >
              Forgot Password?
            </button>
          </div>

          {/* Login Button */}
          <Button
            type="submit"
            className="w-full h-14 rounded-xl bg-[#FF6B4A] hover:bg-[#FF5A39] text-white"
          >
            Sign In
          </Button>
        </form>

        {/* Divider */}
        <div className="flex items-center gap-4 my-8">
          <div className="flex-1 h-px bg-slate-200"></div>
          <span className="text-slate-400">OR</span>
          <div className="flex-1 h-px bg-slate-200"></div>
        </div>

        {/* Register Section */}
        <div className="text-center">
          <p className="text-slate-600 mb-4">
            Don't have an account?
          </p>
          <Button
            type="button"
            onClick={handleRegister}
            variant="outline"
            className="w-full h-14 rounded-xl border-2 border-[#FF6B4A] text-[#FF6B4A] hover:bg-[#FF6B4A] hover:text-white"
          >
            Create New Account
          </Button>
        </div>
      </div>
    </div>
  );
}