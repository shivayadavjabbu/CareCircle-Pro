import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createParentProfile, getParentProfile } from "../api/parentApi";

export default function ParentProfile() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: "",
    phoneNumber: "",
    address: "",
    city: "",
  });

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getParentProfile();
        if (data) {
          setFormData({
            fullName: data.fullName || "",
            phoneNumber: data.phoneNumber || "",
            address: data.address || "",
            city: data.city || "",
          });
        }
      } catch (error) {
        console.log("No profile found or fetch error:", error);
      }
    };
    fetchProfile();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      await createParentProfile(formData);
      setMessage("✅ Profile saved successfully! Redirecting to dashboard...");
      setTimeout(() => {
        navigate("/parent-dashboard");
      }, 1500);
    } catch (error) {
      console.error("Profile Save Error:", error);
      setMessage("❌ " + (error.message || "Failed to save profile"));
    } finally {
      setLoading(false);
    }
  };

  const handleSkip = () => {
    navigate("/parent-dashboard");
  };

  return (
    <div className="min-h-screen pt-28 bg-gray-50 flex items-center justify-center p-8 font-sans">
      <div className="bg-white p-10 rounded-2xl shadow-lg w-full max-w-[600px] border border-gray-100">
        <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Parent Profile</h2>
        {message && <p className={`mb-6 text-center font-semibold ${message.includes("✅") ? "text-green-600" : "text-red-600"}`}>{message}</p>}

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-semibold text-gray-600 ml-1">Full Name</label>
            <input
              type="text"
              name="fullName"
              placeholder="e.g. John Doe"
              value={formData.fullName}
              onChange={handleChange}
              required
              className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-red-400 focus:ring-4 focus:ring-red-400/10 transition-all placeholder-gray-300"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-semibold text-gray-600 ml-1">Phone Number</label>
            <input
              type="tel"
              name="phoneNumber"
              placeholder="e.g. +91 9876543210"
              value={formData.phoneNumber}
              onChange={handleChange}
              required
              className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-red-400 focus:ring-4 focus:ring-red-400/10 transition-all placeholder-gray-300"
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
              className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-red-400 focus:ring-4 focus:ring-red-400/10 transition-all placeholder-gray-300 min-h-[100px] resize-y"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-semibold text-gray-600 ml-1">City</label>
            <input
              type="text"
              name="city"
              placeholder="e.g. New York"
              value={formData.city}
              onChange={handleChange}
              required
              className="w-full p-3 border-2 border-gray-100 rounded-xl focus:outline-none focus:border-red-400 focus:ring-4 focus:ring-red-400/10 transition-all placeholder-gray-300"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full p-3.5 bg-red-500 text-white font-bold rounded-xl shadow-md hover:bg-red-600 hover:-translate-y-0.5 transition-all active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed mt-2"
          >
            {loading ? "Saving..." : "Save Profile"}
          </button>

          <button
            type="button"
            onClick={handleSkip}
            className="w-full p-3.5 bg-gray-100 text-gray-600 font-semibold rounded-xl hover:bg-gray-200 transition-all mt-1"
          >
            Skip for now
          </button>
        </form>
      </div>
    </div>
  );
}
