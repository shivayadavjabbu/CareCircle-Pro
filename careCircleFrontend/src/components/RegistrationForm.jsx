import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function RegistrationForm({ userType }) {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    phone: "",
    city: "",
    experience: ""
  });


const [passwordStrength, setPasswordStrength] = useState("");
const [passwordError, setPasswordError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (userType === "parent") {
      // Redirect to baby details page
      navigate("/baby-details");
    } else {
      alert("✅ Nanny registered successfully! Our team will contact you.");
    }
  };

const handlePasswordChange = (e) => {
    const newPassword = e.target.value;

    // 1. Update the form data (keep your existing logic here)
    handleChange(e);

    // 2. Check Password Length (Must be > 6 characters)
    if (newPassword.length < 8) {
        setPasswordError("Password must be at least 8 characters long.");
        setPasswordStrength("Too Short ❌");
        return; // Stop here
    } else {
        setPasswordError(""); // Clear error
    }

    // 3. Check Password Strength (Simple Logic)
    // Strong = Has Number + Special Char + Uppercase
    const hasNumber = /\d/.test(newPassword);
    const hasSpecial = /[!@#$%^&*]/.test(newPassword);
    const hasUpper = /[A-Z]/.test(newPassword);

    if (hasNumber && hasSpecial && hasUpper) {
        setPasswordStrength("Strong 💪");
    } else if (hasNumber || hasSpecial) {
        setPasswordStrength("Medium ⚠️");
    } else {
        setPasswordStrength("Weak 👎");
    }
};

  return (
    <div className="form-wrapper">
      <h2>
        {userType === "nanny"
          ? "Nanny Registration"
          : "Parent Registration"}
      </h2>

      <form className="form" onSubmit={handleSubmit}>
        <input
          name="name"
          placeholder="Full Name"
          onChange={handleChange}
          required
        />

        <input
          name="phone"
          placeholder="Phone Number"
          onChange={handleChange}
          required
        />

        <input
          name="city"
          placeholder="City"
          onChange={handleChange}
          required
        />

        {userType === "nanny" && (
          <input
            name="experience"
            placeholder="Years of Experience"
            onChange={handleChange}
            required
          />
        )}

        <button type="submit">
          {userType === "parent" ? "Continue" : "Register"}
        </button>
      </form>
    </div>
  );
}
