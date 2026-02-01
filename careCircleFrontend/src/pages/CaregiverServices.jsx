import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
    getServices, addService, updateService, deleteService,
    getCertifications, addCertification, updateCertification, deleteCertification
} from "../api/caregiverApi";
import { getActiveServices } from "../api/matchingApi";
import { getActiveCities } from "../api/cityApi";

export default function CaregiverServices() {
    const navigate = useNavigate();

    // State for Global Data
    const [globalServices, setGlobalServices] = useState([]);
    const [cities, setCities] = useState([]);

    // State for My Data
    const [myServices, setMyServices] = useState([]);
    const [myCertifications, setMyCertifications] = useState([]);

    // Form States
    const [serviceForm, setServiceForm] = useState({
        serviceId: "",
        city: "",
        extraPrice: 0,
        description: "",
        minChildAge: 0,
        maxChildAge: 18
    });

    const [certForm, setCertForm] = useState({
        serviceId: "",
        name: "",
        issuedBy: "",
        validTill: ""
    });

    const [editingServiceId, setEditingServiceId] = useState(null);
    const [editingCertId, setEditingCertId] = useState(null);
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchInitialData();
    }, []);

    const fetchInitialData = async () => {
        setLoading(true);
        try {
            // 1. Fetch Global Metadata (Independent of Profile)
            try {
                const [gs, c] = await Promise.all([
                    getActiveServices(),
                    getActiveCities()
                ]);
                setGlobalServices(gs || []);
                setCities(c || []);
            } catch (err) {
                console.error("Global meta fetch error:", err);
            }

            // 2. Fetch User-Specific Data
            try {
                const [ms, mc] = await Promise.all([
                    getServices(),
                    getCertifications()
                ]);
                setMyServices(ms || []);
                setMyCertifications(mc || []);
            } catch (err) {
                console.warn("User data fetch error (maybe no profile yet):", err);
            }
        } finally {
            setLoading(false);
        }
    };

    // --- Service Actions ---

    const handleServiceChange = (e) => {
        setServiceForm({ ...serviceForm, [e.target.name]: e.target.value });
    };

    const handleEditService = (s) => {
        setEditingServiceId(s.serviceId);
        setServiceForm({
            serviceId: s.serviceId,
            city: s.city,
            extraPrice: s.extraPrice,
            description: s.description,
            minChildAge: s.minChildAge,
            maxChildAge: s.maxChildAge
        });
    };

    const handleServiceSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (editingServiceId) {
                await updateService(serviceForm);
                setMessage("✅ Service updated!");
            } else {
                await addService(serviceForm);
                setMessage("✅ Service added!");
            }
            setEditingServiceId(null);
            setServiceForm({ serviceId: "", city: "", extraPrice: 0, description: "", minChildAge: 0, maxChildAge: 18 });
            fetchInitialData();
        } catch (err) {
            setMessage("❌ Error saving service: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteService = async (id) => {
        if (!window.confirm("Remove this service?")) return;
        try {
            await deleteService(id);
            fetchInitialData();
        } catch (err) {
            alert("Delete failed: " + err.message);
        }
    };

    // --- Certification Actions ---

    const handleCertChange = (e) => {
        setCertForm({ ...certForm, [e.target.name]: e.target.value });
    };

    const handleEditCert = (c) => {
        setEditingCertId(c.id);
        setCertForm({
            serviceId: c.serviceId || "",
            name: c.name,
            issuedBy: c.issuedBy,
            validTill: c.validTill ? c.validTill.split('T')[0] : ""
        });
    };

    const handleCertSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (editingCertId) {
                await updateCertification(editingCertId, certForm);
                setMessage("✅ Certification updated!");
            } else {
                await addCertification(certForm);
                setMessage("✅ Certification added!");
            }
            setEditingCertId(null);
            setCertForm({ serviceId: "", name: "", issuedBy: "", validTill: "" });
            fetchInitialData();
        } catch (err) {
            setMessage("❌ Error saving certification: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteCert = async (id) => {
        if (!window.confirm("Remove this certification?")) return;
        try {
            await deleteCertification(id);
            fetchInitialData();
        } catch (err) {
            alert("Delete failed: " + err.message);
        }
    };

    return (
        <div className="min-h-screen pt-24 px-6 pb-20 font-sans">
            <div className="max-w-[1200px] mx-auto">
                <div className="mb-8 flex justify-between items-center">
                    <div>
                        <h1 className="text-[32px] font-semibold text-[#1d1d1f] tracking-tight">Services & Certifications</h1>
                        <p className="text-[#86868b] mt-1 text-[17px]">Manage your professional offerings and credentials.</p>
                    </div>
                    <button onClick={() => navigate("/caregiver-dashboard")} className="text-[#0071e3] font-medium hover:underline">
                        Back to Dashboard
                    </button>
                </div>

                {message && (
                    <div className="mb-6 p-4 rounded-xl bg-white shadow-sm border border-gray-100 text-center animate-fade-in">
                        {message}
                    </div>
                )}

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">

                    {/* Section 1: Services */}
                    <div className="space-y-8">
                        <div className="card-apple p-8 shadow-lg">
                            <h2 className="text-xl font-bold mb-6">{editingServiceId ? "Edit Service" : "Add New Capability"}</h2>
                            <form onSubmit={handleServiceSubmit} className="space-y-4">
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="label-apple">Service Type</label>
                                        <select name="serviceId" value={serviceForm.serviceId} onChange={handleServiceChange} className="input-apple" required>
                                            <option value="">Select Service</option>
                                            {globalServices.map(s => <option key={s.id} value={s.id}>{s.serviceName}</option>)}
                                        </select>
                                    </div>
                                    <div>
                                        <label className="label-apple">City</label>
                                        <select name="city" value={serviceForm.city} onChange={handleServiceChange} className="input-apple" required>
                                            <option value="">Select City</option>
                                            {cities.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
                                        </select>
                                    </div>
                                </div>
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="label-apple">Extra Price (/hr)</label>
                                        <input type="number" name="extraPrice" value={serviceForm.extraPrice} onChange={handleServiceChange} className="input-apple" />
                                    </div>
                                    <div className="grid grid-cols-2 gap-2">
                                        <div>
                                            <label className="label-apple">Min Age</label>
                                            <input type="number" name="minChildAge" value={serviceForm.minChildAge} onChange={handleServiceChange} className="input-apple" />
                                        </div>
                                        <div>
                                            <label className="label-apple">Max Age</label>
                                            <input type="number" name="maxChildAge" value={serviceForm.maxChildAge} onChange={handleServiceChange} className="input-apple" />
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <label className="label-apple">Description</label>
                                    <textarea name="description" value={serviceForm.description} onChange={handleServiceChange} className="input-apple min-h-[80px]" placeholder="Briefly describe your experience with this service..."></textarea>
                                </div>
                                <div className="flex gap-3 pt-2">
                                    <button type="submit" disabled={loading} className="btn-apple-primary flex-1">
                                        {editingServiceId ? "Update Service" : "Register Service"}
                                    </button>
                                    {editingServiceId && (
                                        <button type="button" onClick={() => setEditingServiceId(null)} className="px-6 py-2 text-gray-500 font-medium">Cancel</button>
                                    )}
                                </div>
                            </form>
                        </div>

                        <div className="space-y-4">
                            <h3 className="font-bold text-[#1d1d1f] flex items-center gap-2">
                                Your Registered Services
                                <span className="bg-gray-100 text-gray-600 px-2 py-0.5 rounded-full text-xs">{myServices.length}</span>
                            </h3>
                            {myServices.length === 0 ? (
                                <div className="p-10 border-2 border-dashed border-gray-200 rounded-3xl text-center text-gray-400">
                                    No services added yet.
                                </div>
                            ) : (
                                myServices.map(s => (
                                    <div key={s.id} className="card-apple p-5 flex justify-between items-center group">
                                        <div>
                                            <div className="flex items-center gap-2">
                                                <h4 className="font-bold text-lg">{globalServices.find(gs => gs.id === s.serviceId)?.serviceName || "Service"}</h4>
                                                {!s.active && <span className="bg-orange-100 text-orange-600 px-2 py-0.5 rounded text-[10px] font-bold uppercase">Pending Verification</span>}
                                            </div>
                                            <p className="text-sm text-gray-500">{s.city} • +${s.extraPrice}/hr • {s.minChildAge}-{s.maxChildAge} years</p>
                                        </div>
                                        <div className="flex gap-2">
                                            <button onClick={() => handleEditService(s)} className="p-2 text-blue-500 hover:bg-blue-50 rounded-full transition-colors"><svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" /></svg></button>
                                            <button onClick={() => handleDeleteService(s.serviceId)} className="p-2 text-red-500 hover:bg-red-50 rounded-full transition-colors"><svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg></button>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
                    </div>

                    {/* Section 2: Certifications */}
                    <div className="space-y-8">
                        <div className="card-apple p-8 shadow-lg bg-indigo-50/30 border-indigo-100/50">
                            <h2 className="text-xl font-bold mb-6">{editingCertId ? "Edit Certification" : "Add Credentials"}</h2>
                            <form onSubmit={handleCertSubmit} className="space-y-4">
                                <div>
                                    <label className="label-apple">Link to Service (Optional)</label>
                                    <select name="serviceId" value={certForm.serviceId} onChange={handleCertChange} className="input-apple">
                                        <option value="">General Certification</option>
                                        {globalServices.map(s => <option key={s.id} value={s.id}>{s.serviceName}</option>)}
                                    </select>
                                </div>
                                <div>
                                    <label className="label-apple">Certification Name</label>
                                    <input name="name" value={certForm.name} onChange={handleCertChange} placeholder="e.g. CPR Certified" className="input-apple" required />
                                </div>
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="label-apple">Issued By</label>
                                        <input name="issuedBy" value={certForm.issuedBy} onChange={handleCertChange} placeholder="Organization" className="input-apple" required />
                                    </div>
                                    <div>
                                        <label className="label-apple">Valid Till</label>
                                        <input type="date" name="validTill" value={certForm.validTill} onChange={handleCertChange} className="input-apple" required />
                                    </div>
                                </div>
                                <div className="flex gap-3 pt-2">
                                    <button type="submit" disabled={loading} className="btn-apple-primary flex-1 bg-indigo-600 hover:bg-indigo-700">
                                        {editingCertId ? "Update Certification" : "Add Certification"}
                                    </button>
                                    {editingCertId && (
                                        <button type="button" onClick={() => setEditingCertId(null)} className="px-6 py-2 text-gray-500 font-medium">Cancel</button>
                                    )}
                                </div>
                            </form>
                        </div>

                        <div className="space-y-4">
                            <h3 className="font-bold text-[#1d1d1f] flex items-center gap-2">
                                Professional Credentials
                                <span className="bg-indigo-50 text-indigo-600 px-2 py-0.5 rounded-full text-xs">{myCertifications.length}</span>
                            </h3>
                            {myCertifications.length === 0 ? (
                                <div className="p-10 border-2 border-dashed border-indigo-100 rounded-3xl text-center text-indigo-300">
                                    No certifications added.
                                </div>
                            ) : (
                                myCertifications.map(c => (
                                    <div key={c.id} className="card-apple p-5 flex justify-between items-center bg-white border-indigo-50 shadow-sm">
                                        <div>
                                            <div className="flex items-center gap-2">
                                                <h4 className="font-bold text-[#1d1d1f]">{c.name}</h4>
                                                <span className={`text-[10px] font-bold px-2 py-0.5 rounded uppercase ${c.verificationStatus === 'VERIFIED' ? 'bg-green-100 text-green-600' : 'bg-orange-100 text-orange-600'
                                                    }`}>
                                                    {c.verificationStatus}
                                                </span>
                                            </div>
                                            <p className="text-xs text-gray-500 mt-0.5">
                                                {c.issuedBy} • Valid: {new Date(c.validTill).toLocaleDateString()}
                                                {c.serviceName && ` • ${c.serviceName}`}
                                            </p>
                                        </div>
                                        <div className="flex gap-2">
                                            <button onClick={() => handleEditCert(c)} className="p-2 text-indigo-500 hover:bg-indigo-50 rounded-full transition-colors"><svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" /></svg></button>
                                            <button onClick={() => handleDeleteCert(c.id)} className="p-2 text-red-500 hover:bg-red-50 rounded-full transition-colors"><svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg></button>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
                    </div>

                </div>
            </div>
        </div>
    );
}
