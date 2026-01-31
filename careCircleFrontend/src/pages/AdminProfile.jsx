import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createAdminProfile } from "../api/adminApi";

export default function AdminProfile() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        fullName: "",
        phoneNumber: "",
        address: "",
        city: "",
        adminLevel: "ADMIN",
    });

    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage("");

        try {
            await createAdminProfile(formData);
            setMessage("✅ Admin profile created successfully!");
            setTimeout(() => {
                navigate("/admin-dashboard");
            }, 1500);
        } catch (error) {
            console.error("Profile Save Error:", error);
            setMessage("❌ " + (error.message || "Failed to save profile"));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen pt-28 bg-gray-50 flex items-center justify-center p-8 font-sans">
            <div className="bg-white p-10 rounded-2xl shadow-lg w-full max-w-[600px] border border-gray-100">
                <h2 className="text-2xl font-bold text-gray-800 mb-2 text-center">🛡️ Admin Profile Setup</h2>
                <p className="text-gray-500 text-center mb-8">
                    Complete your admin profile to access the dashboard
                </p>

                {message && <p className={`mb-6 text-center font-semibold ${message.includes("✅") ? "text-green-600" : "text-red-600"}`}>{message}</p>}

                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-semibold text-gray-600 ml-1">Full Name</label>
                        <input
                            type="text"
                            name="fullName"
                            placeholder="e.g. System Admin"
                            value={formData.fullName}
                            onChange={handleChange}
                            required
                            className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-orange-400 focus:ring-4 focus:ring-orange-400/10 transition-all placeholder-gray-300"
                        />
                    </div>

                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-semibold text-gray-600 ml-1">Phone Number</label>
                        <input
                            type="tel"
                            name="phoneNumber"
                            placeholder="e.g. +91 9000000000"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            required
                            className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-orange-400 focus:ring-4 focus:ring-orange-400/10 transition-all placeholder-gray-300"
                        />
                    </div>

                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-semibold text-gray-600 ml-1">Address</label>
                        <textarea
                            name="address"
                            placeholder="Your full address..."
                            value={formData.address}
                            onChange={handleChange}
                            required
                            className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-orange-400 focus:ring-4 focus:ring-orange-400/10 transition-all placeholder-gray-300 resize-y min-h-[80px]"
                        />
                    </div>

                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-semibold text-gray-600 ml-1">City</label>
                        <input
                            type="text"
                            name="city"
                            placeholder="e.g. Hyderabad"
                            value={formData.city}
                            onChange={handleChange}
                            required
                            className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-orange-400 focus:ring-4 focus:ring-orange-400/10 transition-all placeholder-gray-300"
                        />
                    </div>

                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-semibold text-gray-600 ml-1">Admin Level</label>
                        <select
                            name="adminLevel"
                            value={formData.adminLevel}
                            onChange={handleChange}
                            required
                            className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-orange-400 focus:ring-4 focus:ring-orange-400/10 transition-all bg-white"
                        >
                            <option value="ADMIN">Admin</option>
                            <option value="SUPER_ADMIN">Super Admin</option>
                        </select>
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full p-3.5 mt-4 bg-orange-500 text-white font-bold rounded-xl shadow-md hover:bg-orange-600 hover:-translate-y-0.5 transition-all active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed"
                    >
                        {loading ? "Creating Profile..." : "Create Admin Profile"}
                    </button>
                </form>
            </div>
        </div>
    );
}
