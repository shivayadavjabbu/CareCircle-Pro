import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { getVerifiedCaregivers } from "../api/parentApi";
import { createBooking } from "../api/bookingApi";

export default function FindCaretaker() {
    const location = useLocation();
    const navigate = useNavigate();
    const { childId, childName } = location.state || {}; // Retrieve child details
    const [caretakers, setCaretakers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [bookingModal, setBookingModal] = useState(null); // stores caretaker object if modal open
    const [bookingDetails, setBookingDetails] = useState({
        date: "",
        startTime: "09:00",
        endTime: "17:00",
        serviceId: null // We might need to fetch this or pick from caretaker services
    });

    useEffect(() => {
        if (!childId) {
            alert("No child selected. Redirecting to dashboard.");
            navigate("/parent-dashboard");
            return;
        }

        const fetchCaretakers = async () => {
            try {
                const data = await getVerifiedCaregivers();
                console.log("Allowed Caretakers:", data);
                setCaretakers(Array.isArray(data) ? data : []);
            } catch (err) {
                console.error("Fetch error:", err);
                setError("Failed to load suitable caretakers.");
            } finally {
                setLoading(false);
            }
        };
        fetchCaretakers();
    }, [childId, navigate]);

    const handleBookClick = (caretaker) => {
        // Ideally, we'd let user pick a service. For now, we might pick the first one or ask.
        // Let's check services structure. Assuming caretaker has `services` array.
        const defaultService = caretaker.services?.[0]?.id;
        setBookingDetails(prev => ({ ...prev, serviceId: defaultService }));
        setBookingModal(caretaker);
    };

    const confirmBooking = async () => {
        if (!bookingDetails.date || !bookingDetails.serviceId) {
            alert("Please select a date and ensure the caretaker has a valid service.");
            return;
        }

        try {
            const payload = {
                caregiverId: bookingModal.caregiverId || bookingModal.id, // Check field name from API response
                serviceId: bookingDetails.serviceId,
                bookingType: "HOURLY",
                startDate: bookingDetails.date,
                startTime: bookingDetails.startTime + ":00", // Append seconds
                endTime: bookingDetails.endTime + ":00",
                children: [{ childId: childId }]
            };

            await createBooking(payload);
            alert(`✅ Automatically assigned ${bookingModal.fullName || "Nanny"} to ${childName}!`);
            setBookingModal(null);
            navigate("/my-bookings"); // Redirect to bookings page
        } catch (err) {
            console.error("Booking failed:", err);
            alert("Booking failed: " + err.message);
        }
    };

    return (
        <div className="min-h-screen pt-32 px-6 bg-slate-50 font-sans pb-20">
            <div className="max-w-6xl mx-auto">
                <div className="flex items-center justify-between mb-8">
                    <button
                        onClick={() => navigate("/parent-dashboard")}
                        className="flex items-center gap-2 text-slate-500 hover:text-slate-800 font-bold transition-colors"
                    >
                        ← Back to Dashboard
                    </button>
                    <div className="text-right">
                        <h1 className="text-3xl font-black text-slate-900">Find a Caretaker</h1>
                        {childName && <p className="text-sm font-bold text-indigo-600 uppercase tracking-wider">Booking for: {childName}</p>}
                    </div>
                </div>

                {loading ? (
                    <div className="text-center py-20 font-bold text-slate-400">Loading Caretakers...</div>
                ) : error ? (
                    <div className="text-center py-20 font-bold text-red-500">{error}</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {caretakers.map(caretaker => (
                            <div key={caretaker.id} className="bg-white p-6 rounded-3xl shadow-xl border border-slate-100 flex flex-col transition-transform hover:-translate-y-1">
                                <div className="flex items-center gap-4 mb-4">
                                    <div className="w-12 h-12 bg-indigo-100 rounded-full flex items-center justify-center text-xl">
                                        {caretaker.gender === 'FEMALE' ? '👩‍⚕️' : caretaker.gender === 'MALE' ? '👨‍⚕️' : '🧑‍⚕️'}
                                    </div>
                                    <div>
                                        <h3 className="font-bold text-slate-800">{caretaker.fullName}</h3>
                                        <div className="flex gap-2 text-xs font-bold text-slate-400 uppercase">
                                            <span>{caretaker.city}</span> • <span>{caretaker.experienceYears} Years Exp.</span>
                                        </div>
                                    </div>
                                </div>

                                {/* Summary doesn't have Bio/Services usually, so we encourage viewing profile or direct booking */}
                                <div className="mb-6 flex-1 space-y-3">
                                    <p className="text-sm text-slate-500 italic">Select to view full profile details including bio and services.</p>
                                </div>

                                <div className="space-y-3">
                                    <button
                                        onClick={() => navigate(`/caretaker/${caretaker.id}`, { state: { childId, childName } })}
                                        className="w-full py-3 bg-white border-2 border-slate-100 text-slate-600 rounded-xl font-bold hover:bg-slate-50 transition-colors"
                                    >
                                        View Profile
                                    </button>
                                    <button
                                        onClick={() => handleBookClick(caretaker)}
                                        className="w-full py-3 bg-indigo-600 text-white rounded-xl font-bold hover:bg-indigo-700 transition-colors shadow-lg shadow-indigo-200"
                                    >
                                        Assign to {childName}
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Booking Modal */}
            {bookingModal && (
                <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-3xl p-8 max-w-sm w-full shadow-2xl animate-fade-in">
                        <h3 className="text-xl font-black text-slate-800 mb-1">Confirm Assignment</h3>
                        <p className="text-sm text-slate-500 mb-6">Booking <strong>{bookingModal.fullName}</strong> for <strong>{childName}</strong></p>

                        <div className="space-y-4 mb-6">
                            <div>
                                <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Date</label>
                                <input
                                    type="date"
                                    value={bookingDetails.date}
                                    onChange={e => setBookingDetails({ ...bookingDetails, date: e.target.value })}
                                    className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 font-bold text-slate-700 outline-none focus:border-indigo-500"
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Start</label>
                                    <input
                                        type="time"
                                        value={bookingDetails.startTime}
                                        onChange={e => setBookingDetails({ ...bookingDetails, startTime: e.target.value })}
                                        className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 font-bold text-slate-700 outline-none focus:border-indigo-500"
                                    />
                                </div>
                                <div>
                                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">End</label>
                                    <input
                                        type="time"
                                        value={bookingDetails.endTime}
                                        onChange={e => setBookingDetails({ ...bookingDetails, endTime: e.target.value })}
                                        className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 font-bold text-slate-700 outline-none focus:border-indigo-500"
                                    />
                                </div>
                            </div>
                        </div>

                        <div className="flex gap-3">
                            <button
                                onClick={() => setBookingModal(null)}
                                className="flex-1 py-3 text-slate-500 font-bold hover:bg-slate-100 rounded-xl transition-colors"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={confirmBooking}
                                className="flex-1 py-3 bg-indigo-600 text-white font-bold rounded-xl hover:bg-indigo-700 transition-colors shadow-lg shadow-indigo-200"
                            >
                                Confirm
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
