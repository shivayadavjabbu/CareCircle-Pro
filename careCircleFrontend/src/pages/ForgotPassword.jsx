import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { forgotPassword } from "../api/authApi";

export default function ForgotPassword() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("ROLE_PARENT");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSendOTP = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");
    setError("");

    try {
      await forgotPassword(email, role);
      setMessage("OTP sent successfully to your email.");
      setTimeout(() => {
        navigate("/update-password", { state: { email, role } });
      }, 1500);
    } catch (err) {
      setError(err.message || "Failed to send OTP. Please try again.");
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
            <h2 className="text-3xl font-extrabold text-slate-900 mb-2">Forgot Password?</h2>
            <p className="text-slate-500 font-medium">Enter your email and role to receive an OTP.</p>
          </div>

          <form onSubmit={handleSendOTP} className="space-y-6">
            {/* Role Selector */}
            <div className="flex p-1.5 bg-slate-100/80 rounded-2xl mb-6 backdrop-blur-sm">
              <button
                type="button"
                onClick={() => setRole("ROLE_PARENT")}
                className={`flex-1 py-2.5 rounded-xl text-xs font-bold transition-all duration-300 ${role === "ROLE_PARENT"
                  ? "bg-white text-brand-parent shadow-sm"
                  : "text-slate-500 hover:text-slate-700"
                  }`}
              >
                Parent
              </button>
              <button
                type="button"
                onClick={() => setRole("ROLE_CARETAKER")}
                className={`flex-1 py-2.5 rounded-xl text-xs font-bold transition-all duration-300 ${role === "ROLE_CARETAKER"
                  ? "bg-white text-brand-caregiver shadow-sm"
                  : "text-slate-500 hover:text-slate-700"
                  }`}
              >
                Caregiver
              </button>
              <button
                type="button"
                onClick={() => setRole("ROLE_ADMIN")}
                className={`flex-1 py-2.5 rounded-xl text-xs font-bold transition-all duration-300 ${role === "ROLE_ADMIN"
                  ? "bg-white text-brand-admin shadow-sm"
                  : "text-slate-500 hover:text-slate-700"
                  }`}
              >
                Admin
              </button>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 ml-1">Email Address</label>
              <input
                type="email"
                placeholder="name@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
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
              {loading ? "Sending..." : "Send OTP"}
            </button>
            <button
              type="button"
              onClick={() => navigate("/login")}
              className="w-full text-center text-sm font-bold text-slate-500 hover:text-indigo-600 transition-colors mt-4"
            >
              Back to Login
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
