import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RegisterParent.css";

export default function RegisterParent() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch("/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email.trim(),
          password: password.trim(),
          role: "ROLE_PARENT",
        }),
      });

      // ✅ EMAIL EXISTS
      if (response.status === 409) {
        setSuccessMessage("Email already exists. Please login.");
        setTimeout(() => navigate("/login"), 2000);
        return;
      }

      // ❌ Other errors
      if (!response.ok) {
        setSuccessMessage("Registration failed. Please try again.");
        return;
      }

      // ✅ SUCCESS
      setSuccessMessage("Registered successfully! Redirecting to login...");
      setTimeout(() => navigate("/login"), 2000);

    } catch (error) {
      setSuccessMessage("Server error. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-parent-container">
      <div className="register-parent-card">
        <h2>Register as Parent</h2>

        {successMessage && (
          <div className="success-message">{successMessage}</div>
        )}

        <form onSubmit={handleRegister}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </button>
        </form>
      </div>
    </div>
  );
}
