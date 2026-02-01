import React, { useEffect, useState } from "react";
import { getServices, createService, updateService, deleteService } from "../api/adminApi";

export default function ManageServices() {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [currentService, setCurrentService] = useState(null);
    const [formData, setFormData] = useState({ serviceName: "", description: "", category: "", basePrice: "" });

    useEffect(() => {
        fetchServices();
    }, []);

    const fetchServices = async () => {
        try {
            const data = await getServices();
            setServices(data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleOpenModal = (service = null) => {
        if (service) {
            setCurrentService(service);
            setFormData({
                serviceName: service.serviceName,
                description: service.description,
                category: service.category,
                basePrice: service.basePrice
            });
        } else {
            setCurrentService(null);
            setFormData({ serviceName: "", description: "", category: "", basePrice: "" });
        }
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (currentService) {
                await updateService(currentService.id, formData);
            } else {
                await createService(formData);
            }
            setShowModal(false);
            fetchServices();
        } catch (err) {
            alert("Operation failed: " + err.message);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to deactivate this service?")) return;
        try {
            await deleteService(id);
            fetchServices();
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
                        <h1 className="text-[40px] font-semibold text-[#1d1d1f]">Manage Services</h1>
                        <p className="text-gray-500">Configure platform service offerings.</p>
                    </div>
                    <button
                        onClick={() => handleOpenModal()}
                        className="bg-[#0071e3] text-white px-6 py-2 rounded-full font-medium hover:bg-[#0077ed] transition-colors"
                    >
                        Add Service
                    </button>
                </div>

                <div className="grid gap-6">
                    {services.length === 0 ? (
                        <div className="card-apple p-12 text-center text-gray-500">No services defined.</div>
                    ) : (
                        services.map((s) => (
                            <div key={s.id} className="card-apple p-6 flex justify-between items-center group">
                                <div>
                                    <h3 className="text-xl font-semibold">{s.serviceName}</h3>
                                    <p className="text-sm text-gray-500 mb-2">{s.description}</p>
                                    <div className="flex gap-2">
                                        <span className="px-3 py-1 bg-blue-50 text-blue-600 rounded-full text-xs font-bold uppercase tracking-wider">{s.category}</span>
                                        <span className="px-3 py-1 bg-green-50 text-green-600 rounded-full text-xs font-bold tracking-wider">${s.basePrice}/unit</span>
                                    </div>
                                </div>
                                <div className="flex gap-3">
                                    <button onClick={() => handleOpenModal(s)} className="p-2 text-gray-400 hover:text-[#0071e3] transition-colors">
                                        Edit
                                    </button>
                                    <button onClick={() => handleDelete(s.id)} className="p-2 text-gray-400 hover:text-red-500 transition-colors">
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
                        <h2 className="text-2xl font-semibold mb-6">{currentService ? "Edit Service" : "Add New Service"}</h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Service Name</label>
                                <input
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                    value={formData.serviceName}
                                    onChange={(e) => setFormData({ ...formData, serviceName: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                                <textarea
                                    required
                                    rows={3}
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                    value={formData.description}
                                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Category</label>
                                    <input
                                        required
                                        className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                        value={formData.category}
                                        onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Base Price</label>
                                    <input
                                        required
                                        type="number"
                                        step="0.01"
                                        className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#0071e3]"
                                        value={formData.basePrice}
                                        onChange={(e) => setFormData({ ...formData, basePrice: e.target.value })}
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
                                    {currentService ? "Save Changes" : "Create Service"}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
