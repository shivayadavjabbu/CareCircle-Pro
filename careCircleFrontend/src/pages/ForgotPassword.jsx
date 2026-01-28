import { useState } from "react";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");

  const reset = (e) => {
    e.preventDefault();
    alert("Password reset link sent (mock)");
  };

  return (
    <div className="auth-container">
      <h2>Forgot Password</h2>

      <form className="form" onSubmit={reset}>
        <input type="email" placeholder="Registered Email" required onChange={(e) => setEmail(e.target.value)} />
        <button type="submit">Send Reset Link</button>
      </form>
    </div>
  );
}
