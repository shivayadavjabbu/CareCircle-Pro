import React, { useEffect, useState } from "react";
import { createCity, updateCity, deleteCity } from "../api/adminApi";
import { getActiveCities } from "../api/cityApi";

export default function ManageCities() {
    const [cities, setCities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [currentCity, setCurrentCity] = useState(null);
    const [formData, setFormData] = useState({ name: "", state: "", country: "" });

    useEffect(() => {
        fetchCities();
    }, []);

    const fetchCities = async () => {
        try {
            const data = await getActiveCities();
            setCities(data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleOpenModal = (city = null) => {
        if (city) {
            setCurrentCity(city);
            setFormData({
                name: city.name,
                state: city.state,
                country: city.country
            });
        } else {
            setCurrentCity(null);
            setFormData({ name: "", state: "", country: "" });
        }
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (currentCity) {
                await updateCity(currentCity.id, formData);
            } else {
                await createCity(formData);
            }
            setShowModal(false);
            fetchCities();
        } catch (err) {
            alert("Operation failed: " + err.message);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to deactivate this city?")) return;
        try {
            await deleteCity(id);
            fetchCities();
        } catch (err) {
            alert("Delete failed: " + err.message);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen pt-[100px] flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#0071e3]"></div>
            </div>
        );
    }

    return (
        <div className="min-h-screen pt-[100px] pb-12 px-6 bg-[#f5f5f7]">
            <div className="max-w-[1024px] mx-auto">
                <div className="flex justify-between items-end mb-8">
                    <div>
                        <h1 className="text-[40px] font-semibold text-[#1d1d1f]">Manage Cities</h1>
                        <p className="text-gray-500">Configure supported locations.</p>
                    </div>
                    <button
                        onClick={() => handleOpenModal()}
                        className="bg-[#0071e3] text-white px-6 py-2 rounded-full font-medium hover:bg-[#0077ed] transition-colors"
                    >
                        Add City
                    </button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {cities.length === 0 ? (
                        <div className="col-span-full card-apple p-12 text-center text-gray-500">No cities defined.</div>
                    ) : (
                        cities.map((c) => (
                            <div key={c.id} className="card-apple p-6 flex flex-col justify-between group animate-in-apple">
                                <div className="mb-4">
                                    <h3 className="text-xl font-semibold mb-1">{c.name}</h3>
                                    <p className="text-sm text-gray-500">{c.state}, {c.country}</p>
                                </div>
                                <div className="flex gap-2 border-t border-gray-100 pt-4">
                                    <button onClick={() => handleOpenModal(c)} className="flex-1 py-2 text-sm font-medium text-gray-600 hover:text-[#0071e3] bg-gray-50 rounded-lg transition-colors">
                                        Edit
                                    </button>
                                    <button onClick={() => handleDelete(c.id)} className="flex-1 py-2 text-sm font-medium text-red-500 hover:text-red-700 bg-red-50 rounded-lg transition-colors">
                                        Deactivate
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            {showModal && (
                <div className="fixed inset-0 bg-black/20 backdrop-blur-sm z-50 flex items-center justify-center p-6">
                    <div className="bg-white rounded-[24px] w-full max-w-[500px] p-8 shadow-2xl animate-in-apple">
                        <h2 className="text-2xl font-semibold mb-6">{currentCity ? "Edit City" : "Add New City"}</h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">City Name</label>
                                <input
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                    value={formData.name}
                                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">State</label>
                                    <input
                                        required
                                        className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                        value={formData.state}
                                        onChange={(e) => setFormData({ ...formData, state: e.target.value })}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Country</label>
                                    <input
                                        required
                                        className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                        value={formData.country}
                                        onChange={(e) => setFormData({ ...formData, country: e.target.value })}
                                    />
                                </div>
                            </div>
                            <div className="flex gap-4 pt-6">
                                <button
                                    type="button"
                                    onClick={() => setShowModal(false)}
                                    className="flex-1 px-6 py-3 rounded-full font-semibold border border-gray-200 hover:bg-gray-50 transition-colors"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="flex-1 px-6 py-3 rounded-full font-semibold bg-[#1d1d1f] text-white hover:bg-black transition-colors"
                                >
                                    {currentCity ? "Save Changes" : "Add City"}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
