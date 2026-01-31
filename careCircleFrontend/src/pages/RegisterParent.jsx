import { useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import PasswordInput from "../components/PasswordInput";
import { register } from "../api/authApi";

export default function RegisterParent() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(""); // Clear previous errors
    setSuccessMessage(""); // Clear previous success messages

    try {
      await register(email, password, "ROLE_PARENT");

      setSuccessMessage("OTP Sent to email! Redirecting to verification...");
      setTimeout(() => navigate("/verify-account", { state: { email, role: "ROLE_PARENT" } }), 1500);

    } catch (error) {
      console.error("Registration error:", error);
      const msg = error.message ? error.message.toLowerCase() : "unknown error";
      if (/exist|already|taken|conflict/i.test(msg)) {
        setError("Email already exists. Please login.");
        setTimeout(() => navigate("/login", { state: { role: "ROLE_PARENT" } }), 2000);
      } else {
        setError(error.message || "Server error. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#F5F5F7] p-6 relative overflow-hidden">
      {/* Background Ambience */}
      <div className="absolute top-[-20%] right-[-10%] w-[60%] h-[60%] bg-blue-100/40 rounded-full blur-[120px] pointer-events-none"></div>

      <div className="w-full max-w-[420px] relative z-10 animate-fade-in">
        <div className="glass-panel p-8 md:p-10 rounded-[2rem] shadow-2xl shadow-blue-500/5">
          <div className="text-center mb-8">
            <img src={logo} alt="CareCircle Logo" className="w-16 h-16 mx-auto mb-6 drop-shadow-md rounded-2xl" />
            <h2 className="text-2xl font-semibold text-[#1D1D1F] tracking-tight">Create Account</h2>
            <p className="text-[#86868b] text-sm mt-2">Join CareCircle as a Parent</p>
          </div>

          {error && (
            <div className="mb-6 bg-red-50 p-3 rounded-xl border border-red-100 text-red-600 text-sm font-medium text-center animate-fade-in">
              {error}
            </div>
          )}

          {successMessage && (
            <div className="mb-6 bg-green-50 p-3 rounded-xl border border-green-100 text-green-600 text-sm font-medium text-center animate-fade-in">
              {successMessage}
            </div>
          )}

          <form onSubmit={handleRegister} className="space-y-5">
            <div className="input-group-dynamic">
              <input
                id="email"
                className="input-dynamic peer"
                type="email"
                placeholder=" "
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              <label htmlFor="email" className="label-dynamic">
                Email Address
              </label>
            </div>

            <PasswordInput
              id="password"
              placeholder="Create Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              showStrengthMeter={true}
            />

            <button
              className="btn-primary w-full justify-center flex items-center gap-2"
              type="submit"
              disabled={loading}
            >
              {loading ? "Creating Account..." : "Sign Up"}
            </button>
          </form>

          <div className="mt-8 text-center border-t border-slate-100 pt-6">
            <p className="text-[#86868b] text-sm">
              Already have an account?{" "}
              <button
                onClick={() => navigate("/login", { state: { role: "ROLE_PARENT" } })}
                className="text-[#0071e3] font-medium hover:underline"
              >
                Log In
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}


