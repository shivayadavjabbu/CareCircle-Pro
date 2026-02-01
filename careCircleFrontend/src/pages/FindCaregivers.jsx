import React, { useEffect, useState, useCallback } from "react";
import { searchCaregivers, getActiveServices } from "../api/matchingApi";
import { getActiveCities } from "../api/cityApi";

export default function FindCaregivers() {
    const [caregivers, setCaregivers] = useState([]);
    const [services, setServices] = useState([]);
    const [cities, setCities] = useState([]);

    const [filters, setFilters] = useState({
        city: "",
        serviceId: ""
    });

    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchInitialMeta = async () => {
            try {
                const [sData, cData] = await Promise.all([
                    getActiveServices(),
                    getActiveCities()
                ]);
                setServices(sData);
                setCities(cData);
            } catch (err) {
                console.error("Failed to fetch metadata:", err);
            }
        };
        fetchInitialMeta();
    }, []);

    const fetchCaregivers = useCallback(async (p, isLoadMore = false) => {
        setLoading(true);
        try {
            const result = await searchCaregivers(filters.city, filters.serviceId, p, 6);
            const newContent = result.content || [];

            setCaregivers(prev => isLoadMore ? [...prev, ...newContent] : newContent);
            setHasMore(!result.last);
            setPage(p);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [filters]);

    useEffect(() => {
        fetchCaregivers(0);
    }, [fetchCaregivers]);

    const handleFilterChange = (e) => {
        setFilters(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleLoadMore = () => {
        fetchCaregivers(page + 1, true);
    };

    return (
        <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7]">
            <div className="max-w-[1200px] mx-auto">
                <h1 className="text-[40px] font-semibold text-[#1d1d1f] mb-8 animate-in-apple">Find Caregivers</h1>

                {/* Search Panel */}
                <div className="card-apple p-8 mb-10 flex flex-wrap gap-6 items-end animate-in-apple" style={{ animationDelay: '0.1s' }}>
                    <div className="flex-1 min-w-[200px]">
                        <label className="block text-sm font-semibold text-gray-500 mb-2 uppercase tracking-wider">City</label>
                        <select
                            name="city"
                            value={filters.city}
                            onChange={handleFilterChange}
                            className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3] bg-white transition-all"
                        >
                            <option value="">All Cities</option>
                            {cities.map(c => (
                                <option key={c.id} value={c.name}>{c.name}</option>
                            ))}
                        </select>
                    </div>

                    <div className="flex-1 min-w-[200px]">
                        <label className="block text-sm font-semibold text-gray-500 mb-2 uppercase tracking-wider">Service Type</label>
                        <select
                            name="serviceId"
                            value={filters.serviceId}
                            onChange={handleFilterChange}
                            className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3] bg-white transition-all"
                        >
                            <option value="">All Services</option>
                            {services.map(s => (
                                <option key={s.id} value={s.id}>{s.serviceName}</option>
                            ))}
                        </select>
                    </div>

                    <button
                        onClick={() => fetchCaregivers(0)}
                        className="bg-[#1d1d1f] text-white px-8 py-3 rounded-xl font-semibold hover:bg-black transition-colors"
                    >
                        Search
                    </button>
                </div>

                {/* Results Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {loading && caregivers.length === 0 ? (
                        <div className="col-span-full flex justify-center py-24">
                            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#0071e3]"></div>
                        </div>
                    ) : caregivers.length === 0 ? (
                        <div className="col-span-full card-apple p-24 text-center">
                            <p className="text-xl text-gray-400">No caregivers found for the selected criteria.</p>
                        </div>
                    ) : (
                        <>
                            {caregivers.map((c, i) => (
                                <div
                                    key={c.id}
                                    className="card-apple p-6 flex flex-col justify-between hover:scale-[1.02] transition-transform animate-in-apple"
                                    style={{ animationDelay: `${0.1 + i * 0.05}s` }}
                                >
                                    <div>
                                        <div className="flex justify-between items-start mb-4">
                                            <div className="w-12 h-12 rounded-full bg-gradient-to-br from-blue-100 to-indigo-100 flex items-center justify-center text-[#0071e3] font-bold text-xl">
                                                {c.caregiverName.charAt(0)}
                                            </div>
                                            <span className="text-xs font-bold text-gray-400 uppercase tracking-widest">{c.city}</span>
                                        </div>
                                        <h3 className="text-xl font-bold text-[#1d1d1f] mb-2">{c.caregiverName}</h3>
                                        <p className="text-sm text-gray-500 line-clamp-3 mb-4">{c.description}</p>
                                        <div className="flex flex-wrap gap-2 mb-6">
                                            <span className="px-3 py-1 bg-green-50 text-green-600 rounded-full text-xs font-bold font-mono">+${c.extraPrice}/hr</span>
                                            <span className="px-3 py-1 bg-blue-50 text-blue-600 rounded-full text-xs font-bold">Ages {c.minChildAge}-{c.maxChildAge}</span>
                                        </div>
                                    </div>
                                    <button
                                        onClick={() => alert("Booking functionality coming in the next step!")}
                                        className="w-full py-3 bg-[#0071e3] text-white rounded-xl font-semibold hover:bg-[#0077ed] transition-colors"
                                    >
                                        Book Now
                                    </button>
                                </div>
                            ))}

                            {hasMore && (
                                <div className="col-span-full flex justify-center mt-8">
                                    <button
                                        onClick={handleLoadMore}
                                        disabled={loading}
                                        className="px-8 py-3 bg-white border border-gray-200 rounded-full text-sm font-semibold text-[#0071e3] hover:bg-gray-50 transition-colors disabled:opacity-50"
                                    >
                                        {loading ? "Loading..." : "Load More"}
                                    </button>
                                </div>
                            )}
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}
