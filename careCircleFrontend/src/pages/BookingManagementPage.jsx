import { useState, useEffect } from "react";
import { createBooking, getMyBookings, acceptBooking, getActiveCaregiverBookings } from "../api/bookingApi";

export default function BookingManagementPage() {
    const role = localStorage.getItem("role");
    const [bookings, setBookings] = useState([]);
    const [form, setForm] = useState({ date: "", time: "", caregiverId: "" });

    useEffect(() => {
        const fetchB = async () => {
            if (role === "ROLE_PARENT") setBookings(await getMyBookings() || []);
            else setBookings(await getActiveCaregiverBookings() || []);
        };
        fetchB();
    }, [role]);

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            await createBooking(form);
            alert("Booking Request Sent!");
            setBookings(await getMyBookings());
        } catch { alert("Error creating booking"); }
    };

    const handleAccept = async (id) => {
        try {
            await acceptBooking(id);
            setBookings(await getActiveCaregiverBookings());
        } catch { alert("Error accepting"); }
    };

    return (
        <div className="min-h-screen pt-32 pb-20 px-6 bg-slate-50">
            <div className="max-w-5xl mx-auto">
                <h1 className="text-4xl font-black text-slate-900 mb-12">Booking Hub</h1>

                {role === "ROLE_PARENT" && (
                    <div className="p-10 bg-white rounded-[2.5rem] shadow-xl border border-slate-100 mb-12">
                        <h2 className="text-2xl font-black text-slate-800 mb-6">New Booking Request</h2>
                        <form onSubmit={handleCreate} className="grid grid-cols-1 md:grid-cols-3 gap-6">
                            <input type="date" value={form.date} onChange={e => setForm({ ...form, date: e.target.value })} className="p-4 rounded-2xl bg-slate-50 border border-slate-100 outline-none" required />
                            <input type="time" value={form.time} onChange={e => setForm({ ...form, time: e.target.value })} className="p-4 rounded-2xl bg-slate-50 border border-slate-100 outline-none" required />
                            <button type="submit" className="py-4 bg-indigo-600 text-white rounded-2xl font-black shadow-xl hover:bg-indigo-700 transition-all">Send Request</button>
                        </form>
                    </div>
                )}

                <div className="space-y-6">
                    <h2 className="text-2xl font-black text-slate-800">Booking History</h2>
                    <div className="grid gap-6">
                        {bookings.length > 0 ? bookings.map((b, i) => (
                            <div key={i} className="p-8 bg-white rounded-[2rem] shadow-lg border border-slate-50 flex justify-between items-center group hover:border-indigo-200 transition-all">
                                <div>
                                    <h3 className="text-xl font-black text-slate-900 mb-1">Booking #{b.id || i + 1}</h3>
                                    <p className="text-slate-400 font-bold uppercase text-xs">{b.date} • {b.time}</p>
                                </div>
                                <div className="flex items-center gap-6">
                                    <span className="px-6 py-2 rounded-full bg-slate-100 text-slate-500 text-xs font-black uppercase">{b.status}</span>
                                    {role === "ROLE_CARETAKER" && b.status === "PENDING" && (
                                        <button onClick={() => handleAccept(b.id)} className="px-8 py-3 bg-indigo-600 text-white rounded-xl font-black shadow-lg hover:shadow-indigo-100 active:scale-95 transition-all">Accept</button>
                                    )}
                                </div>
                            </div>
                        )) : <div className="py-20 text-center font-bold text-slate-400 italic">No bookings found in your history.</div>}
                    </div>
                </div>
            </div>
        </div>
    );
}
