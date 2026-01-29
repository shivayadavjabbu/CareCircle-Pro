import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
export default function AdminDashboard() {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState(localStorage.getItem("userEmail") || "admin@example.com");

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("adminProfileCreated");
    alert("Logged out successfully!");
    navigate("/");
  };

  return (
    <div className="bg-[#f0f4f8] min-h-screen pt-32 font-sans">
      {/* Main Dashboard Content */}
      <div className="p-8 max-w-7xl mx-auto">
        <div className="mb-10">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">Welcome Admin</h1>
          <p className="text-gray-600">
            Manage users, nannies, and platform activities from here
          </p>
        </div>

        {/* Stats Section */}
        <div className="mb-12">
          <h2 className="text-xl font-bold text-gray-800 mb-6">Platform Stats</h2>
          <div className="flex flex-wrap gap-4">
            <div className="flex-1 min-w-[150px] bg-white rounded-xl p-5 text-center shadow-sm border border-gray-100">
              <h3 className="text-2xl font-bold text-gray-800 mb-1">0</h3>
              <p className="text-sm text-gray-500">Registered Parents</p>
            </div>
            <div className="flex-1 min-w-[150px] bg-white rounded-xl p-5 text-center shadow-sm border border-gray-100">
              <h3 className="text-2xl font-bold text-gray-800 mb-1">0</h3>
              <p className="text-sm text-gray-500">Registered Nannies</p>
            </div>
            <div className="flex-1 min-w-[150px] bg-white rounded-xl p-5 text-center shadow-sm border border-gray-100">
              <h3 className="text-2xl font-bold text-gray-800 mb-1">0</h3>
              <p className="text-sm text-gray-500">Active Bookings</p>
            </div>
          </div>
        </div>

        {/* Action Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div className="bg-white rounded-xl p-6 text-center shadow-sm border border-gray-100 hover:-translate-y-1 hover:shadow-md transition-all duration-300 group">
            <div className="text-4xl mb-4 group-hover:scale-110 transition-transform duration-300">👨‍👩‍👧</div>
            <h3 className="text-lg font-bold text-gray-800 mb-2">Manage Parents</h3>
            <p className="text-sm text-gray-500 mb-5">
              View, edit, or remove parent accounts
            </p>
            <button className="bg-[#ff9800] text-white px-4 py-2 rounded-md font-semibold text-sm hover:bg-[#e68900] transition-colors duration-300 w-full">Go to Parents</button>
          </div>

          <div className="bg-white rounded-xl p-6 text-center shadow-sm border border-gray-100 hover:-translate-y-1 hover:shadow-md transition-all duration-300 group">
            <div className="text-4xl mb-4 group-hover:scale-110 transition-transform duration-300">👶</div>
            <h3 className="text-lg font-bold text-gray-800 mb-2">Manage Nannies</h3>
            <p className="text-sm text-gray-500 mb-5">
              Review, approve, or remove nannies
            </p>
            <button className="bg-[#ff9800] text-white px-4 py-2 rounded-md font-semibold text-sm hover:bg-[#e68900] transition-colors duration-300 w-full">Go to Nannies</button>
          </div>

          <div className="bg-white rounded-xl p-6 text-center shadow-sm border border-gray-100 hover:-translate-y-1 hover:shadow-md transition-all duration-300 group">
            <div className="text-4xl mb-4 group-hover:scale-110 transition-transform duration-300">📅</div>
            <h3 className="text-lg font-bold text-gray-800 mb-2">Manage Bookings</h3>
            <p className="text-sm text-gray-500 mb-5">
              Track all active and past bookings
            </p>
            <button className="bg-[#ff9800] text-white px-4 py-2 rounded-md font-semibold text-sm hover:bg-[#e68900] transition-colors duration-300 w-full">View Bookings</button>
          </div>

          <div className="bg-white rounded-xl p-6 text-center shadow-sm border border-gray-100 hover:-translate-y-1 hover:shadow-md transition-all duration-300 group">
            <div className="text-4xl mb-4 group-hover:scale-110 transition-transform duration-300">⚙️</div>
            <h3 className="text-lg font-bold text-gray-800 mb-2">Settings</h3>
            <p className="text-sm text-gray-500 mb-5">
              Update admin profile and platform configurations
            </p>
            <button className="bg-[#ff9800] text-white px-4 py-2 rounded-md font-semibold text-sm hover:bg-[#e68900] transition-colors duration-300 w-full">Settings</button>
          </div>
        </div>
      </div>
    </div>
  );
}
