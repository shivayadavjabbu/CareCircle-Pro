import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createParentProfile, getParentProfile, createCaregiverProfile, getCaregiverProfile, addChild, getChildren } from "../api/profileApi";

export default function ProfileSetupPage() {
    const navigate = useNavigate();
    const role = localStorage.getItem("role");
    const [loading, setLoading] = useState(false);
    const [isUpdate, setIsUpdate] = useState(false);
    const [error, setError] = useState("");
    const [profile, setProfile] = useState({ fullName: "", phoneNumber: "", city: "", bio: "", experienceYears: "", age: "", gender: "", address: "" });
    const [child, setChild] = useState({ name: "", age: "", gender: "" });
    const [children, setChildren] = useState([]);

    useEffect(() => {
        const fetchExisting = async () => {
            let data = null;
            if (role === "ROLE_PARENT") {
                data = await getParentProfile();
                setChildren(await getChildren() || []);
            } else if (role === "ROLE_CARETAKER") {
                data = await getCaregiverProfile();
                if (data) setIsUpdate(true);
            }
            if (data) setProfile(prev => ({ ...prev, ...data }));
        };
        fetchExisting();
    }, [role]);

    const handleSave = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            setError("");
            if (role === "ROLE_PARENT") {
                if (isUpdate) await updateParentProfile(profile);
                else await createParentProfile(profile);
            } else if (role === "ROLE_CARETAKER") {
                if (isUpdate) await updateCaregiverProfile(profile);
                else await createCaregiverProfile(profile);
            }
            if (role === "ROLE_CARETAKER") navigate("/caretaker-dashboard");
            else navigate("/parent-dashboard");
        } catch (err) {
            console.error("Save Error:", err);
            setError(err.message || "Error saving profile");
        }
        finally { setLoading(false); }
    };

    const handleAddChild = async () => {
        try {
            await addChild(child);
            setChildren([...children, child]);
            setChild({ name: "", age: "", gender: "" });
        } catch (err) { alert("Error adding child"); }
    };

    return (
        <div className="min-h-screen pt-32 pb-20 px-6 bg-slate-50 flex items-center justify-center">
            <div className="w-full max-w-2xl p-10 bg-white rounded-[2.5rem] shadow-2xl border border-slate-100">
                <h2 className="text-3xl font-black text-slate-900 mb-8">Tell us about yourself</h2>

                {error && <div className="p-4 bg-red-50 text-red-600 rounded-2xl text-sm font-bold flex items-center gap-2 mb-6">⚠️ {error}</div>}

                <form onSubmit={handleSave} className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <input placeholder="Full Name" value={profile.fullName} onChange={e => setProfile({ ...profile, fullName: e.target.value })} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all font-medium" required />
                        <input placeholder="Phone Number" value={profile.phoneNumber} onChange={e => setProfile({ ...profile, phoneNumber: e.target.value })} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all font-medium" required />
                    </div>
                    <input placeholder="City" value={profile.city} onChange={e => setProfile({ ...profile, city: e.target.value })} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all font-medium" required />

                    {(role === "ROLE_CARETAKER") && (
                        <input placeholder="Years of Experience" type="number" value={profile.experienceYears} onChange={e => setProfile({ ...profile, experienceYears: e.target.value })} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all font-medium" required />
                    )}

                    <textarea placeholder="Bio / Introduction" value={profile.bio} onChange={e => setProfile({ ...profile, bio: e.target.value })} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all font-medium min-h-[120px]" />

                    <button type="submit" disabled={loading} className="w-full py-4 bg-indigo-600 text-white rounded-2xl font-black shadow-xl shadow-indigo-100 hover:bg-indigo-700 transition-all">
                        {loading ? "Saving..." : "Update Profile"}
                    </button>
                </form>

                {(role === "ROLE_PARENT") && (
                    <div className="mt-12 pt-10 border-t border-slate-100">
                        <h3 className="text-xl font-black text-slate-900 mb-6">Children Management</h3>
                        <div className="space-y-4 mb-8">
                            {children.map((c, i) => (
                                <div key={i} className="p-5 bg-slate-50 rounded-2xl flex justify-between items-center border border-slate-100">
                                    <span className="font-bold text-slate-700">{c.name}</span>
                                    <span className="text-sm font-bold text-indigo-500">{c.age} Years • {c.gender}</span>
                                </div>
                            ))}
                        </div>
                        <div className="flex flex-wrap gap-4">
                            <input placeholder="Name" value={child.name} onChange={e => setChild({ ...child, name: e.target.value })} className="flex-1 min-w-[150px] p-4 rounded-2xl bg-slate-50 border border-slate-100 outline-none" />
                            <input placeholder="Age" type="number" value={child.age} onChange={e => setChild({ ...child, age: e.target.value })} className="w-24 p-4 rounded-2xl bg-slate-50 border border-slate-100 outline-none" />
                            <button onClick={handleAddChild} className="px-8 py-4 bg-slate-900 text-white rounded-2xl font-bold hover:bg-slate-800 transition-all">Add</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
