import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getParentProfile, getChildren } from "../api/parentApi";
import { getBookings } from "../api/bookingApi";

export default function ParentDashboard() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [childrenCount, setChildrenCount] = useState(0);
  const [bookings, setBookings] = useState([]);
  const [activeTab, setActiveTab] = useState('ACCEPTED'); // Default to Accepted for parents usually
  const [error, setError] = useState("");

  const filteredBookings = bookings.filter(b => b.status === activeTab);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [profileData, childrenData] = await Promise.all([
          getParentProfile(),
          getChildren()
        ]);
        setProfile(profileData);
        setChildrenCount(childrenData.length || 0);

        // Fetch bookings for this parent
        if (profileData && profileData.userId) {
          const myBookings = await getBookings(null, profileData.userId, null);
          setBookings(myBookings || []);
        } else if (profileData && profileData.id) {
          const myBookings = await getBookings(null, profileData.id, null);
          setBookings(myBookings || []);
        }

      } catch (err) {
        console.error("Failed to fetch dashboard data", err);
        setError("Could not load dashboard data.");
      }
    };
    fetchDashboardData();
  }, [navigate]);

  const handleCancelBooking = async (bookingId) => {
    if (!window.confirm("Are you sure you want to cancel this booking?")) return;
    try {
      // Assuming updateBookingStatus handles CANCELLED logic
      // Since we don't have updateBookingStatus imported, we need to import it or use it from props if passed
      // Wait, I need to import updateBookingStatus from bookingApi
      const { updateBookingStatus } = await import("../api/bookingApi");
      await updateBookingStatus(bookingId, "CANCELLED");

      setBookings(prev => prev.map(b =>
        b.id === bookingId ? { ...b, status: "CANCELLED" } : b
      ));
      alert("Booking Cancelled.");
    } catch (err) {
      alert("Failed to cancel: " + err.message);
    }
  };

  return (
    <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7] font-sans">
      <div className="max-w-[1200px] mx-auto">

        {/* Header */}
        <div className="mb-10 flex justify-between items-end">
          <div>
            <h1 className="text-[40px] font-bold text-[#1d1d1f] tracking-tight">Hi, {profile?.fullName || "Parent"}</h1>
            <p className="text-[#86868b] text-[19px] mt-1 font-medium">Parent Dashboard</p>
          </div>
          <div className="text-right">
            <button className="text-sm font-bold text-[#0071e3] px-4 py-2 hover:bg-blue-50 rounded-full transition-colors" onClick={() => navigate("/parent-profile")}>Edit Profile</button>
          </div>
        </div>

        {error && (
          <div className="mb-8 p-4 bg-[#fff2f2] border border-[#ff3b30]/20 rounded-xl text-[#ff3b30] flex items-center gap-3">
            <span>⚠️</span> {error}
          </div>
        )}

        {/* Bento Grid Layout */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 auto-rows-min">

          {/* Quick Stats - Children */}
          <div className="card-apple flex flex-col justify-between group cursor-pointer hover:shadow-apple-hover transition-all p-6 min-h-[160px]" onClick={() => navigate("/baby-details")}>
            <div className="flex justify-between items-start">
              <div className="bg-[#0071e3] p-2 rounded-lg text-white shadow-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
                </svg>
              </div>
              <span className="text-[#1d1d1f] font-bold text-3xl">{childrenCount}</span>
            </div>
            <div>
              <h3 className="font-bold text-[#1d1d1f] text-lg">My Family</h3>
              <p className="text-xs text-[#86868b] font-medium">Manage profiles</p>
            </div>
          </div>

          {/* Quick Stats - Messages */}
          <div className="card-apple flex flex-col justify-between group cursor-pointer hover:shadow-apple-hover transition-all p-6 min-h-[160px]" onClick={() => navigate("/chat")}>
            <div className="flex justify-between items-start">
              <div className="bg-[#34c759] p-2 rounded-lg text-white shadow-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-6 h-6">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 0 1 .865-.501 48.172 48.172 0 0 0 3.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
                </svg>
              </div>
            </div>
            <div>
              <h3 className="font-bold text-[#1d1d1f] text-lg">Messages</h3>
              <p className="text-xs text-[#86868b] font-medium">Inbox</p>
            </div>
          </div>

          {/* Find Nanny - Tall Card */}
          <div className="card-apple md:col-span-2 flex flex-col justify-center items-start p-8 bg-gradient-to-r from-[#0071e3] to-[#42a5f5] text-white cursor-pointer hover:shadow-lg transition-all min-h-[160px]" onClick={() => navigate("/search-caregivers")}>
            <div className="flex items-center gap-4">
              <div className="p-3 bg-white/20 rounded-full backdrop-blur-sm">
                <svg className="w-8 h-8 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607z" /></svg>
              </div>
              <div>
                <h3 className="text-2xl font-bold">Find Caregivers</h3>
                <p className="text-white/90 text-sm font-medium mt-1">Browse verified professionals in your area.</p>
              </div>
            </div>
          </div>

          {/* Unified Bookings Card (Wide) */}
          <div className="card-apple md:col-span-4 p-8 flex flex-col bg-white overflow-hidden min-h-[400px]">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
              <h3 className="text-2xl font-bold text-[#1d1d1f]">My Bookings</h3>

              {/* Tabs */}
              <div className="flex gap-2 p-1 bg-gray-100 rounded-lg">
                {['REQUESTED', 'ACCEPTED', 'COMPLETED', 'CANCELLED'].map(status => (
                  <button
                    key={status}
                    onClick={() => setActiveTab(status)}
                    className={`px-4 py-1.5 rounded-md text-sm font-bold transition-all ${activeTab === status
                      ? 'bg-white text-blue-600 shadow-sm'
                      : 'text-gray-500 hover:text-gray-700'
                      }`}
                  >
                    {status.charAt(0) + status.slice(1).toLowerCase()}
                  </button>
                ))}
              </div>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {filteredBookings.length === 0 ? (
                <div className="text-center py-10 text-[#86868b] flex flex-col items-center">
                  <p className="mb-4 font-medium">No {activeTab.toLowerCase()} bookings.</p>
                  {activeTab !== 'CANCELLED' && (
                    <button onClick={() => navigate("/search-caregivers")} className="btn-apple-primary px-6">Book a Caregiver</button>
                  )}
                </div>
              ) : (
                filteredBookings.map(b => (
                  <div key={b.id} className={`border rounded-2xl p-5 transition-colors ${b.status === 'REQUESTED' ? 'border-gray-200 bg-gray-50/50' :
                    b.status === 'ACCEPTED' ? 'border-green-200 bg-green-50/30' :
                      'border-gray-100 bg-white opacity-75'
                    }`}>
                    <div className="flex flex-col md:flex-row justify-between gap-4">

                      {/* Left Info */}
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <span className="font-bold text-lg text-[#1d1d1f]">{b.caregiverName || "Caregiver"}</span>
                          <span className={`text-xs px-2 py-0.5 rounded font-bold ${b.status === 'REQUESTED' ? 'bg-blue-100 text-blue-700' :
                            b.status === 'ACCEPTED' ? 'bg-green-100 text-green-700' :
                              'bg-gray-100 text-gray-600'
                            }`}>{b.bookingType}</span>
                        </div>

                        <div className="text-sm text-gray-600 font-medium">
                          {b.bookingType === 'HOURLY'
                            ? `${b.startDate} • ${b.startTime?.substring(0, 5)} - ${b.endTime?.substring(0, 5)}`
                            : `${b.startDate} to ${b.endDate}`
                          }
                        </div>
                        {/* Service Type maybe? */}
                      </div>

                      {/* Right Actions */}
                      <div className="flex flex-col items-end gap-3 min-w-[150px]">
                        <span className="text-2xl font-bold text-[#1d1d1f]">${b.finalPrice}</span>

                        <div className="flex gap-2 justify-end w-full">
                          {b.status === 'ACCEPTED' && (
                            <>
                              <button
                                onClick={() => navigate(`/chat/${b.caregiverId}`, { state: { bookingId: b.id, partnerId: b.caregiverId } })}
                                className="p-2 rounded-full bg-blue-100 text-blue-600 hover:bg-blue-200 hover:scale-105 transition-transform"
                                title="Chat with Caregiver"
                              >
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
                                  <path strokeLinecap="round" strokeLinejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 0 1 .865-.501 48.172 48.172 0 0 0 3.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
                                </svg>
                              </button>
                              <button
                                onClick={() => handleCancelBooking(b.id)}
                                className="px-4 py-2 rounded-lg border border-red-200 text-red-500 font-bold hover:bg-red-50 text-xs transition-colors"
                              >
                                Cancel
                              </button>
                            </>
                          )}

                          {b.status === 'REQUESTED' && (
                            <button
                              onClick={() => handleCancelBooking(b.id)}
                              className="px-4 py-2 rounded-lg border border-gray-300 text-gray-500 font-bold hover:bg-gray-100 text-xs transition-colors"
                            >
                              Cancel Request
                            </button>
                          )}

                          {(b.status === 'CANCELLED' || b.status === 'REJECTED' || b.status === 'COMPLETED') && (
                            <span className="text-xs font-bold text-gray-400 uppercase tracking-wide">{b.status}</span>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}
