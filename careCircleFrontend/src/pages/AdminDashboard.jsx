import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createAdminProfile } from "../api/adminApi";
import "./AdminDashboard.css";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState(localStorage.getItem("userEmail") || "admin@example.com");
  const [profileInitialized, setProfileInitialized] = useState(false);

  useEffect(() => {
    // 🛡️ Auto-create Admin Profile if not exists (per guide flow, admin creates profile POST)
    // Since there's no GET /admin/profile/me in the guide, we might just try to create one on first login
    // or assume the user does it manually via settings. 
    // However, for better UX, let's try to initialize it.

    // NOTE: The guide says "Create Admin Profile". We can do this once.
    // Ideally check if exists, but we lack a GET endpoint in the guide.
    // We will just store a flag in localStorage for this session demo.

    const initProfile = async () => {
      const hasProfile = localStorage.getItem("adminProfileCreated");
      if (!hasProfile) {
        try {
          // Try to create a default admin profile
          await createAdminProfile({
            fullName: "System Admin",
            phoneNumber: "9000000000",
            adminLevel: "SUPER_ADMIN"
          });
          localStorage.setItem("adminProfileCreated", "true");
          console.log("Admin profile initialized");
          setProfileInitialized(true);
        } catch (err) {
          // If profile creation fails, redirect to profile setup page
          console.log("Admin profile creation failed, redirecting to setup:", err);
          setTimeout(() => {
            navigate("/admin-profile");
          }, 1500);
        }
      } else {
        setProfileInitialized(true);
      }
    };

    initProfile();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("adminProfileCreated");
    alert("Logged out successfully!");
    navigate("/");
  };

  return (
    <div className="admin-dashboard">
      {/* Navbar */}
      <nav className="dashboard-navbar">
        <div className="navbar-brand">👑 CareCircle Admin</div>
        <div className="navbar-actions">
          <div className="user-info">
            <div className="user-avatar">A</div>
            <span>{userEmail}</span>
          </div>
          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </nav>

      {/* Main Dashboard Content */}
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1 className="dashboard-title">Welcome Admin</h1>
          <p className="dashboard-subtitle">
            Manage users, nannies, and platform activities from here
          </p>
        </div>

        {/* Stats Section */}
        <div className="stats-section">
          <h2 className="stats-title">Platform Stats</h2>
          <div className="stats-grid">
            <div className="stat-item">
              <h3 className="stat-value">0</h3>
              <p className="stat-label">Registered Parents</p>
            </div>
            <div className="stat-item">
              <h3 className="stat-value">0</h3>
              <p className="stat-label">Registered Nannies</p>
            </div>
            <div className="stat-item">
              <h3 className="stat-value">0</h3>
              <p className="stat-label">Active Bookings</p>
            </div>
          </div>
        </div>

        {/* Action Cards */}
        <div className="dashboard-grid">
          <div className="dashboard-card">
            <div className="card-icon">👨‍👩‍👧</div>
            <h3 className="card-title">Manage Parents</h3>
            <p className="card-description">
              View, edit, or remove parent accounts
            </p>
            <button className="card-action">Go to Parents</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">👶</div>
            <h3 className="card-title">Manage Nannies</h3>
            <p className="card-description">
              Review, approve, or remove nannies
            </p>
            <button className="card-action">Go to Nannies</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">📅</div>
            <h3 className="card-title">Manage Bookings</h3>
            <p className="card-description">
              Track all active and past bookings
            </p>
            <button className="card-action">View Bookings</button>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">⚙️</div>
            <h3 className="card-title">Settings</h3>
            <p className="card-description">
              Update admin profile and platform configurations
            </p>
            <button className="card-action">Settings</button>
          </div>
        </div>
      </div>
    </div>
  );
}
