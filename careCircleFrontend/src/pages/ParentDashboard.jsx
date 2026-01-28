import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getParentProfile } from "../api/parentApi";
import "./ParentDashboard.css";

export default function ParentDashboard() {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState(localStorage.getItem("userEmail") || "");
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getParentProfile();
        setProfile(data);
        if (data.email) setUserEmail(data.email);
      } catch (err) {
        console.error("Failed to fetch profile", err);
        setError("Could not load profile. Please complete your profile.");
      }
    };
    fetchProfile();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    alert("Logged out successfully!");
    navigate("/");
  };

  return (
    <div className="parent-dashboard">
      {/* Navbar */}
      <nav className="dashboard-navbar">
        <div className="navbar-brand">
          👨‍👩‍👧 CareCircle Parent
        </div>
        <div className="navbar-actions">
          <div className="user-info">
            <div className="user-avatar">P</div>
            <span>{profile ? profile.fullName : userEmail}</span>
          </div>
          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1 className="dashboard-title">Welcome to Your Dashboard</h1>
          <p className="dashboard-subtitle">
            Manage your childcare needs all in one place
          </p>
          {error && <p className="error-message">{error}</p>}
        </div>

        {/* Stats Section */}
        <div className="stats-section">
          <h2 className="stats-title">Your Activity</h2>
          <div className="stats-grid">
            <div className="stat-item">
              <h3 className="stat-value">{profile ? "Active" : "-"}</h3>
              <p className="stat-label">Profile Status</p>
            </div>
            {/* Add more real stats if available from API */}
          </div>
        </div>

        {/* Action Cards */}
        <div className="dashboard-grid">
          <div className="dashboard-card">
            <div className="card-icon">🔍</div>
            <h3 className="card-title">Find a Nanny</h3>
            <p className="card-description">
              Browse through verified and trusted nannies in your area
            </p>
            <button
              className="card-action"
              // Note: Create this route or page if it doesn't exist, or link to a list page
              onClick={() => navigate("/find-nanny")}
            >
              Search Nannies
            </button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">👶</div>
            <h3 className="card-title">Baby Details</h3>
            <p className="card-description">
              Add or update your child's information and preferences
            </p>
            <button
              className="card-action"
              onClick={() => navigate("/baby-details")}
            >
              Manage Details
            </button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">⚙️</div>
            <h3 className="card-title">Settings</h3>
            <p className="card-description">
              Update your profile, preferences, and account settings
            </p>
            <button
              className="card-action"
              // Note: Assuming /register-parent or a new /parent-profile page handles profile creation/edit
              // For now leaving as is or redirecting to specific profile edit page
              onClick={() => navigate("/register-parent")}
            >
              Update Profile
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
