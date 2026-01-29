import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getParentProfile, getChildren } from "../api/parentApi";
export default function ParentDashboard() {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState(localStorage.getItem("userEmail") || "");
  const [profile, setProfile] = useState(null);
  const [childrenCount, setChildrenCount] = useState(0);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [profileData, childrenData] = await Promise.all([
          getParentProfile(),
          getChildren()
        ]);
        setProfile(profileData);
        if (profileData.email) setUserEmail(profileData.email);
        setChildrenCount(childrenData.length || 0);
      } catch (err) {
        console.error("Failed to fetch dashboard data", err);
        setError("Could not load dashboard data. Please complete your profile.");
      }
    };
    fetchDashboardData();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    alert("Logged out successfully!");
    navigate("/");
  };

  return (
    <div className="min-h-screen pt-32 bg-[#5c56d6] font-sans pb-20">
      {/* Main Content */}
      <div className="max-w-[1200px] mx-auto px-5">
        <div className="text-center text-white mb-12">
          <h1 className="text-[42px] font-bold m-0 mb-3 tracking-tight">Welcome to Your Dashboard</h1>
          <p className="text-xl opacity-80 m-0">
            Manage your childcare needs all in one place
          </p>
          {error && <p className="text-red-200 mt-4 font-semibold p-3 bg-red-500/20 rounded-xl inline-block">{error}</p>}
        </div>

        {/* Stats Section */}
        <div className="bg-white rounded-[2rem] p-10 shadow-2xl mb-12">
          <h2 className="text-2xl font-bold text-slate-800 mb-8 ml-2">Your Activity</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center p-8 bg-white border border-slate-100 rounded-3xl shadow-[0_10px_40px_-15px_rgba(0,0,0,0.1)] transition-transform hover:scale-[1.02]">
              <h3 className="text-4xl font-black text-indigo-600 mb-2">0</h3>
              <p className="text-sm font-bold text-slate-500 uppercase tracking-widest">Active Bookings</p>
            </div>
            <div className="text-center p-8 bg-white border border-slate-100 rounded-3xl shadow-[0_10px_40px_-15px_rgba(0,0,0,0.1)] transition-transform hover:scale-[1.02]">
              <h3 className="text-4xl font-black text-indigo-600 mb-2">{childrenCount}</h3>
              <p className="text-sm font-bold text-slate-500 uppercase tracking-widest">Registered Children</p>
            </div>
            <div className="text-center p-8 bg-white border border-slate-100 rounded-3xl shadow-[0_10px_40px_-15px_rgba(0,0,0,0.1)] transition-transform hover:scale-[1.02]">
              <h3 className="text-4xl font-black text-indigo-600 mb-2">0</h3>
              <p className="text-sm font-bold text-slate-500 uppercase tracking-widest">Reviews Given</p>
            </div>
          </div>
        </div>

        {/* Action Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-5 gap-6">
          {/* Find a Nanny */}
          <div className="bg-white rounded-[2rem] p-8 shadow-xl flex flex-col items-center text-center transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl">
            <div className="w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center text-3xl mb-6 shadow-inner">🔍</div>
            <h3 className="text-lg font-bold text-slate-800 mb-3">Find a Nanny</h3>
            <p className="text-xs text-slate-500 leading-relaxed mb-8">
              Browse through verified and trusted nannies in your area
            </p>
            <button
              onClick={() => navigate("/find-nanny")}
              className="mt-auto w-full py-3 bg-[#ff9800] text-white rounded-xl font-bold text-sm shadow-lg shadow-orange-200 hover:bg-[#e68a00] transition-colors"
            >
              Search Nannies
            </button>
          </div>

          {/* My Bookings */}
          <div className="bg-white rounded-[2rem] p-8 shadow-xl flex flex-col items-center text-center transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl">
            <div className="w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center text-3xl mb-6 shadow-inner">📅</div>
            <h3 className="text-lg font-bold text-slate-800 mb-3">My Bookings</h3>
            <p className="text-xs text-slate-500 leading-relaxed mb-8">
              View and manage your current and upcoming bookings
            </p>
            <button
              onClick={() => navigate("/my-bookings")}
              className="mt-auto w-full py-3 bg-[#ff9800] text-white rounded-xl font-bold text-sm shadow-lg shadow-orange-200 hover:bg-[#e68a00] transition-colors"
            >
              View Bookings
            </button>
          </div>

          {/* Baby Details */}
          <div className="bg-white rounded-[2rem] p-8 shadow-xl flex flex-col items-center text-center transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl">
            <div className="w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center text-3xl mb-6 shadow-inner">👶</div>
            <h3 className="text-lg font-bold text-slate-800 mb-3">Baby Details</h3>
            <p className="text-xs text-slate-500 leading-relaxed mb-8">
              Add or update your child's information and preferences
            </p>
            <button
              onClick={() => navigate("/baby-details")}
              className="mt-auto w-full py-3 bg-[#ff9800] text-white rounded-xl font-bold text-sm shadow-lg shadow-orange-200 hover:bg-[#e68a00] transition-colors"
            >
              Manage Details
            </button>
          </div>

          {/* Favorites */}
          <div className="bg-white rounded-[2rem] p-8 shadow-xl flex flex-col items-center text-center transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl">
            <div className="w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center text-3xl mb-6 shadow-inner">⭐</div>
            <h3 className="text-lg font-bold text-slate-800 mb-3">Favorites</h3>
            <p className="text-xs text-slate-500 leading-relaxed mb-8">
              Quick access to your favorite and trusted nannies
            </p>
            <button
              onClick={() => navigate("/favorites")}
              className="mt-auto w-full py-3 bg-[#ff9800] text-white rounded-xl font-bold text-sm shadow-lg shadow-orange-200 hover:bg-[#e68a00] transition-colors"
            >
              View Favorites
            </button>
          </div>

          {/* Messages */}
          <div className="bg-white rounded-[2rem] p-8 shadow-xl flex flex-col items-center text-center transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl">
            <div className="w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center text-3xl mb-6 shadow-inner">💬</div>
            <h3 className="text-lg font-bold text-slate-800 mb-3">Messages</h3>
            <p className="text-xs text-slate-500 leading-relaxed mb-8">
              Chat with nannies and manage your conversations
            </p>
            <button
              onClick={() => navigate("/messages")}
              className="mt-auto w-full py-3 bg-[#ff9800] text-white rounded-xl font-bold text-sm shadow-lg shadow-orange-200 hover:bg-[#e68a00] transition-colors"
            >
              Open Messages
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
