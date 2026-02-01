import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import PasswordInput from "../components/PasswordInput";
import { login } from "../api/authApi";
import { getParentProfile } from "../api/parentApi";
import logo from "../assets/logo.png";

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
    if (location.state?.email) {
      setEmail(location.state.email);
    }
  }, [location.state]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const data = await login(email, password, role);
      const token = data.accessToken;
      if (!token) throw new Error("Token not received.");

      const decoded = jwtDecode(token);
      localStorage.setItem("token", token);
      const tokenRole = decoded.role || role;
      localStorage.setItem("role", tokenRole);
      localStorage.setItem("userEmail", decoded.sub || email);

      if (tokenRole === "ROLE_PARENT") {
        try {
          const profile = await getParentProfile();
          navigate(profile && profile.fullName ? "/parent-dashboard" : "/parent-profile");
        } catch {
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
      setError(
        msg.includes("invalid") || msg.includes("credentials") || msg.includes("auth")
          ? "Invalid email or password."
          : "Login failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center pt-32 bg-[#f5f5f7] px-6">

      {/* Apple ID Style Header */}
      <div className="mb-10 text-center">
        <img src={logo} alt="Logo" className="h-12 w-12 mx-auto mb-4 opacity-80" />
        <h1 className="text-[28px] font-semibold text-[#1d1d1f]">Sign in to CareCircle</h1>
      </div>

      <div className="w-full max-w-[440px] bg-white rounded-2xl p-10 shadow-sm border border-[#d2d2d7]">
        <form onSubmit={handleLogin} className="space-y-6">

          {/* Role Toggle */}
          <div className="flex justify-center p-1 bg-[#f5f5f7] rounded-lg mb-4">
            <button
              type="button"
              onClick={() => setRole("ROLE_PARENT")}
              className={`flex-1 py-1.5 rounded-md text-[13px] font-medium transition-all ${role === "ROLE_PARENT" ? "bg-white shadow-sm text-[#1d1d1f]" : "text-[#86868b] hover:text-[#1d1d1f]"}`}
            >
              Parent
            </button>
            <button
              type="button"
              onClick={() => setRole("ROLE_CARETAKER")}
              className={`flex-1 py-1.5 rounded-md text-[13px] font-medium transition-all ${role === "ROLE_CARETAKER" ? "bg-white shadow-sm text-[#1d1d1f]" : "text-[#86868b] hover:text-[#1d1d1f]"}`}
            >
              Caregiver
            </button>
            <button
              type="button"
              onClick={() => setRole("ROLE_ADMIN")}
              className={`flex-1 py-1.5 rounded-md text-[13px] font-medium transition-all ${role === "ROLE_ADMIN" ? "bg-white shadow-sm text-[#1d1d1f]" : "text-[#86868b] hover:text-[#1d1d1f]"}`}
            >
              Admin
            </button>
          </div>

          <div className="space-y-4">
            <div>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="input-apple"
                placeholder="Email or Phone Number"
                required
              />
            </div>
            <div>
              <PasswordInput
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
                showStrengthMeter={false}
                className="border border-[#d2d2d7] rounded-lg overflow-hidden focus-within:ring-4 focus-within:ring-[#0071e3]/20 focus-within:border-[#0071e3]"
              />
            </div>
          </div>

          {error && (
            <div className="text-[#ff3b30] text-sm text-center">
              {error}
            </div>
          )}

          <div className="flex flex-col gap-4 mt-8">
            <button
              type="submit"
              disabled={loading}
              className="btn-apple-primary w-full py-3 text-[17px] flex justify-center items-center gap-2"
            >
              {loading && <span className="animate-spin h-4 w-4 border-2 border-white border-t-transparent rounded-full"></span>}
              Sign In
            </button>

            <div className="flex justify-between items-center mt-2">
              <div className="h-[1px] bg-[#d2d2d7] flex-1"></div>
              <span className="px-4 text-[#86868b] text-sm">or</span>
              <div className="h-[1px] bg-[#d2d2d7] flex-1"></div>
            </div>

            <button
              type="button"
              onClick={() => navigate("/register", { state: { role } })}
              className="text-[#0071e3] text-sm font-medium hover:underline text-center"
            >
              Create your CareCircle ID
            </button>
            <button
              type="button"
              onClick={() => navigate("/forgot-password")}
              className="text-[#0071e3] text-sm hover:underline text-center -mt-2"
            >
              Forgotten your password?
            </button>
          </div>

        </form>
      </div >

      <footer className="mt-12 text-[#86868b] text-xs">
        <p>Copyright © 2026 CareCircle Inc. All rights reserved.</p>
        <div className="flex gap-4 justify-center mt-2">
          <a href="#" className="hover:underline">Privacy Policy</a>
          <span>|</span>
          <a href="#" className="hover:underline">Terms of Use</a>
        </div>
      </footer>
    </div >
  );
}
