import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/authApi";
export default function AdminLogin() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const data = await login(email, password, "ROLE_ADMIN");

      localStorage.setItem("token", data.accessToken);
      localStorage.setItem("role", "ROLE_ADMIN");

      alert("Admin login successful!");
      navigate("/admin-dashboard");
    } catch (err) {
      console.error("Admin Login Error:", err);
      const msg = err.message.toLowerCase();
      if (msg.includes("invalid") || msg.includes("credentials") || msg.includes("unauthorized") || msg.includes("password") || msg.includes("auth")) {
        setError("Invalid username or password");
      } else {
        setError("Invalid username or password");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen pt-28 flex items-center justify-center bg-gradient-to-br from-[#fdfbfb] to-[#ebedee] p-5 font-sans">
      <div className="bg-white p-10 rounded-xl shadow-lg w-full max-w-[400px] text-center">
        <h2 className="mb-[15px] text-3xl font-bold text-gray-800">Admin Login</h2>
        <p className="mb-6 text-gray-600">Enter your credentials to access the admin dashboard</p>

        {error && <div className="bg-[#ffe0e0] text-[#d8000c] p-2 mb-[10px] rounded-md text-sm">{error}</div>}

        <form onSubmit={handleLogin}>
          <input
            className="w-full p-3 my-2.5 rounded-lg border border-gray-300 text-base focus:outline-none focus:ring-2 focus:ring-[#ff6f61]/20 focus:border-[#ff6f61]"
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            className="w-full p-3 my-2.5 rounded-lg border border-gray-300 text-base focus:outline-none focus:ring-2 focus:ring-[#ff6f61]/20 focus:border-[#ff6f61]"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button
            className="w-full p-3 mt-[15px] border-none rounded-lg bg-[#ff6f61] text-white text-base font-semibold cursor-pointer transition-colors duration-300 hover:bg-[#e85a4f] disabled:opacity-60 disabled:cursor-not-allowed shadow-md"
            type="submit"
            disabled={loading}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}
