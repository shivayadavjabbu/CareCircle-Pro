import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getChildren } from "../api/profileApi";

export default function RegisteredChildren() {
    const navigate = useNavigate();
    const [children, setChildren] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchChildren = async () => {
            try {
                const data = await getChildren();
                // Ensure data is always an array
                setChildren(Array.isArray(data) ? data : []);
            } catch (err) {
                console.error("Failed to fetch children:", err);
                setError("Could not load children data.");
            } finally {
                setLoading(false);
            }
        };
        fetchChildren();
    }, []);

    return (
        <div className="min-h-screen pt-32 bg-[#5c56d6] font-sans pb-20 px-5">
            <div className="max-w-[1000px] mx-auto">
                <div className="flex items-center justify-between mb-8 text-white">
                    <button
                        onClick={() => navigate(-1)}
                        className="flex items-center gap-2 text-white/80 hover:text-white font-bold transition-colors"
                    >
                        ← Back to Dashboard
                    </button>
                    <h1 className="text-3xl font-bold">Registered Children</h1>
                    <div className="w-20"></div> {/* Spacer for centering */}
                </div>

                <div className="bg-white rounded-[2rem] p-8 md:p-10 shadow-2xl">
                    {loading ? (
                        <div className="text-center py-20 text-slate-500 font-bold">Loading children data...</div>
                    ) : error ? (
                        <div className="text-center py-10 text-red-500 font-bold">{error}</div>
                    ) : children.length === 0 ? (
                        <div className="text-center py-20">
                            <div className="text-6xl mb-4">👶</div>
                            <h3 className="text-xl font-bold text-slate-800 mb-2">No Children Registered Yet</h3>
                            <p className="text-slate-500 mb-8">Add your children details to get started.</p>
                            <button
                                onClick={() => navigate("/baby-details")}
                                className="px-8 py-3 bg-[#ff9800] text-white rounded-xl font-bold shadow-lg shadow-orange-200 hover:bg-[#e68a00] transition-colors"
                            >
                                Add Child
                            </button>
                        </div>
                    ) : (
                        <div className="overflow-hidden rounded-xl border border-slate-100">
                            <table className="w-full text-left border-collapse">
                                <thead>
                                    <tr className="bg-slate-50 border-b border-slate-100">
                                        <th className="p-5 font-black text-slate-400 uppercase text-xs tracking-wider">Name</th>
                                        <th className="p-5 font-black text-slate-400 uppercase text-xs tracking-wider">Age (Years)</th>
                                        <th className="p-5 font-black text-slate-400 uppercase text-xs tracking-wider">Gender</th>
                                        <th className="p-5 font-black text-slate-400 uppercase text-xs tracking-wider">Special Needs</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-slate-100">
                                    {children.map((child, index) => (
                                        <tr key={index} className="hover:bg-slate-50/50 transition-colors">
                                            <td className="p-5 font-bold text-slate-700">{child.name}</td>
                                            <td className="p-5 font-bold text-slate-700">{child.age}</td>
                                            <td className="p-5 font-bold text-slate-700 capitalize">{child.gender?.toLowerCase() || "-"}</td>
                                            <td className="p-5 font-bold text-slate-700">
                                                {child.specialNeeds ? (
                                                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                                                        Yes
                                                    </span>
                                                ) : (
                                                    <span className="text-slate-400">No</span>
                                                )}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
