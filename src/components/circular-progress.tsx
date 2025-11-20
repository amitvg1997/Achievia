import { useEffect, useState } from "react";
import { motion } from "motion/react";

interface CircularProgressProps {
  progress: number;
  size?: number;
  strokeWidth?: number;
}

export function CircularProgress({
  progress,
  size = 80,
  strokeWidth = 8,
}: CircularProgressProps) {
  const [animatedProgress, setAnimatedProgress] = useState(0);
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;

  useEffect(() => {
    const timer = setTimeout(() => {
      setAnimatedProgress(progress);
    }, 100);
    return () => clearTimeout(timer);
  }, [progress]);

  // Color based on progress
  const getColor = () => {
    if (progress >= 75) return "url(#gradient-green)";
    if (progress >= 50) return "url(#gradient-blue)";
    if (progress >= 25) return "url(#gradient-orange)";
    return "url(#gradient-red)";
  };

  const offset = circumference - (animatedProgress / 100) * circumference;

  return (
    <div className="relative inline-flex items-center justify-center">
      <svg width={size} height={size} className="transform -rotate-90">
        {/* Gradient Definitions */}
        <defs>
          <linearGradient id="gradient-green" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#10b981" />
            <stop offset="100%" stopColor="#059669" />
          </linearGradient>
          <linearGradient id="gradient-blue" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#3b82f6" />
            <stop offset="100%" stopColor="#2563eb" />
          </linearGradient>
          <linearGradient id="gradient-orange" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#FF6B4A" />
            <stop offset="100%" stopColor="#f97316" />
          </linearGradient>
          <linearGradient id="gradient-red" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#ef4444" />
            <stop offset="100%" stopColor="#dc2626" />
          </linearGradient>
        </defs>

        {/* Background circle */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke="#F1F5F9"
          strokeWidth={strokeWidth}
        />
        
        {/* Progress circle with motion */}
        <motion.circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke={getColor()}
          strokeWidth={strokeWidth}
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          strokeLinecap="round"
          initial={{ strokeDashoffset: circumference }}
          animate={{ strokeDashoffset: offset }}
          transition={{
            duration: 1.5,
            ease: "easeOut",
          }}
        />
      </svg>
      
      {/* Percentage text with animation */}
      <motion.div 
        className="absolute inset-0 flex items-center justify-center"
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5, delay: 0.2 }}
      >
        <span className="text-[#2C3E50]">{progress}%</span>
      </motion.div>
    </div>
  );
}
