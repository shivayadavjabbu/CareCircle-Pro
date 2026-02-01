import React, { useEffect, useState } from "react";
import { getAllCaregivers, getPendingCertifications, verifyCaregiver, rejectCaregiver, verifyCertification, rejectCertification } from "../api/adminApi";

export default function ManageCaregivers() {
    const [pendingCaretakers, setPendingCaretakers] = useState([]);
    const [processedCaretakers, setProcessedCaretakers] = useState([]);
    const [certifications, setCertifications] = useState([]);

    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState("pending");
    const [reasons, setReasons] = useState({});

    // Pagination states
    const [pageState, setPageState] = useState({
        pending: { page: 0, hasMore: true },
        processed: { page: 0, hasMore: true },
        certs: { page: 0, hasMore: false } // Certs API currently doesn't support pagination, but we list it for consistency
    });

    useEffect(() => {
        initialFetch();
    }, []);

    const initialFetch = async () => {
        setLoading(true);
        try {
            // Fetch first page for each caretaker tab
            const pData = await getAllCaregivers("", ["PENDING"], 0, 5);
            setPendingCaretakers(pData.content || []);
            setPageState(prev => ({
                ...prev,
                pending: { page: 0, hasMore: !pData.last }
            }));

            const prData = await getAllCaregivers("", ["VERIFIED", "REJECTED"], 0, 5);
            setProcessedCaretakers(prData.content || []);
            setPageState(prev => ({
                ...prev,
                processed: { page: 0, hasMore: !prData.last }
            }));

            // Certs (all for now)
            const cData = await getPendingCertifications();
            setCertifications(cData || []);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const loadMore = async (tab) => {
        const nextPage = pageState[tab].page + 1;
        try {
            if (tab === "pending") {
                const data = await getAllCaregivers("", ["PENDING"], nextPage, 5);
                setPendingCaretakers(prev => [...prev, ...data.content]);
                setPageState(prev => ({ ...prev, pending: { page: nextPage, hasMore: !data.last } }));
            } else if (tab === "processed") {
                const data = await getAllCaregivers("", ["VERIFIED", "REJECTED"], nextPage, 5);
                setProcessedCaretakers(prev => [...prev, ...data.content]);
                setPageState(prev => ({ ...prev, processed: { page: nextPage, hasMore: !data.last } }));
            }
        } catch (err) {
            alert("Failed to load more: " + err.message);
        }
    };

    const handleUpdateReason = (id, value) => {
        setReasons(prev => ({ ...prev, [id]: value }));
    };

    const handleVerifyProfile = async (id) => {
        const reason = reasons[id];
        if (!reason) return alert("Please enter a reason");
        try {
            await verifyCaregiver(id, reason);
            initialFetch();
        } catch (err) {
            alert("Failed: " + err.message);
        }
    };

    const handleRejectProfile = async (id) => {
        const reason = reasons[id];
        if (!reason) return alert("Please enter a reason");
        try {
            await rejectCaregiver(id, reason);
            initialFetch();
        } catch (err) {
            alert("Failed: " + err.message);
        }
    };

    const handleVerifyCert = async (id) => {
        const reason = reasons[id];
        if (!reason) return alert("Please enter a reason");
        try {
            await verifyCertification(id, reason);
            initialFetch();
        } catch (err) {
            alert("Failed: " + err.message);
        }
    };

    const handleRejectCert = async (id) => {
        const reason = reasons[id];
        if (!reason) return alert("Please enter a reason");
        try {
            await rejectCertification(id, reason);
            initialFetch();
        } catch (err) {
            alert("Failed: " + err.message);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen pt-[120px] flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#0071e3]"></div>
            </div>
        );
    }

    const renderCaretakerList = (list, tabType) => (
        <div className="grid gap-6">
            {list.length === 0 ? (
                <div className="card-apple p-12 text-center text-gray-500">No caretakers found.</div>
            ) : (
                <>
                    {list.map((c) => (
                        <div key={c.id} className="card-apple p-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                            <div className="flex-1">
                                <h3 className="text-xl font-semibold">{c.fullName}</h3>
                                <p className="text-gray-500">{c.userEmail} • {c.city}</p>
                                <span className={`inline-block mt-2 px-3 py-1 rounded-full text-xs font-bold ${c.verificationStatus === 'VERIFIED' ? 'bg-green-100 text-green-600' : c.verificationStatus === 'REJECTED' ? 'bg-red-100 text-red-600' : 'bg-yellow-100 text-yellow-600'}`}>
                                    {c.verificationStatus}
                                </span>
                            </div>
                            <div className="flex flex-col sm:flex-row items-center gap-3 w-full md:w-auto">
                                <input
                                    type="text"
                                    placeholder="Reason..."
                                    className="px-4 py-2 rounded-lg border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-[#0071e3] w-full sm:w-[200px]"
                                    value={reasons[c.id] || ""}
                                    onChange={(e) => handleUpdateReason(c.id, e.target.value)}
                                />
                                <div className="flex gap-2 w-full sm:w-auto">
                                    {c.verificationStatus !== 'VERIFIED' && (
                                        <button onClick={() => handleVerifyProfile(c.id)} className="flex-1 sm:flex-none bg-[#1d1d1f] text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-black transition-colors">Verify</button>
                                    )}
                                    {c.verificationStatus !== 'REJECTED' && (
                                        <button onClick={() => handleRejectProfile(c.id)} className="flex-1 sm:flex-none border border-red-500 text-red-500 px-4 py-2 rounded-lg text-sm font-medium hover:bg-red-50 transition-colors">Reject</button>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                    {pageState[tabType].hasMore && (
                        <button
                            onClick={() => loadMore(tabType)}
                            className="mx-auto mt-4 px-8 py-3 bg-white border border-gray-200 rounded-full text-sm font-semibold text-[#0071e3] hover:bg-gray-50 transition-colors"
                        >
                            Load More
                        </button>
                    )}
                </>
            )}
        </div>
    );

    return (
        <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7]">
            <div className="max-w-[1024px] mx-auto">
                <h1 className="text-[40px] font-semibold text-[#1d1d1f] mb-8">Manage Caregivers</h1>

                <div className="flex flex-wrap gap-4 mb-8">
                    <button
                        onClick={() => setActiveTab("pending")}
                        className={`px-6 py-2 rounded-full font-medium transition-all ${activeTab === "pending" ? "bg-[#1d1d1f] text-white" : "bg-white text-[#1d1d1f] hover:bg-gray-100"}`}
                    >
                        Pending Caretakers
                    </button>
                    <button
                        onClick={() => setActiveTab("processed")}
                        className={`px-6 py-2 rounded-full font-medium transition-all ${activeTab === "processed" ? "bg-[#1d1d1f] text-white" : "bg-white text-[#1d1d1f] hover:bg-gray-100"}`}
                    >
                        Processed Caretakers
                    </button>
                    <button
                        onClick={() => setActiveTab("certifications")}
                        className={`px-6 py-2 rounded-full font-medium transition-all ${activeTab === "certifications" ? "bg-[#1d1d1f] text-white" : "bg-white text-[#1d1d1f] hover:bg-gray-100"}`}
                    >
                        Pending Certifications
                    </button>
                </div>

                {activeTab === "pending" && renderCaretakerList(pendingCaretakers, "pending")}
                {activeTab === "processed" && renderCaretakerList(processedCaretakers, "processed")}
                {activeTab === "certifications" && (
                    <div className="grid gap-6">
                        {certifications.length === 0 ? (
                            <div className="card-apple p-12 text-center text-gray-500">No pending certifications.</div>
                        ) : (
                            certifications.map((cert) => (
                                <div key={cert.id} className="card-apple p-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                                    <div className="flex-1">
                                        <h3 className="text-xl font-semibold">{cert.certificationName}</h3>
                                        <p className="text-gray-500">Issued by: {cert.issuingOrganization}</p>
                                        <p className="text-xs text-gray-400 mt-1">Caregiver ID: {cert.caregiverId}</p>
                                    </div>
                                    <div className="flex flex-col sm:flex-row items-center gap-3 w-full md:w-auto">
                                        <input
                                            type="text"
                                            placeholder="Reason..."
                                            className="px-4 py-2 rounded-lg border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-[#0071e3] w-full sm:w-[200px]"
                                            value={reasons[cert.id] || ""}
                                            onChange={(e) => handleUpdateReason(cert.id, e.target.value)}
                                        />
                                        <div className="flex gap-2 w-full sm:w-auto">
                                            <button onClick={() => handleVerifyCert(cert.id)} className="flex-1 sm:flex-none bg-[#1d1d1f] text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-black transition-colors">Verify</button>
                                            <button onClick={() => handleRejectCert(cert.id)} className="flex-1 sm:flex-none border border-red-500 text-red-500 px-4 py-2 rounded-lg text-sm font-medium hover:bg-red-50 transition-colors">Reject</button>
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
