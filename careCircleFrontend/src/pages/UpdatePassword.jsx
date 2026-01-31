import { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { resetPassword } from "../api/authApi";
import { isValidPassword } from "../utils/passwordValidation";

export default function UpdatePassword() {
  const location = useLocation();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (location.state?.email && location.state?.role) {
      setEmail(location.state.email);
      setRole(location.state.role);
    } else {
      // If accessed directly without state, redirect to forgot password
      navigate("/forgot-password");
    }
  }, [location, navigate]);

  const handleReset = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setMessage("");

    if (newPassword !== confirmPassword) {
      setError("Passwords do not match");
      setLoading(false);
      return;
    }

    if (!isValidPassword(newPassword)) {
      setError(
        "Password must be at least 8 characters, contain 2 numbers and 1 special character."
      );
      setLoading(false);
      return;
    }

    try {
      await resetPassword(email, otp, newPassword, role);
      setMessage("Password updated successfully! Redirecting to login...");
      setTimeout(() => {
        navigate("/login", { state: { role } });
      }, 2000);
    } catch (err) {
      setError(err.message || "Failed to reset password. Please check OTP and try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-50 via-white to-indigo-50/30 p-6 font-sans">
      <div className="absolute inset-0 z-0 overflow-hidden pointer-events-none">
        <div className="absolute top-[10%] left-[10%] w-[30%] h-[30%] bg-indigo-200/20 blur-[100px] rounded-full animate-float"></div>
        <div className="absolute bottom-[10%] right-[10%] w-[30%] h-[30%] bg-pink-200/20 blur-[100px] rounded-full animate-float" style={{ animationDelay: '-1.5s' }}></div>
      </div>

      <div className="relative z-10 w-full max-w-[480px] animate-fade-in-up">
        <div className="glass-card rounded-[2.5rem] p-10 md:p-12 border-white/50 shadow-2xl">
          <div className="text-center mb-8">
            <h2 className="text-3xl font-extrabold text-slate-900 mb-2">Reset Password</h2>
            <p className="text-slate-500 font-medium">Enter the OTP sent to {email}</p>
          </div>

          <form onSubmit={handleReset} className="space-y-6">
            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 ml-1">OTP Code</label>
              <input
                type="text"
                placeholder="Enter OTP"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
              />
            </div>

            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 ml-1">New Password</label>
              <input
                type="password"
                placeholder="••••••••"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
              />
            </div>

            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 ml-1">Confirm Password</label>
              <input
                type="password"
                placeholder="••••••••"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
              />
            </div>

            {error && (
              <div className="p-4 bg-red-50 border border-red-100 rounded-2xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2">
                <span className="text-red-500 text-lg">⚠️</span>
                <p className="text-sm font-bold text-red-600">{error}</p>
              </div>
            )}

            {message && (
              <div className="p-4 bg-green-50 border border-green-100 rounded-2xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2">
                <span className="text-green-500 text-lg">✅</span>
                <p className="text-sm font-bold text-green-600">{message}</p>
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full py-4 rounded-2xl font-extrabold text-white transition-all shadow-xl active:scale-95 bg-indigo-600 hover:bg-indigo-700 shadow-indigo-200"
            >
              {loading ? "Resetting..." : "Reset Password"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
