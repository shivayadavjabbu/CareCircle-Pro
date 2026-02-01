import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getCaregiverProfile, getServices, getCertifications } from "../api/caregiverApi";
import { getActiveServices } from "../api/matchingApi";
import { getActiveCities } from "../api/cityApi";

export default function CaregiverDashboard() {
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [myServices, setMyServices] = useState([]);
    const [myCertifications, setMyCertifications] = useState([]);
    const [globalServices, setGlobalServices] = useState([]);
    const [cities, setCities] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);

            // 1. Fetch Profile (Crucial for greeting and experience)
            try {
                const p = await getCaregiverProfile();
                setProfile(p);
            } catch (err) {
                console.warn("Profile not found or fetch error:", err.message);
                // We keep profile as null, but don't break the whole dashboard
            }

            // 2. Fetch My Services & Certifications
            try {
                const [ms, mc] = await Promise.all([
                    getServices(),
                    getCertifications()
                ]);
                setMyServices(ms || []);
                setMyCertifications(mc || []);
            } catch (err) {
                console.error("Error fetching my services/certs:", err);
            }

            // 3. Fetch Global Meta (Cities & Services)
            try {
                const [gs, c] = await Promise.all([
                    getActiveServices(),
                    getActiveCities()
                ]);
                setGlobalServices(gs || []);
                setCities(c || []);
            } catch (err) {
                console.error("Error fetching global metadata:", err);
            }

            setLoading(false);
        };
        fetchData();
    }, []);

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

            {/* Header Section */}
            <div className="max-w-[1200px] mx-auto mb-10 flex flex-col md:flex-row md:items-end justify-between gap-6 animate-fade-in-up">
                <div>
                    <h1 className="text-[40px] font-bold tracking-tight text-[#1d1d1f]">
                        Hi, {profile?.fullName?.split(" ")[0] || "Caregiver"}
                    </h1>
                    <p className="text-[#86868b] text-[19px] mt-1 font-medium">Your professional care hub.</p>
                </div>
                <div className="flex gap-3">
                    <button
                        onClick={() => navigate("/nanny-profile")}
                        className="btn-apple-secondary px-6"
                    >
                        Edit Profile
                    </button>
                    <button
                        onClick={handleLogout}
                        className="px-6 py-2.5 rounded-full border border-red-200 text-red-500 font-bold hover:bg-red-50 transition-all text-[15px]"
                    >
                        Logout
                    </button>
                </div>
            </div>

            {/* Bento Grid Layout */}
            <div className="max-w-[1200px] mx-auto grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 auto-rows-[200px] animate-fade-in-up" style={{ animationDelay: '0.1s' }}>

                {/* Verification Status - Med Card */}
                <div className="card-apple md:col-span-2 flex flex-col justify-between p-8 relative overflow-hidden group">
                    <div className="z-10">
                        <div className="flex items-center gap-2 mb-3">
                            <div className={`w-3 h-3 rounded-full ${profile?.verificationStatus === 'VERIFIED' ? 'bg-green-500 shadow-[0_0_12px_rgba(34,197,94,0.4)]' : 'bg-orange-500 shadow-[0_0_12px_rgba(249,115,22,0.4)]'}`}></div>
                            <span className="text-xs font-extrabold uppercase tracking-widest text-[#86868b]">Identity Status</span>
                        </div>
                        <h3 className="text-3xl font-bold text-[#1d1d1f] tracking-tight">{profile?.verificationStatus || "PENDING"}</h3>
                        <p className="text-sm text-[#86868b] mt-2 font-medium max-w-[200px]">
                            {profile?.verificationStatus === 'VERIFIED'
                                ? "Your profile is live and searchable by parents."
                                : "Documents under review. We'll notify you soon."}
                        </p>
                    </div>
                    <div className="absolute right-[-10px] bottom-[-20px] opacity-[0.03] group-hover:opacity-[0.06] transition-opacity duration-700">
                        <svg className="w-48 h-48" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z" /></svg>
                    </div>
                </div>

                {/* Experience Stats */}
                <div className="card-apple flex flex-col justify-center items-center text-center p-8 group">
                    <span className="text-xs font-extrabold uppercase tracking-widest text-[#86868b] mb-2">Experience</span>
                    <div className="text-4xl font-bold text-[#1d1d1f] group-hover:scale-110 transition-transform duration-500">{profile?.experienceYears || 0}</div>
                    <span className="text-sm font-bold text-[#1d1d1f]">Years</span>
                </div>

                {/* Rating Card */}
                <div className="card-apple flex flex-col justify-center items-center text-center p-8 bg-gradient-to-br from-amber-50 to-orange-50/50 border-orange-100/50">
                    <span className="text-xs font-extrabold uppercase tracking-widest text-orange-400 mb-2">Member Rating</span>
                    <div className="flex items-center gap-1">
                        <span className="text-4xl font-bold text-[#1d1d1f]">{profile?.rating || "4.9"}</span>
                        <span className="text-orange-400 text-2xl">★</span>
                    </div>
                </div>

                {/* Setup Profile Prompt (Visible if no profile) */}
                {!profile && (
                    <div className="card-apple md:col-span-2 flex items-center justify-between p-8 bg-[#0071e3] text-white cursor-pointer group" onClick={() => navigate("/nanny-profile")}>
                        <div className="z-10">
                            <h3 className="text-2xl font-bold mb-1">Complete Your Profile</h3>
                            <p className="text-white/70 text-sm font-medium">Families can only find you once your profile is set up.</p>
                        </div>
                        <div className="bg-white/20 p-4 rounded-2xl group-hover:scale-110 transition-transform">
                            <span className="text-2xl font-bold">→</span>
                        </div>
                    </div>
                )}

                {/* Services Bento - Tall Card */}
                <div
                    onClick={() => navigate("/caregiver-services")}
                    className="card-apple md:row-span-2 p-8 flex flex-col justify-between bg-white cursor-pointer hover:shadow-apple-hover group"
                >
                    <div>
                        <div className="w-12 h-12 rounded-2xl bg-purple-100 text-purple-600 flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                            </svg>
                        </div>
                        <h3 className="text-2xl font-bold text-[#1d1d1f] mb-3 leading-tight">Professional Services</h3>
                        <div className="space-y-3">
                            {myServices.slice(0, 3).map(s => (
                                <div key={s.id} className="flex flex-col">
                                    <span className="text-sm font-bold text-[#1d1d1f]">{globalServices.find(gs => gs.id === s.serviceId)?.serviceName || "Service"}</span>
                                    <span className="text-[10px] text-[#86868b] font-medium uppercase tracking-tighter">{s.city} • ${s.extraPrice}/hr</span>
                                </div>
                            ))}
                            {myServices.length > 3 && <span className="text-xs text-[#0071e3] font-bold">+{myServices.length - 3} more</span>}
                            {myServices.length === 0 && <p className="text-xs text-[#86868b]">No services registered yet.</p>}
                        </div>
                    </div>
                    <div className="pt-6 border-t border-gray-50 flex items-center justify-between text-[#0071e3] font-bold text-sm">
                        <span>Manage All</span>
                        <span className="group-hover:translate-x-1 transition-transform">→</span>
                    </div>
                </div>

                {/* Certifications Bento - Wide Card */}
                <div
                    onClick={() => navigate("/caregiver-services")}
                    className="card-apple md:col-span-2 p-8 flex flex-col justify-between bg-[#0071e3] text-white cursor-pointer group"
                >
                    <div className="flex justify-between items-start">
                        <div>
                            <h3 className="text-2xl font-bold mb-1">Certifications</h3>
                            <p className="text-white/70 text-sm font-medium">Verify your skills to attract more families.</p>
                        </div>
                        <div className="bg-white/20 p-3 rounded-2xl backdrop-blur-md">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                            </svg>
                        </div>
                    </div>

                    <div className="flex gap-4 mt-6 overflow-hidden">
                        {myCertifications.length > 0 ? (
                            myCertifications.slice(0, 2).map(c => (
                                <div key={c.id} className="bg-white/10 backdrop-blur-md p-3 rounded-xl border border-white/20 flex-1 min-w-0">
                                    <div className="font-bold truncate text-sm">{c.name}</div>
                                    <div className="text-[10px] opacity-70 truncate uppercase font-bold tracking-tighter">
                                        {c.verificationStatus} {c.serviceName && `• ${c.serviceName}`}
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className="text-white/50 text-xs italic">Zero credentials uploaded.</div>
                        )}
                    </div>
                </div>

                {/* Earnings/Quick Actions */}
                <div className="card-apple flex flex-col justify-center items-center text-center p-8 bg-white border-blue-50">
                    <span className="text-xs font-extrabold uppercase tracking-widest text-[#0071e3] mb-2">New Bookings</span>
                    <div className="text-4xl font-bold text-[#1d1d1f]">0</div>
                    <button className="mt-4 text-[10px] font-bold text-[#0071e3] uppercase tracking-widest hover:underline">View Calendar</button>
                </div>

            </div>
        </div>
    );
}
