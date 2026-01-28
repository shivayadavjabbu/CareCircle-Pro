import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createAdminProfile } from "../api/adminApi";
import "./ParentDashboard.css"; // Reuse parent dashboard styles

export default function AdminProfile() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        fullName: "",
        phoneNumber: "",
        adminLevel: "ADMIN",
    });

    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage("");

        try {
            await createAdminProfile(formData);
            setMessage("✅ Admin profile created successfully!");
            setTimeout(() => {
                navigate("/admin-dashboard");
            }, 1500);
        } catch (error) {
            console.error("Profile Save Error:", error);
            setMessage("❌ " + (error.message || "Failed to save profile"));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="dashboard-container" style={{ padding: "2rem" }}>
            <div className="dashboard-card" style={{ maxWidth: "600px", margin: "0 auto" }}>
                <h2>🛡️ Admin Profile Setup</h2>
                <p style={{ color: "#666", marginBottom: "1.5rem" }}>
                    Complete your admin profile to access the dashboard
                </p>

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

                    <select
                        name="adminLevel"
                        value={formData.adminLevel}
                        onChange={handleChange}
                        required
                        style={{ padding: "10px", borderRadius: "5px", border: "1px solid #ccc" }}
                    >
                        <option value="ADMIN">Admin</option>
                        <option value="SUPER_ADMIN">Super Admin</option>
                    </select>

                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            padding: "10px",
                            backgroundColor: "#ff9800",
                            color: "white",
                            border: "none",
                            borderRadius: "5px",
                            cursor: "pointer",
                            fontWeight: "bold"
                        }}
                    >
                        {loading ? "Creating Profile..." : "Create Admin Profile"}
                    </button>
                </form>
            </div>
        </div>
    );
}
