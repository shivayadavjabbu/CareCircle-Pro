import { useState } from "react";
import { isValidPassword } from "../utils/passwordValidation";

export default function UpdatePassword() {
  const [password, setPassword] = useState("");

  const handleUpdate = (e) => {
    e.preventDefault();

    if (!isValidPassword(password)) {
      alert(
        "Password must be at least 8 characters, contain 2 numbers and 1 special character."
      );
      return;
    }

    const user = JSON.parse(localStorage.getItem("user"));
    if (!user) {
      alert("User not found");
      return;
    }

    user.password = password;
    localStorage.setItem("user", JSON.stringify(user));

    alert("Password updated successfully");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-5 font-sans">
      <div className="bg-white p-10 rounded-2xl shadow-lg w-full max-w-[400px] text-center">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Update Password</h2>

        <form className="flex flex-col gap-4" onSubmit={handleUpdate}>
          <input
            type="password"
            placeholder="New Password"
            required
            onChange={(e) => setPassword(e.target.value)}
            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500/20 focus:border-green-500 transition-all"
          />

          <button
            type="submit"
            className="w-full p-3 bg-green-600 text-white font-semibold rounded-lg hover:bg-green-700 transition-colors shadow-md"
          >
            Update Password
          </button>
        </form>
      </div>
    </div>
  );
}
