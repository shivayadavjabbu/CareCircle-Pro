import { useState } from "react";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");

  const reset = (e) => {
    e.preventDefault();
    alert("Password reset link sent (mock)");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-5 font-sans">
      <div className="bg-white p-10 rounded-2xl shadow-lg w-full max-w-[400px] text-center">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Forgot Password</h2>

        <form className="flex flex-col gap-4" onSubmit={reset}>
          <input
            type="email"
            placeholder="Registered Email"
            required
            onChange={(e) => setEmail(e.target.value)}
            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all"
          />
          <button
            type="submit"
            className="w-full p-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors shadow-md"
          >
            Send Reset Link
          </button>
        </form>
      </div>
    </div>
  );
}
