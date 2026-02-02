import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getCaregiverProfile, getServices, getCertifications } from "../api/caregiverApi";
import { getMyAvailability } from "../api/availabilityApi";
import { getActiveServices } from "../api/matchingApi";
import { getBookings, updateBookingStatus } from "../api/bookingApi";

export default function CaregiverDashboard() {
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [myServices, setMyServices] = useState([]);
    const [myCertifications, setMyCertifications] = useState([]);
    const [myAvailability, setMyAvailability] = useState([]);

    // Unified Bookings Logic
    const [allBookings, setAllBookings] = useState([]);
    const [activeTab, setActiveTab] = useState('REQUESTED');
    const [loading, setLoading] = useState(true);

    const filteredBookings = allBookings.filter(b => b.status === activeTab);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                // Profile & User Data
                const p = await getCaregiverProfile();
                setProfile(p);

                const [ms, mc, ma] = await Promise.all([
                    getServices(),
                    getCertifications(),
                    getMyAvailability()
                ]);
                setMyServices(ms || []);
                setMyCertifications(mc || []);
                setMyAvailability(ma || []);

                if (p && p.userId) {
                    // Fetch ALL bookings for this caregiver (or at least the active ones)
                    // We can pass null status to get everything, or list specific statuses.
                    // Let's fetch the main ones we care about + history if needed.
                    // For now, fetching everything lets client-side filtering work smoothly.
                    const bookings = await getBookings(p.userId, null, null);
                    setAllBookings(bookings || []);
                } else {
                    console.error("Profile loaded but userId is missing:", p);
                }

            } catch (err) {
                console.warn("Error fetching dashboard data:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const handleBookingAction = async (bookingId, newStatus) => {
        try {
            const updatedBooking = await updateBookingStatus(bookingId, newStatus);

            // Update local state
            setAllBookings(prev => prev.map(b =>
                b.id === bookingId ? { ...b, status: newStatus } : b
            ));

            alert(`Booking ${newStatus.toLowerCase()}!`);

            // Optional: Auto-switch tab if accepting? 
            if (newStatus === 'ACCEPTED') {
                // Maybe stay on REQUESTED so they can process others? 
                // Or switch to ACCEPTED to see it? User preference.
                // Staying is simpler.
            }

        } catch (err) {
            alert("Failed to update status: " + err.message);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("userEmail");
        navigate("/");
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-[#0071e3]"></div>
            </div>
        );
    }

    return (
        <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7] font-sans">

            {/* Header */}
            <div className="max-w-[1200px] mx-auto mb-10 flex flex-col md:flex-row md:items-end justify-between gap-6">
                <div>
                    <h1 className="text-[40px] font-bold tracking-tight text-[#1d1d1f]">
                        Hi, {profile?.fullName || "Caregiver"}
                    </h1>
                    <p className="text-[#86868b] text-[19px] mt-1 font-medium">Caregiver Dashboard</p>
                </div>
                <div className="flex gap-3">
                    <button onClick={() => navigate("/nanny-profile")} className="btn-apple-secondary px-6">Edit Profile</button>
                    <button onClick={handleLogout} className="px-6 py-2.5 rounded-full border border-red-200 text-red-500 font-bold hover:bg-red-50 transition-all text-[15px]">Logout</button>
                </div>
            </div>

            {/* Grid */}
            <div className="max-w-[1200px] mx-auto grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 auto-rows-[200px]">

                {/* Status Card (Small) */}
                <div className="card-apple flex flex-col justify-center items-center text-center p-6">
                    <div className={`w-3 h-3 rounded-full mb-2 ${profile?.verificationStatus === 'VERIFIED' ? 'bg-green-500' : 'bg-orange-500'}`}></div>
                    <span className="text-xs font-bold uppercase text-[#86868b]">Status</span>
                    <h3 className="text-xl font-bold text-[#1d1d1f] mt-1">{profile?.verificationStatus}</h3>
                </div>

                {/* Experience (Small) */}
                <div className="card-apple flex flex-col justify-center items-center text-center p-6">
                    <span className="text-xs font-bold uppercase text-[#86868b]">Experience</span>
                    <div className="text-3xl font-bold text-[#1d1d1f] mt-1">{profile?.experienceYears || 0}</div>
                    <span className="text-xs text-[#86868b] mt-1">Years</span>
                </div>

                {/* Availability (Simplfied) */}
                <div onClick={() => navigate("/caregiver-availability")} className="card-apple p-6 flex flex-col justify-between cursor-pointer hover:shadow-apple-hover">
                    <div>
                        <h3 className="text-lg font-bold text-[#1d1d1f]">Availability</h3>
                        <p className="text-xs text-[#86868b] mt-1">Manage slots</p>
                    </div>
                    <div className="mt-2">
                        <div className="text-3xl font-bold text-blue-600">{myAvailability.length}</div>
                        <div className="text-xs font-medium text-blue-600">Active Slots</div>
                    </div>
                </div>

                {/* Credentials (Simplified) */}
                <div onClick={() => navigate("/caregiver-services")} className="card-apple p-6 flex flex-col justify-between cursor-pointer hover:shadow-apple-hover">
                    <div>
                        <h3 className="text-lg font-bold text-[#1d1d1f]">Services</h3>
                        <p className="text-xs text-[#86868b] mt-1">Skills & Certs</p>
                    </div>
                    <div className="mt-2 flex gap-4">
                        <div>
                            <div className="text-2xl font-bold text-purple-600">{myServices.length}</div>
                            <div className="text-[10px] uppercase font-bold text-purple-600">Svcs</div>
                        </div>
                        <div>
                            <div className="text-2xl font-bold text-blue-600">{myCertifications.length}</div>
                            <div className="text-[10px] uppercase font-bold text-blue-600">Certs</div>
                        </div>
                    </div>
                </div>

                {/* Unified Bookings Card */}
                <div className="card-apple md:col-span-2 lg:col-span-4 row-span-2 p-8 flex flex-col bg-white overflow-hidden mt-6">
                    <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
                        <h3 className="text-2xl font-bold text-[#1d1d1f]">Bookings</h3>

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
                            <div className="text-center py-10 text-[#86868b]">
                                <p>No {activeTab.toLowerCase()} bookings.</p>
                            </div>
                        ) : (
                            filteredBookings.map(b => (
                                <div key={b.id} className={`border rounded-2xl p-5 transition-colors ${b.status === 'REQUESTED' ? 'border-gray-200 bg-gray-50/50 hover:border-blue-400' :
                                    b.status === 'ACCEPTED' ? 'border-green-200 bg-green-50/30' :
                                        'border-gray-100 bg-white opacity-75'
                                    }`}>
                                    <div className="flex flex-col md:flex-row justify-between gap-4">
                                        <div className="flex-1">
                                            <div className="flex items-center gap-2 mb-2">
                                                <span className="font-bold text-lg text-[#1d1d1f]">{b.parentName || "Parent"}</span>
                                                <span className={`text-xs px-2 py-0.5 rounded font-bold ${b.status === 'REQUESTED' ? 'bg-blue-100 text-blue-700' :
                                                    b.status === 'ACCEPTED' ? 'bg-green-100 text-green-700' :
                                                        'bg-gray-100 text-gray-600'
                                                    }`}>{b.bookingType}</span>
                                            </div>
                                            <div className="text-sm text-gray-600">
                                                {b.bookingType === 'HOURLY'
                                                    ? `${b.startDate} • ${b.startTime?.substring(0, 5)} - ${b.endTime?.substring(0, 5)}`
                                                    : `${b.startDate} to ${b.endDate}`
                                                }
                                            </div>
                                            {b.children && b.children.length > 0 && (
                                                <div className="mt-2 text-sm text-gray-600 font-medium">
                                                    Care for: {b.children.map(c => c.childName).join(", ")}
                                                </div>
                                            )}
                                        </div>

                                        <div className="flex flex-col justify-between items-end gap-3 min-w-[150px]">
                                            <span className="text-2xl font-bold text-[#1d1d1f]">${b.finalPrice}</span>

                                            <div className="flex gap-2 w-full justify-end">
                                                {b.status === 'REQUESTED' && (
                                                    <>
                                                        <button
                                                            onClick={() => handleBookingAction(b.id, "REJECTED")}
                                                            className="px-4 py-2 rounded-lg border border-gray-300 font-bold text-gray-500 hover:bg-gray-100 text-sm"
                                                        >
                                                            Decline
                                                        </button>
                                                        <button
                                                            onClick={() => handleBookingAction(b.id, "ACCEPTED")}
                                                            className="px-4 py-2 rounded-lg bg-[#0071e3] text-white font-bold hover:bg-[#0077ED] text-sm shadow-md"
                                                        >
                                                            Accept
                                                        </button>
                                                    </>
                                                )}

                                                {b.status === 'ACCEPTED' && (
                                                    <>
                                                        <button
                                                            onClick={() => navigate(`/chat/${b.parentId}`, { state: { bookingId: b.id, partnerId: b.parentId } })}
                                                            className="p-2 rounded-full bg-blue-100 text-blue-600 hover:bg-blue-200"
                                                            title="Chat"
                                                        >
                                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
                                                                <path strokeLinecap="round" strokeLinejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 0 1 .865-.501 48.172 48.172 0 0 0 3.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
                                                            </svg>
                                                        </button>
                                                        <button
                                                            onClick={() => handleBookingAction(b.id, "CANCELLED")}
                                                            className="px-4 py-2 rounded-lg border border-red-200 text-red-500 font-bold hover:bg-red-50 text-xs"
                                                        >
                                                            Cancel
                                                        </button>
                                                    </>
                                                )}

                                                {(b.status === 'CANCELLED' || b.status === 'REJECTED' || b.status === 'COMPLETED') && (
                                                    <span className="text-xs font-bold text-gray-400 uppercase">{b.status}</span>
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
    );
}
