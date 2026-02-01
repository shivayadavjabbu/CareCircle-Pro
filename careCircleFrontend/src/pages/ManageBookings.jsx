import React, { useEffect, useState, useCallback } from "react";
import { getBookings } from "../api/adminApi";

export default function ManageBookings() {
    const [activeTab, setActiveTab] = useState("active"); // active | completed | cancelled
    const [bookings, setBookings] = useState({
        active: [],
        completed: [],
        cancelled: []
    });
    const [pageState, setPageState] = useState({
        active: 0,
        completed: 0,
        cancelled: 0
    });
    const [hasMore, setHasMore] = useState({
        active: true,
        completed: true,
        cancelled: true
    });
    const [loading, setLoading] = useState(false);

    const fetchBookings = useCallback(async (type, page, isLoadMore = false) => {
        setLoading(true);
        try {
            const result = await getBookings(type, page, 5);
            const newContent = result.content || [];

            setBookings(prev => ({
                ...prev,
                [type]: isLoadMore ? [...prev[type], ...newContent] : newContent
            }));

            setPageState(prev => ({
                ...prev,
                [type]: page
            }));

            setHasMore(prev => ({
                ...prev,
                [type]: !result.last
            }));
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        if (bookings[activeTab].length === 0) {
            fetchBookings(activeTab, 0);
        }
    }, [activeTab, fetchBookings, bookings]);

    const handleLoadMore = () => {
        const nextPage = pageState[activeTab] + 1;
        fetchBookings(activeTab, nextPage, true);
    };

    const StatusBadge = ({ status }) => {
        const colors = {
            'REQUESTED': 'bg-blue-100 text-blue-600',
            'ACCEPTED': 'bg-green-100 text-green-600',
            'COMPLETED': 'bg-gray-100 text-gray-600',
            'CANCELLED': 'bg-red-100 text-red-600',
            'REJECTED': 'bg-orange-100 text-orange-600'
        };
        return (
            <span className={`px-3 py-1 rounded-full text-xs font-bold ${colors[status] || 'bg-gray-100 text-gray-600'}`}>
                {status}
            </span>
        );
    };

    return (
        <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7]">
            <div className="max-w-[1024px] mx-auto">
                <h1 className="text-[40px] font-semibold text-[#1d1d1f] mb-8">Manage Bookings</h1>

                {/* Tabs */}
                <div className="flex gap-4 mb-8">
                    {["active", "completed", "cancelled"].map(tab => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={`px-6 py-2 rounded-full font-medium transition-all ${activeTab === tab ? "bg-[#1d1d1f] text-white" : "bg-white text-[#1d1d1f] border border-gray-200 hover:bg-gray-50"}`}
                        >
                            {tab.charAt(0).toUpperCase() + tab.slice(1)}
                        </button>
                    ))}
                </div>

                {/* List */}
                <div className="grid gap-6">
                    {loading && bookings[activeTab].length === 0 ? (
                        <div className="flex justify-center py-12">
                            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#0071e3]"></div>
                        </div>
                    ) : bookings[activeTab].length === 0 ? (
                        <div className="card-apple p-12 text-center text-gray-500">No bookings found in this category.</div>
                    ) : (
                        <>
                            {bookings[activeTab].map((b) => (
                                <div key={b.id} className="card-apple p-6 animate-in-apple">
                                    <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                                        <div className="flex-1">
                                            <div className="flex items-center gap-3 mb-2">
                                                <h3 className="text-lg font-semibold">Booking #{b.id.slice(0, 8)}</h3>
                                                <StatusBadge status={b.status} />
                                            </div>
                                            <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-2 text-sm text-gray-500">
                                                <p><span className="font-medium text-gray-700">Type:</span> {b.bookingType}</p>
                                                <p><span className="font-medium text-gray-700">Price:</span> ${b.finalPrice.toFixed(2)}</p>
                                                <p><span className="font-medium text-gray-700">Parent:</span> {b.parentId.slice(0, 8)}...</p>
                                                <p><span className="font-medium text-gray-700">Caregiver:</span> {b.caregiverId.slice(0, 8)}...</p>
                                                <p className="sm:col-span-2">
                                                    <span className="font-medium text-gray-700">Time:</span> {b.bookingType === 'HOURLY' ? `${b.startTime} - ${b.endTime}` : `${b.startDate} to ${b.endDate}`}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}

                            {hasMore[activeTab] && (
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
