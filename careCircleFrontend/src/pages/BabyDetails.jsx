import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { addChild } from "../api/parentApi";
import "./BabyDetails.css";

export default function BabyDetails() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    age: "",
    gender: "",
    specialNeeds: "",
  });

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const payload = {
        name: form.name.trim(),
        age: Number(form.age),
        gender: form.gender,
        specialNeeds: form.specialNeeds.trim(),
      };

      await addChild(payload);

      setMessage("✅ Child saved successfully!");

      // Reset form
      setForm({
        name: "",
        age: "",
        gender: "",
        specialNeeds: "",
      });

      // Redirect after success
      setTimeout(() => navigate("/parent-dashboard"), 1200);

    } catch (error) {
      console.error("SAVE ERROR:", error);
      setMessage("❌ " + (error.message || "Failed to save child"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="baby-details-container">
      <form className="baby-details-card" onSubmit={handleSubmit}>
        <div className="baby-details-header">
          <div className="baby-details-icon">👶</div>
          <h2 className="baby-details-title">Add Child</h2>
          <p className="baby-details-subtitle">Enter your child's information</p>
        </div>

        {message && <p className="status-message">{message}</p>}

        <div className="baby-details-form">
          <div className="form-group">
            <label htmlFor="name">Child Name</label>
            <input
              id="name"
              type="text"
              name="name"
              placeholder="Enter child's name"
              value={form.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="age">Age</label>
              <input
                id="age"
                type="number"
                name="age"
                placeholder="Age"
                value={form.age}
                onChange={handleChange}
                min="0"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="gender">Gender</label>
              <select
                id="gender"
                name="gender"
                value={form.gender}
                onChange={handleChange}
                required
              >
                <option value="">Select Gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="specialNeeds">Special Needs (Optional)</label>
            <textarea
              id="specialNeeds"
              name="specialNeeds"
              placeholder="Any special needs or requirements..."
              value={form.specialNeeds}
              onChange={handleChange}
            />
          </div>

          <button type="submit" className="baby-details-btn" disabled={loading}>
            {loading ? "Saving..." : "Save Child"}
          </button>
        </div>
      </form>
    </div>
  );
}
