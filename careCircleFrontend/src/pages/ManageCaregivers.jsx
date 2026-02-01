import React, { useEffect, useState, useCallback } from "react";
import { getAllCaregivers, getAllCertifications, verifyCaregiver, rejectCaregiver, verifyCertification, rejectCertification } from "../api/adminApi";

export default function ManageCaregivers() {
    const [activeType, setActiveType] = useState("profiles"); // profiles | certs
    const [activeStatus, setActiveStatus] = useState("PENDING"); // PENDING | VERIFIED | REJECTED

    const [data, setData] = useState({
        profiles: { PENDING: [], VERIFIED: [], REJECTED: [] },
        certs: { PENDING: [], VERIFIED: [], REJECTED: [] }
    });

    const [pageState, setPageState] = useState({
        profiles: { PENDING: 0, VERIFIED: 0, REJECTED: 0 },
        certs: { PENDING: 0, VERIFIED: 0, REJECTED: 0 }
    });

    const [hasMore, setHasMore] = useState({
        profiles: { PENDING: true, VERIFIED: true, REJECTED: true },
        certs: { PENDING: true, VERIFIED: true, REJECTED: true }
    });

    const [loading, setLoading] = useState(false);
    const [reasons, setReasons] = useState({});

    const fetchData = useCallback(async (type, status, page, isLoadMore = false) => {
        setLoading(true);
        try {
            let result;
            if (type === "profiles") {
                result = await getAllCaregivers("", [status], page, 5);
            } else {
                result = await getAllCertifications([status], page, 5);
            }

            const newContent = result.content || [];
            setData(prev => ({
                ...prev,
                [type]: {
                    ...prev[type],
                    [status]: isLoadMore ? [...prev[type][status], ...newContent] : newContent
                }
            }));

            setPageState(prev => ({
                ...prev,
                [type]: { ...prev[type], [status]: page }
            }));

            setHasMore(prev => ({
                ...prev,
                [type]: { ...prev[type], [status]: !result.last }
            }));
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        // Fetch only for active view
        fetchData(activeType, activeStatus, 0);
    }, [activeType, activeStatus, fetchData]);

    const handleLoadMore = () => {
        const nextPage = pageState[activeType][activeStatus] + 1;
        fetchData(activeType, activeStatus, nextPage, true);
    };

    const handleUpdateReason = (id, value) => {
        setReasons(prev => ({ ...prev, [id]: value }));
    };

    const handleAction = async (id, actionType) => {
        const reason = reasons[id];
        if (!reason) return alert("Please enter a reason");

        try {
            if (activeType === "profiles") {
                if (actionType === "verify") await verifyCaregiver(id, reason);
                else await rejectCaregiver(id, reason);
            } else {
                if (actionType === "verify") await verifyCertification(id, reason);
                else await rejectCertification(id, reason);
            }
            // Refresh current view
            fetchData(activeType, activeStatus, 0);
            // Also reset page to 0 if we were on a load more
            setPageState(prev => ({ ...prev, [activeType]: { ...prev[activeType], [activeStatus]: 0 } }));
        } catch (err) {
            alert("Action failed: " + err.message);
        }
    };

    const renderItem = (item) => {
        const id = item.id;
        const status = item.verificationStatus;
        const isCert = activeType === "certs";

        return (
            <div key={id} className="card-apple p-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 animate-in-apple">
                <div className="flex-1">
                    <h3 className="text-xl font-semibold">{isCert ? item.name : item.fullName}</h3>
                    <p className="text-gray-500">
                        {isCert ? `Issued by: ${item.issuedBy}` : `${item.userEmail} • ${item.city}`}
                    </p>
                    {isCert && <p className="text-xs text-gray-400 mt-1">Caregiver ID: {item.caregiverId}</p>}
                    <span className={`inline-block mt-2 px-3 py-1 rounded-full text-xs font-bold ${status === 'VERIFIED' ? 'bg-green-100 text-green-600' :
                            status === 'REJECTED' ? 'bg-red-100 text-red-600' :
                                'bg-yellow-100 text-yellow-600'
                        }`}>
                        {status}
                    </span>
                </div>

                <div className="flex flex-col sm:flex-row items-center gap-3 w-full md:w-auto">
                    <input
                        type="text"
                        placeholder="Reason..."
                        className="px-4 py-2 rounded-lg border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-[#0071e3] w-full sm:w-[200px]"
                        value={reasons[id] || ""}
                        onChange={(e) => handleUpdateReason(id, e.target.value)}
                    />
                    <div className="flex gap-2 w-full sm:w-auto">
                        {status !== 'VERIFIED' && (
                            <button onClick={() => handleAction(id, "verify")} className="flex-1 sm:flex-none bg-[#1d1d1f] text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-black transition-colors">Verify</button>
                        )}
                        {status !== 'REJECTED' && (
                            <button onClick={() => handleAction(id, "reject")} className="flex-1 sm:flex-none border border-red-500 text-red-500 px-4 py-2 rounded-lg text-sm font-medium hover:bg-red-50 transition-colors">Reject</button>
                        )}
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7]">
            <div className="max-w-[1024px] mx-auto">
                <h1 className="text-[40px] font-semibold text-[#1d1d1f] mb-8">Manage Caregivers</h1>

                {/* Primary Type Tabs */}
                <div className="flex gap-2 mb-6 border-b border-gray-200 pb-4">
                    <button
                        onClick={() => { setActiveType("profiles"); setActiveStatus("PENDING"); }}
                        className={`px-6 py-2 rounded-lg font-semibold transition-all ${activeType === "profiles" ? "text-[#0071e3] border-b-2 border-[#0071e3]" : "text-gray-500 hover:text-gray-700"}`}
                    >
                        Caretaker Profiles
                    </button>
                    <button
                        onClick={() => { setActiveType("certs"); setActiveStatus("PENDING"); }}
                        className={`px-6 py-2 rounded-lg font-semibold transition-all ${activeType === "certs" ? "text-[#0071e3] border-b-2 border-[#0071e3]" : "text-gray-500 hover:text-gray-700"}`}
                    >
                        Certifications
                    </button>
                </div>

                {/* Secondary Status Tabs */}
                <div className="flex gap-4 mb-8">
                    {["PENDING", "VERIFIED", "REJECTED"].map(s => (
                        <button
                            key={s}
                            onClick={() => setActiveStatus(s)}
                            className={`px-4 py-2 rounded-full text-sm font-medium transition-all ${activeStatus === s ? "bg-[#1d1d1f] text-white" : "bg-white text-[#1d1d1f] border border-gray-200 hover:bg-gray-50"}`}
                        >
                            {s.charAt(0) + s.slice(1).toLowerCase()}
                        </button>
                    ))}
                </div>

                {/* Data List */}
                <div className="grid gap-6">
                    {loading && data[activeType][activeStatus].length === 0 ? (
                        <div className="flex justify-center py-12">
                            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#0071e3]"></div>
                        </div>
                    ) : data[activeType][activeStatus].length === 0 ? (
                        <div className="card-apple p-12 text-center text-gray-500">No items found in this category.</div>
                    ) : (
                        <>
                            {data[activeType][activeStatus].map(renderItem)}

                            {hasMore[activeType][activeStatus] && (
                                <button
                                    onClick={handleLoadMore}
                                    disabled={loading}
                                    className="mx-auto mt-4 px-8 py-3 bg-white border border-gray-200 rounded-full text-sm font-semibold text-[#0071e3] hover:bg-gray-50 transition-colors disabled:opacity-50"
                                >
                                    {loading ? "Loading..." : "Load More"}
                                </button>
                            )}
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}
