import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAdminStatistics } from "../api/adminApi";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [userEmail, setUserEmail] = useState(localStorage.getItem("userEmail") || "admin@example.com");
  const [stats, setStats] = useState({
    totalParents: 0,
    totalChildren: 0,
    totalCaregivers: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await getAdminStatistics();
        setStats(data);
      } catch (err) {
        console.error("Failed to fetch admin stats:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("adminProfileCreated");
    alert("Logged out successfully!");
    navigate("/");
  };

  if (loading) {
    return (
      <div className="min-h-screen pt-[80px] flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#0071e3]"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen pt-[80px] pb-12 px-6 font-sans">
      <div className="max-w-[1024px] mx-auto">

        {/* Header */}
        <div className="mb-10 flex justify-between items-end">
          <div>
            <h1 className="text-[40px] font-semibold text-[#1d1d1f] tracking-tight">Admin Dashboard</h1>
            <p className="text-[#86868b] text-[17px]">Overview of platform activity.</p>
          </div>
          <button
            onClick={handleLogout}
            className="text-sm font-medium text-[#0071e3] hover:underline"
          >
            Sign Out
          </button>
        </div>

        {/* Bento Grid Layout */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 auto-rows-[180px]">

          {/* Parents Stats Card (Large) */}
          <div className="card-apple md:col-span-2 row-span-1 flex flex-col justify-center items-start relative overflow-hidden group cursor-pointer" onClick={() => navigate("/admin/parents")}>
            <div className="z-10">
              <h3 className="text-[#86868b] font-medium text-sm uppercase tracking-wider mb-1">Total Parents</h3>
              <div className="text-[48px] font-semibold text-[#1d1d1f]">{stats.totalParents}</div>
            </div>
            <div className="absolute right-6 top-6 opacity-10 group-hover:opacity-20 transition-opacity">
              <svg className="w-32 h-32" fill="currentColor" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" /></svg>
            </div>
          </div>

          {/* Nannies Stats Card */}
          <div className="card-apple flex flex-col justify-between group cursor-pointer" onClick={() => navigate("/admin/nannies")}>
            <div className="flex justify-between items-start">
              <div className="bg-[#5856d6] p-2 rounded-lg text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
                </svg>
              </div>
              <span className="text-[#1d1d1f] font-bold text-2xl">{stats.totalCaregivers}</span>
            </div>
            <div>
              <h3 className="font-semibold text-[#1d1d1f]">Nannies</h3>
              <p className="text-xs text-[#86868b]">Verified Caregivers</p>
            </div>
          </div>

          {/* Children Stats Card (Replaced Bookings) */}
          <div className="card-apple flex flex-col justify-between group cursor-pointer" onClick={() => navigate("/admin/parents")}>
            <div className="flex justify-between items-start">
              <div className="bg-[#34c759] p-2 rounded-lg text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M12 21a9.004 9.004 0 0 0 8.716-6.747M12 21a9.004 9.004 0 0 1-8.716-6.747M12 21c2.485 0 4.5-4.03 4.5-9S14.485 3 12 3m0 18c-2.485 0-4.5-4.03-4.5-9S9.515 3 12 3m0 0a8.997 8.997 0 0 1 7.843 4.582M12 3a8.997 8.997 0 0 0-7.843 4.582m15.686 0A11.953 11.953 0 0 1 12 10.5c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0 1 21 12c0 .778-.099 1.533-.284 2.253m0 0A11.952 11.952 0 0 1 12 15c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0 1 3 12c0-.778.099-1.533.284-2.253" />
                </svg>
              </div>
              <span className="text-[#1d1d1f] font-bold text-2xl">{stats.totalChildren}</span>
            </div>
            <div>
              <h3 className="font-semibold text-[#1d1d1f]">Total Children</h3>
              <p className="text-xs text-[#86868b]">Registered Kids</p>
            </div>
          </div>

          {/* Manage Parents (Action) */}
          <div className="card-apple md:col-start-1 md:row-span-2 flex flex-col justify-end p-8 bg-gradient-to-br from-[#0071e3] to-[#42a5f5] text-white cursor-pointer hover:shadow-apple-hover" onClick={() => navigate("/admin/parents")}>
            <div className="mb-4">
              <svg className="w-12 h-12 text-white/80" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" d="M18 18.72a9.094 9.094 0 0 0 3.741-.479 3 3 0 0 0-4.682-2.72m.94 3.198.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0 1 12 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 0 1 6 18.719m12 0a5.971 5.971 0 0 0-.941-3.197m0 0A5.995 5.995 0 0 0 12 12.75a5.995 5.995 0 0 0-5.058 2.772m0 0a3 3 0 0 0-4.681 2.72 8.986 8.986 0 0 0 3.74.477m.94-3.197a5.971 5.971 0 0 0-.94 3.197M15 6.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Zm6 3a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Zm-13.5 0a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Z" />
              </svg>
            </div>
            <h3 className="text-3xl font-bold mb-2">Manage Parents</h3>
            <p className="text-white/80 text-sm mb-6">View, edit, or disable parent accounts.</p>
            <button className="bg-white text-[#0071e3] px-6 py-3 rounded-full font-semibold text-sm w-fit hover:bg-white/90 transition-colors">View All</button>
          </div>

          {/* Manage Nannies (Action) */}
          <div className="card-apple md:col-span-2 flex items-center justify-between cursor-pointer group" onClick={() => navigate("/admin/nannies")}>
            <div className="flex items-center gap-4">
              <div className="bg-[#ff9500] p-3 rounded-full text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
                </svg>
              </div>
              <div>
                <h3 className="font-semibold text-[#1d1d1f]">Manage Caregivers</h3>
                <p className="text-sm text-gray-500">Verify certifications & profiles</p>
              </div>
            </div>
            <div className="text-[#d2d2d7] group-hover:text-[#1d1d1f] transition-colors">›</div>
          </div>

          {/* Manage Bookings (Action) */}
          <div className="card-apple md:col-span-2 flex items-center justify-between cursor-pointer group" onClick={() => navigate("/admin/bookings")}>
            <div className="flex items-center gap-4">
              <div className="bg-[#0071e3] p-3 rounded-full text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5" />
                </svg>
              </div>
              <div>
                <h3 className="font-semibold text-[#1d1d1f]">Manage Bookings</h3>
                <p className="text-sm text-gray-500">Monitor all platform bookings</p>
              </div>
            </div>
            <div className="text-[#d2d2d7] group-hover:text-[#1d1d1f] transition-colors">›</div>
          </div>

          {/* Manage Services (Action) */}
          <div className="card-apple md:col-span-2 flex items-center justify-between cursor-pointer group" onClick={() => navigate("/admin/services")}>
            <div className="flex items-center gap-4">
              <div className="bg-[#34c759] p-3 rounded-full text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M9.568 3H5.25A2.25 2.25 0 0 0 3 5.25v4.318c0 .597.237 1.17.659 1.591l9.581 9.581a2.25 2.25 0 0 0 3.182 0l4.318-4.318a2.25 2.25 0 0 0 0-3.182L10.58 3.659a2.25 2.25 0 0 0-1.591-.659ZM6 6h1.5v1.5H6V6Z" />
                </svg>
              </div>
              <div>
                <h3 className="font-semibold text-[#1d1d1f]">Manage Services</h3>
                <p className="text-sm text-gray-500">Configure core offerings & pricing</p>
              </div>
            </div>
            <div className="text-[#d2d2d7] group-hover:text-[#1d1d1f] transition-colors">›</div>
          </div>



          {/* Edit Profile Card */}
          <div className="card-apple flex flex-col justify-center items-center text-center cursor-pointer group hover:bg-[#f5f5f7]" onClick={() => navigate("/admin-profile")}>
            <div className="mb-2 text-[#86868b] group-hover:text-[#0071e3]">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8">
                <path strokeLinecap="round" strokeLinejoin="round" d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75c-2.2 0-4.17.947-5.523 2.457M12 12.75a4.5 4.5 0 1 0 0-9 4.5 4.5 0 0 0 0 9Z" />
              </svg>
            </div>
            <span className="text-sm font-medium text-[#1d1d1f]">Profile</span>
          </div>

        </div>
      </div>
    </div>
  );
}
