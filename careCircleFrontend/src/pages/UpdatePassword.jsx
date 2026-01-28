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
    <div className="container">
      <h2>Update Password</h2>

      <form className="form" onSubmit={handleUpdate}>
        <input
          type="password"
          placeholder="New Password"
          required
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="submit">Update Password</button>
      </form>
    </div>
  );
}
