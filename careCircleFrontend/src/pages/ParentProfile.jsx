import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createParentProfile, getParentProfile } from "../api/parentApi";
import "./ParentDashboard.css"; // Reuse dashboard styles or create new ones

export default function ParentProfile() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: "",
    phoneNumber: "",
    address: "",
  });

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getParentProfile();
        if (data) {
          setFormData({
            fullName: data.fullName || "",
            phoneNumber: data.phoneNumber || "",
            address: data.address || "",
          });
        }
      } catch (error) {
        console.log("No profile found or fetch error:", error);
      }
    };
    fetchProfile();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      await createParentProfile(formData);
      setMessage("✅ Profile saved successfully! Redirecting to dashboard...");
      setTimeout(() => {
        navigate("/parent-dashboard");
      }, 1500);
    } catch (error) {
      console.error("Profile Save Error:", error);
      setMessage("❌ " + (error.message || "Failed to save profile"));
    } finally {
      setLoading(false);
    }
  };

  const handleSkip = () => {
    navigate("/parent-dashboard");
  };

  return (
    <div className="dashboard-container" style={{ padding: "2rem" }}>
      <div className="dashboard-card" style={{ maxWidth: "600px", margin: "0 auto" }}>
        <h2>Parent Profile</h2>
        {message && <p className="status-message">{message}</p>}

        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "1rem" }}>
          <input
            type="text"
            name="fullName"
            placeholder="Full Name"
            value={formData.fullName}
            onChange={handleChange}
            required
            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
          />

          <input
            type="tel"
            name="phoneNumber"
            placeholder="Phone Number"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
          />

          <textarea
            name="address"
            placeholder="Address"
            value={formData.address}
            onChange={handleChange}
            required
            style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc", minHeight: "80px" }}
          />

          <button
            type="submit"
            disabled={loading}
            style={{
              padding: "10px",
              backgroundColor: "#ff6b6b",
              color: "white",
              border: "none",
              borderRadius: "5px",
              cursor: "pointer",
              fontWeight: "bold"
            }}
          >
            {loading ? "Saving..." : "Save Profile"}
          </button>

          <button
            type="button"
            onClick={handleSkip}
            style={{
              padding: "10px",
              backgroundColor: "#6c757d",
              color: "white",
              border: "none",
              borderRadius: "5px",
              cursor: "pointer",
              fontWeight: "normal",
              marginTop: "10px"
            }}
          >
            Skip for now
          </button>
        </form>
      </div>
    </div>
  );
}
