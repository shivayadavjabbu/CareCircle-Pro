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
        setError("Could not load dashboard data.");
      }
    };
    fetchDashboardData();
  }, [navigate]);

  return (
    <div className="min-h-screen bg-[#f5f5f7] pt-[80px] pb-12 px-6">
      <div className="max-w-[1024px] mx-auto">

        {/* Header */}
        <div className="mb-10 flex justify-between items-end">
          <div>
            <h1 className="text-[40px] font-semibold text-[#1d1d1f] tracking-tight">Dashboard</h1>
            <p className="text-[#86868b] text-[17px]">Welcome back, {profile?.fullName || "Parent"}.</p>
          </div>
          <div className="text-right">
            <span className="text-sm font-medium text-[#0071e3] cursor-pointer hover:underline">Edit Profile</span>
          </div>
        </div>

        {error && (
          <div className="mb-8 p-4 bg-[#fff2f2] border border-[#ff3b30]/20 rounded-xl text-[#ff3b30] flex items-center gap-3">
            <span>⚠️</span> {error}
          </div>
        )}

        {/* Bento Grid Layout */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 auto-rows-[180px]">

          {/* Quick Stats - Large Card */}
          <div className="card-apple md:col-span-2 row-span-1 flex flex-col justify-center items-start relative overflow-hidden group">
            <div className="z-10">
              <h3 className="text-[#86868b] font-medium text-sm uppercase tracking-wider mb-1">Total Bookings</h3>
              <div className="text-[48px] font-semibold text-[#1d1d1f]">0</div>
            </div>
            <div className="absolute right-6 top-6 opacity-10 group-hover:opacity-20 transition-opacity">
              <svg className="w-32 h-32" fill="currentColor" viewBox="0 0 24 24"><path d="M6.75 3v2.25h10.5V3A.75.75 0 0 1 18 3.75v2.25h1.5a2.25 2.25 0 0 1 2.25 2.25v12a2.25 2.25 0 0 1-2.25 2.25H4.5A2.25 2.25 0 0 1 2.25 18v-12a2.25 2.25 0 0 1 2.25-2.25H6v-2.25A.75.75 0 0 1 6.75 3Z" /></svg>
            </div>
          </div>

          {/* Children Card */}
          <div className="card-apple flex flex-col justify-between group cursor-pointer" onClick={() => navigate("/baby-details")}>
            <div className="flex justify-between items-start">
              <div className="bg-[#0071e3] p-2 rounded-lg text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
                </svg>
              </div>
              <span className="text-[#1d1d1f] font-bold text-2xl">{childrenCount}</span>
            </div>
            <div>
              <h3 className="font-semibold text-[#1d1d1f]">My Family</h3>
              <p className="text-xs text-[#86868b]">Manage profiles</p>
            </div>
          </div>

          {/* Messages */}
          <div className="card-apple flex flex-col justify-between group cursor-pointer" onClick={() => navigate("/messages")}>
            <div className="flex justify-between items-start">
              <div className="bg-[#34c759] p-2 rounded-lg text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 0 1 .865-.501 48.172 48.172 0 0 0 3.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
                </svg>
              </div>
            </div>
            <div>
              <h3 className="font-semibold text-[#1d1d1f]">Messages</h3>
              <p className="text-xs text-[#86868b]">0 New</p>
            </div>
          </div>

          {/* Find Nanny - Tall Card */}
          <div className="card-apple md:col-start-1 md:row-span-2 flex flex-col justify-end p-8 bg-gradient-to-br from-[#0071e3] to-[#42a5f5] text-white cursor-pointer hover:shadow-apple-hover" onClick={() => navigate("/find-nanny")}>
            <div className="mb-4">
              <svg className="w-12 h-12 text-white/80" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607z" /></svg>
            </div>
            <h3 className="text-3xl font-bold mb-2">Find Care</h3>
            <p className="text-white/80 text-sm mb-6">Browse verified professionals in your area.</p>
            <button className="bg-white text-[#0071e3] px-6 py-3 rounded-full font-semibold text-sm w-fit hover:bg-white/90 transition-colors">Search Now</button>
          </div>

          {/* Favorites */}
          <div className="card-apple md:col-span-2 flex items-center justify-between cursor-pointer group" onClick={() => navigate("/favorites")}>
            <div className="flex items-center gap-4">
              <div className="bg-[#ff2d55] p-3 rounded-full text-white">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6"><path strokeLinecap="round" strokeLinejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" /></svg>
              </div>
              <div>
                <h3 className="font-semibold text-[#1d1d1f]">Saved Caregivers</h3>
                <p className="text-sm text-[#86868b]">View your shortlist</p>
              </div>
            </div>
            <div className="text-[#d2d2d7] group-hover:text-[#1d1d1f] transition-colors">›</div>
          </div>

          {/* Quick Link: Payment Methods */}
          <div className="card-apple flex flex-col justify-center items-center text-center cursor-pointer group hover:bg-[#f5f5f7]">
            <div className="mb-2 text-[#86868b] group-hover:text-[#0071e3]">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8"><path strokeLinecap="round" strokeLinejoin="round" d="M2.25 8.25h19.5M2.25 9h19.5m-16.5 5.25h6m-6 2.25h3m-3.75 3h15a2.25 2.25 0 002.25-2.25V6.75A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25v10.5A2.25 2.25 0 004.5 19.5z" /></svg>
            </div>
            <span className="text-sm font-medium text-[#1d1d1f]">Payment</span>
          </div>

        </div>
      </div>
    </div>
  );
}
