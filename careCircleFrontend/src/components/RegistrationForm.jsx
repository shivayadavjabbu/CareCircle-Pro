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

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (userType === "parent") {
      // Redirect to baby details page
      navigate("/baby-details");
    } else {
      alert("✅ Caregiver registered successfully! Our team will contact you.");
    }
  };

  return (
    <div className="form-wrapper">
      <h2>
        {userType === "caregiver"
          ? "Caregiver Registration"
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

        {userType === "caregiver" && (
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
