import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
    getCaregiverProfile,
    getCertifications,
    getCapabilities,
    addCertification,
    addCapability
} from "../api/caregiverApi";

export default function NannyDashboard() {
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [certifications, setCertifications] = useState([]);
    const [capabilities, setCapabilities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    const [newCert, setNewCert] = useState({ certificationName: "", issuedBy: "", validTill: "" });
    const [newCap, setNewCap] = useState({ serviceType: "INFANT_CARE", description: "", minChildAge: 0, maxChildAge: 10, requiresCertification: true });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const prof = await getCaregiverProfile();
                setProfile(prof);

                const certs = await getCertifications();
                setCertifications(certs || []);

                const caps = await getCapabilities();
                setCapabilities(caps || []);
            } catch (err) {
                console.error("Dashboard fetch error:", err);
                if (err.message.includes("404")) {
                    navigate("/nanny-profile");
                } else {
                    setError(err.message || "Failed to load dashboard data");
                }
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [navigate]);

    const handleAddCert = async (e) => {
        e.preventDefault();
        try {
            await addCertification(newCert);
            setCertifications([...certifications, newCert]);
            setNewCert({ certificationName: "", issuedBy: "", validTill: "" });
            setMessage("✅ Certification added!");
        } catch (err) {
            setError(err.message);
        }
    };

    const handleAddCap = async (e) => {
        e.preventDefault();
        try {
            await addCapability(newCap);
            setCapabilities([...capabilities, newCap]);
            setNewCap({ serviceType: "INFANT_CARE", description: "", minChildAge: 0, maxChildAge: 10, requiresCertification: true });
            setMessage("✅ Capability added!");
        } catch (err) {
            setError(err.message);
        }
    };

    if (loading) return <div className="pt-32 text-center font-bold">Loading Dashboard...</div>;

    return (
        <div className="min-h-screen pt-28 pb-20 bg-slate-50 px-6">
            <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8">

                {/* Profile Summary */}
                <div className="lg:col-span-1 space-y-8">
                    <div className="bg-white p-8 rounded-3xl shadow-xl border border-slate-100">
                        <h2 className="text-2xl font-black text-slate-900 mb-6">Profile Summary</h2>
                        <div className="space-y-4">
                            <div className="flex flex-col">
                                <span className="text-xs font-bold text-slate-400 uppercase">Name</span>
                                <span className="font-bold text-slate-700">{profile?.fullName}</span>
                            </div>
                            <div className="flex flex-col">
                                <span className="text-xs font-bold text-slate-400 uppercase">Location</span>
                                <span className="font-bold text-slate-700">{profile?.city}</span>
                            </div>
                            <div className="flex flex-col">
                                <span className="text-xs font-bold text-slate-400 uppercase">Experience</span>
                                <span className="font-bold text-slate-700">{profile?.experienceYears} Years</span>
                            </div>
                            <button
                                onClick={() => navigate("/nanny-profile")}
                                className="w-full mt-4 py-3 bg-slate-100 text-slate-600 rounded-xl font-bold hover:bg-slate-200 transition-all"
                            >
                                Edit Profile
                            </button>
                        </div>
                    </div>

                    <div className="bg-white p-8 rounded-3xl shadow-xl border border-slate-100">
                        <h2 className="text-2xl font-black text-slate-900 mb-6">Quick Chat</h2>
                        <button
                            onClick={() => navigate("/communication")}
                            className="w-full py-4 bg-indigo-600 text-white rounded-2xl font-black shadow-lg shadow-indigo-100 hover:bg-indigo-700 transition-all flex items-center justify-center gap-2"
                        >
                            💬 Open Messages
                        </button>
                    </div>
                </div>

                {/* Management Area */}
                <div className="lg:col-span-2 space-y-8">
                    {error && <div className="p-4 bg-red-50 text-red-600 rounded-2xl text-sm font-bold animate-pulse">⚠️ {error}</div>}
                    {message && <div className="p-4 bg-green-50 text-green-600 rounded-2xl text-sm font-bold animate-bounce">✅ {message}</div>}

                    {/* Certifications */}
                    <section className="bg-white p-8 rounded-3xl shadow-xl border border-slate-100">
                        <h2 className="text-2xl font-black text-slate-900 mb-6">Certifications</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
                            {certifications.map((cert, i) => (
                                <div key={i} className="p-4 bg-slate-50 rounded-2xl border border-slate-100">
                                    <p className="font-black text-slate-800">{cert.certificationName}</p>
                                    <p className="text-xs font-bold text-slate-500 uppercase">{cert.issuedBy}</p>
                                </div>
                            ))}
                        </div>
                        <form onSubmit={handleAddCert} className="space-y-4 pt-6 border-t border-slate-50">
                            <h3 className="text-sm font-black text-slate-400 uppercase">Add New Certification</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <input
                                    placeholder="Cert Name"
                                    value={newCert.certificationName}
                                    onChange={e => setNewCert({ ...newCert, certificationName: e.target.value })}
                                    className="p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500"
                                />
                                <input
                                    placeholder="Issued By"
                                    value={newCert.issuedBy}
                                    onChange={e => setNewCert({ ...newCert, issuedBy: e.target.value })}
                                    className="p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500"
                                />
                            </div>
                            <button type="submit" className="px-6 py-2 bg-slate-900 text-white rounded-xl font-bold hover:bg-slate-800">Add Cert</button>
                        </form>
                    </section>

                    {/* Capabilities */}
                    <section className="bg-white p-8 rounded-3xl shadow-xl border border-slate-100">
                        <h2 className="text-2xl font-black text-slate-900 mb-6">Services & Capabilities</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
                            {capabilities.map((cap, i) => (
                                <div key={i} className="p-4 bg-slate-50 rounded-2xl border border-slate-100">
                                    <p className="font-black text-indigo-600">{cap.serviceType.replace('_', ' ')}</p>
                                    <p className="text-sm font-medium text-slate-600">{cap.description}</p>
                                </div>
                            ))}
                        </div>
                        <form onSubmit={handleAddCap} className="space-y-4 pt-6 border-t border-slate-50">
                            <h3 className="text-sm font-black text-slate-400 uppercase">Add New Service</h3>
                            <select
                                value={newCap.serviceType}
                                onChange={e => setNewCap({ ...newCap, serviceType: e.target.value })}
                                className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500"
                            >
                                <option value="INFANT_CARE">Infant Care</option>
                                <option value="TODDLER_CARE">Toddler Care</option>
                                <option value="SCHOOL_AGE_CARE">School Age Care</option>
                                <option value="SPECIAL_NEEDS">Special Needs</option>
                            </select>
                            <textarea
                                placeholder="Describe your experience with this service..."
                                value={newCap.description}
                                onChange={e => setNewCap({ ...newCap, description: e.target.value })}
                                className="w-full p-3 rounded-xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500 min-h-[100px]"
                            />
                            <button type="submit" className="px-6 py-2 bg-slate-900 text-white rounded-xl font-bold hover:bg-slate-800">Add Service</button>
                        </form>
                    </section>

                </div>
            </div>
        </div>
    );
}
