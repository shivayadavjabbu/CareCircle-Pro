import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { login } from "../api/authApi";
import { getParentProfile } from "../api/parentApi";

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState(location.state?.role || "ROLE_PARENT");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (location.state?.role) {
      setRole(location.state.role);
    }
  }, [location.state]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const data = await login(email, password, role);
      const token = data.accessToken;

      if (!token) {
        throw new Error("Token not received. Please contact support.");
      }

      const decoded = jwtDecode(token);
      localStorage.setItem("token", token);
      const tokenRole = decoded.role || role;
      localStorage.setItem("role", tokenRole);
      localStorage.setItem("userEmail", decoded.sub || email);

      if (tokenRole === "ROLE_PARENT") {
        try {
          const profile = await getParentProfile();
          if (profile && profile.fullName) {
            navigate("/parent-dashboard");
          } else {
            navigate("/parent-profile");
          }
        } catch (err) {
          // If profile fetch fails (e.g., 404), assume profile is incomplete
          navigate("/parent-profile");
        }
      } else if (tokenRole === "ROLE_CARETAKER" || tokenRole === "ROLE_CAREGIVER") {
        navigate("/nanny-profile");
      } else if (tokenRole === "ROLE_ADMIN") {
        navigate("/admin-dashboard");
      } else {
        navigate("/");
      }
    } catch (error) {
      console.error("Login error:", error);
      const msg = error.message.toLowerCase();
      if (msg.includes("invalid") || msg.includes("credentials") || msg.includes("unauthorized") || msg.includes("password") || msg.includes("auth")) {
        setError("Invalid username or password");
      } else {
        setError("Invalid username or password"); // Forcing the requested message even for generic errors during login
      }
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
          <div className="text-center mb-10">
            <div className="w-20 h-20 bg-indigo-600 rounded-2xl flex items-center justify-center text-white text-3xl mx-auto mb-6 shadow-xl shadow-indigo-100 rotate-3">C</div>
            <h2 className="text-3xl font-extrabold text-slate-900 mb-2">Welcome Back</h2>
            <p className="text-slate-500 font-medium tracking-tight mb-8">Login to your account</p>

            {/* Role Selector Tabs */}
            <div className="flex p-1.5 bg-slate-100/80 rounded-2xl mb-8 backdrop-blur-sm">
              <button
                type="button"
                onClick={() => setRole("ROLE_PARENT")}
                className={`flex-1 py-2.5 rounded-xl text-sm font-bold transition-all duration-300 ${role === "ROLE_PARENT"
                  ? "bg-white text-brand-parent shadow-sm"
                  : "text-slate-500 hover:text-slate-700"
                  }`}
              >
                👨‍👩‍👧 Parent
              </button>
              <button
                type="button"
                onClick={() => setRole("ROLE_CARETAKER")}
                className={`flex-1 py-2.5 rounded-xl text-sm font-bold transition-all duration-300 ${role === "ROLE_CARETAKER"
                  ? "bg-white text-brand-nanny shadow-sm"
                  : "text-slate-500 hover:text-slate-700"
                  }`}
              >
                👩‍⚕️ Caregiver
              </button>
            </div>
          </div>

          <form onSubmit={handleLogin} className="space-y-6">
            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 ml-1">Email Address</label>
              <input
                type="email"
                placeholder="name@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
                required
              />
            </div>

            <div className="space-y-2">
              <div className="flex justify-between items-center ml-1">
                <label className="text-sm font-bold text-slate-700">Password</label>
                <button
                  type="button"
                  onClick={() => navigate("/forgot-password")}
                  className="text-xs font-bold text-indigo-600 hover:text-indigo-700"
                >
                  Forgot?
                </button>
              </div>
              <input
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
                required
              />
            </div>

            {error && (
              <div className="p-4 bg-red-50 border border-red-100 rounded-2xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2">
                <span className="text-red-500 text-lg">⚠️</span>
                <p className="text-sm font-bold text-red-600">{error}</p>
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className={`w-full py-4 rounded-2xl font-extrabold text-white transition-all shadow-xl active:scale-95 btn-premium ${role === "ROLE_PARENT" ? "bg-brand-parent hover:bg-brand-parent-dark shadow-brand-parent/20" :
                role === "ROLE_ADMIN" ? "bg-brand-admin hover:bg-brand-admin-dark shadow-brand-admin/20" :
                  "bg-brand-nanny hover:bg-brand-nanny-dark shadow-brand-nanny/20"
                }`}
            >
              {loading ? (
                <span className="flex items-center justify-center gap-2">
                  <svg className="animate-spin h-5 w-5 text-white" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Processing...
                </span>
              ) : "Sign In"}
            </button>
          </form>

          <div className="mt-8 pt-8 border-t border-slate-100 text-center">
            <p className="text-slate-500 text-sm font-medium">
              Don't have an account?{" "}
              <button
                onClick={() => navigate(role === "ROLE_PARENT" ? "/register-parent" : "/register-nanny")}
                className="font-bold text-indigo-600 hover:underline"
              >
                Create Account
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
