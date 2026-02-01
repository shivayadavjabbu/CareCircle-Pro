import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getParentProfile, getCaregiverProfile, getAdminStats } from "../api/profileApi";
import { getMyBookings, getActiveCaregiverBookings } from "../api/bookingApi";

export default function MainDashboard() {
    const navigate = useNavigate();
    const role = localStorage.getItem("role");
    const [profile, setProfile] = useState(null);
    const [bookings, setBookings] = useState([]);
    const [stats, setStats] = useState({ active: 0, total: 0 });

    useEffect(() => {
        if (role === "ROLE_CARETAKER") {
            navigate("/caretaker-dashboard");
            return;
        }
        if (role === "ROLE_PARENT") {
            navigate("/parent-dashboard");
            return;
        }
        if (role === "ROLE_ADMIN") {
            navigate("/admin-dashboard");
            return;
        }

        const fetchData = async () => {
            try {
                if (role === "ROLE_PARENT") {
                    setProfile(await getParentProfile());
                    const b = await getMyBookings();
                    setBookings(Array.isArray(b) ? b : []);
                } else if (role === "ROLE_ADMIN") {
                    setStats(await getAdminStats() || { active: 0, total: 0 });
                }
            } catch (e) { console.error(e); }
        };
        fetchData();
    }, [role, navigate]);

    return (
        <div className="min-h-screen pt-32 pb-20 px-6 bg-slate-50">
            <div className="max-w-7xl mx-auto">
                <div className="mb-12">
                    <h1 className="text-4xl font-black text-slate-900 mb-2">Hello, {profile?.fullName || "CareCircle Admin"}</h1>
                    <p className="text-lg font-medium text-slate-500 tracking-tight">Here's what's happening today</p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-12">
                    <div className="p-8 bg-indigo-600 rounded-[2.5rem] shadow-xl shadow-indigo-100 text-white">
                        <h3 className="text-xs font-black uppercase tracking-widest opacity-80 mb-2">Current Status</h3>
                        <p className="text-3xl font-black">Online</p>
                    </div>
                    <div className="p-8 bg-white rounded-[2.5rem] shadow-xl border border-slate-100">
                        <h3 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-2">My Bookings</h3>
                        <p className="text-3xl font-black text-slate-900">{bookings.length}</p>
                    </div>
                    <div className="p-8 bg-white rounded-[2.5rem] shadow-xl border border-slate-100">
                        <h3 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-2">New Messages</h3>
                        <p className="text-3xl font-black text-slate-900">0</p>
                    </div>
                    <div className="p-8 bg-white rounded-[2.5rem] shadow-xl border border-slate-100">
                        <h3 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-2">Notifications</h3>
                        <p className="text-3xl font-black text-slate-900">0</p>
                    </div>
                </div>

                <div className="p-10 bg-white rounded-[3rem] shadow-2xl border border-slate-50">
                    <h2 className="text-2xl font-black text-slate-900 mb-8">Recent Bookings</h2>
                    {bookings.length > 0 ? (
                        <div className="space-y-4">
                            {bookings.slice(0, 5).map((b, i) => (
                                <div key={i} className="p-6 bg-slate-50 rounded-2xl flex justify-between items-center border border-slate-100 hover:bg-white hover:shadow-lg transition-all cursor-pointer">
                                    <div>
                                        <p className="font-black text-slate-900 text-lg">#{b.id || i + 1}</p>
                                        <p className="text-sm font-bold text-slate-400 uppercase">{b.date} • {b.time}</p>
                                    </div>
                                    <span className={`px-5 py-2 rounded-full text-xs font-black uppercase ${b.status === 'ACTIVE' ? 'bg-green-100 text-green-600' : 'bg-indigo-100 text-indigo-600'}`}>{b.status || 'Scheduled'}</span>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="py-20 text-center">
                            <div className="text-5xl mb-6">📅</div>
                            <p className="text-slate-400 font-bold text-lg">No active bookings found.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
