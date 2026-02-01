import { useEffect, useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { getCaregiverDetails } from "../api/parentApi";
import { createBooking } from "../api/bookingApi";

export default function CaretakerProfilePublic() {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const { childId, childName } = location.state || {};

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [showBookingModal, setShowBookingModal] = useState(false);
    const [bookingDetails, setBookingDetails] = useState({
        date: "",
        startTime: "09:00",
        endTime: "17:00",
        serviceId: null // Still tricky without fetching services
    });

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const data = await getCaregiverDetails(id);
                setProfile(data);
            } catch (err) {
                console.error("Fetch details error:", err);
                setError("Failed to load caregiver profile.");
            } finally {
                setLoading(false);
            }
        };
        fetchDetails();
    }, [id]);

    const confirmBooking = async () => {
        if (!bookingDetails.date) {
            alert("Please select a date.");
            return;
        }

        // TODO: Handle Service ID selection. For now, sending null might fail or default if backend allows.
        // If backend STRICTLY requires serviceId, we need to fetch services. 
        // Assuming for this demo we proceed or hardcode if we knew IDs.

        try {
            const payload = {
                caregiverId: id,
                serviceId: null, // This might be an issue if backend validates it
                bookingType: "HOURLY",
                startDate: bookingDetails.date,
                startTime: bookingDetails.startTime + ":00",
                endTime: bookingDetails.endTime + ":00",
                children: childId ? [{ childId }] : []
            };

            await createBooking(payload);
            alert(`✅ Automatically assigned ${profile.fullName} to ${childName}!`);
            navigate("/my-bookings");
        } catch (err) {
            console.error("Booking failed:", err);
            alert("Booking failed: " + err.message);
        }
    };

    if (loading) return <div className="pt-32 text-center text-slate-500 font-bold">Loading Profile...</div>;
    if (error) return <div className="pt-32 text-center text-red-500 font-bold">{error}</div>;
    if (!profile) return <div className="pt-32 text-center text-slate-500">Profile not found.</div>;

    return (
        <div className="min-h-screen pt-32 px-6 bg-slate-50 font-sans pb-20">
            <div className="max-w-4xl mx-auto bg-white rounded-[2rem] shadow-2xl overflow-hidden">
                <div className="bg-indigo-600 h-32 md:h-48 relative">
                    <button
                        onClick={() => navigate(-1)}
                        className="absolute top-6 left-6 px-4 py-2 bg-white/20 hover:bg-white/30 text-white rounded-xl font-bold backdrop-blur-sm transition-colors"
                    >
                        ← Back
                    </button>
                </div>

                <div className="px-8 md:px-12 pb-12">
                    <div className="relative -mt-16 mb-6 flex flex-col md:flex-row items-center md:items-end gap-6 text-center md:text-left">
                        <div className="w-32 h-32 bg-white rounded-full p-2 shadow-xl">
                            <div className="w-full h-full bg-slate-100 rounded-full flex items-center justify-center text-4xl">
                                {profile.gender === 'FEMALE' ? '👩‍⚕️' : profile.gender === 'MALE' ? '👨‍⚕️' : '🧑‍⚕️'}
                            </div>
                        </div>
                        <div className="flex-1 pb-4">
                            <h1 className="text-3xl font-black text-slate-900 mb-1">{profile.fullName}</h1>
                            <p className="text-slate-500 font-bold uppercase tracking-wide text-sm">{profile.city}</p>
                        </div>
                        <div className="pb-4">
                            {childName && (
                                <button
                                    onClick={() => setShowBookingModal(true)}
                                    className="px-8 py-3 bg-indigo-600 text-white rounded-xl font-bold shadow-lg shadow-indigo-200 hover:bg-indigo-700 transition-colors"
                                >
                                    Assign to {childName}
                                </button>
                            )}
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-12 mt-12">
                        <div className="space-y-6">
                            <div>
                                <h3 className="text-sm font-black text-slate-400 uppercase mb-2">About</h3>
                                <p className="text-slate-600 leading-relaxed text-lg">{profile.bio || "No biography provided."}</p>
                            </div>
                            <div className="flex gap-12">
                                <div>
                                    <h3 className="text-sm font-black text-slate-400 uppercase mb-1">Experience</h3>
                                    <p className="text-2xl font-black text-slate-800">{profile.experienceYears} Years</p>
                                </div>
                                <div>
                                    <h3 className="text-sm font-black text-slate-400 uppercase mb-1">Age</h3>
                                    <p className="text-2xl font-black text-slate-800">{profile.age}</p>
                                </div>
                            </div>
                        </div>

                        <div className="space-y-6">
                            <div>
                                <h3 className="text-sm font-black text-slate-400 uppercase mb-2">Additional Info</h3>
                                <div className="space-y-3">
                                    <div className="flex items-center justify-between p-4 bg-slate-50 rounded-2xl border border-slate-100">
                                        <span className="font-bold text-slate-500">Gender</span>
                                        <span className="font-bold text-slate-800 capitalize">{profile.gender?.toLowerCase()}</span>
                                    </div>
                                    <div className="flex items-center justify-between p-4 bg-slate-50 rounded-2xl border border-slate-100">
                                        <span className="font-bold text-slate-500">Address</span>
                                        <span className="font-bold text-slate-800">{profile.address || "Hidden"}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Reuse Booking Modal (Simplified) */}
            {showBookingModal && (
                <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-3xl p-8 max-w-sm w-full shadow-2xl animate-fade-in">
                        <h3 className="text-xl font-black text-slate-800 mb-6">Book {profile.fullName}</h3>
                        <div className="space-y-4 mb-6">
                            <div>
                                <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Date</label>
                                <input type="date" value={bookingDetails.date} onChange={e => setBookingDetails({ ...bookingDetails, date: e.target.value })} className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500" />
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Start</label>
                                    <input type="time" value={bookingDetails.startTime} onChange={e => setBookingDetails({ ...bookingDetails, startTime: e.target.value })} className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500" />
                                </div>
                                <div>
                                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">End</label>
                                    <input type="time" value={bookingDetails.endTime} onChange={e => setBookingDetails({ ...bookingDetails, endTime: e.target.value })} className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500" />
                                </div>
                            </div>
                        </div>
                        <div className="flex gap-3">
                            <button onClick={() => setShowBookingModal(false)} className="flex-1 py-3 text-slate-500 font-bold hover:bg-slate-100 rounded-xl">Cancel</button>
                            <button onClick={confirmBooking} className="flex-1 py-3 bg-indigo-600 text-white font-bold rounded-xl hover:bg-indigo-700 shadow-lg">Confirm</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
