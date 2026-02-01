import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createParentProfile, getParentProfile } from "../api/parentApi";
import { getActiveCities } from "../api/cityApi";

export default function ParentProfile() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: "",
    phoneNumber: "",
    address: "",
    city: "",
  });

  const [cities, setCities] = useState([]);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch supported cities
        const citiesData = await getActiveCities();
        setCities(citiesData || []);

        // Fetch profile
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
        console.log("Fetch error:", error);
      }
    };
    fetchData();
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
    <div className="min-h-screen pt-28 flex items-center justify-center bg-gradient-to-br from-slate-50 via-white to-indigo-50/30 p-6 font-sans">
      <div className="absolute inset-0 z-0 overflow-hidden pointer-events-none">
        <div className="absolute top-[10%] right-[10%] w-[30%] h-[35%] bg-indigo-200/20 blur-[100px] rounded-full animate-float"></div>
        <div className="absolute bottom-[10%] left-[10%] w-[30%] h-[35%] bg-pink-200/20 blur-[100px] rounded-full animate-float" style={{ animationDelay: '-1.5s' }}></div>
      </div>

      <div className="relative z-10 w-full max-w-[600px] animate-fade-in-up">
        <div className="glass-card rounded-[2.5rem] p-10 md:p-12 border-white/50 shadow-2xl">
          <div className="text-center mb-10">
            <h2 className="text-3xl font-extrabold text-slate-900 mb-2">Build Your Main Profile</h2>
            <p className="text-slate-500 font-medium tracking-tight">Tell us a bit about yourself</p>
          </div>

          {message && (
            <div className={`mb-6 p-4 rounded-2xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2 ${message.includes("✅") ? "bg-green-50 border border-green-100 text-green-600" : "bg-red-50 border border-red-100 text-red-600"}`}>
              <span className="text-lg">{message.includes("✅") ? "✅" : "⚠️"}</span>
              <p className="text-sm font-bold">{message}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 ml-1">Full Name</label>
              <input
                type="text"
                name="fullName"
                placeholder="e.g. John Doe"
                value={formData.fullName}
                onChange={handleChange}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
              />
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 ml-1">Phone Number</label>
              <input
                type="tel"
                name="phoneNumber"
                placeholder="e.g. +91 9876543210"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50"
              />
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 ml-1">Address</label>
              <textarea
                name="address"
                placeholder="Your full address..."
                value={formData.address}
                onChange={handleChange}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50 min-h-[100px] resize-y"
              />
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 ml-1">City</label>
              <select
                name="city"
                value={formData.city}
                onChange={handleChange}
                required
                className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50 appearance-none"
              >
                <option value="" disabled>Select your city</option>
                {cities.map((city) => (
                  <option key={city.id} value={city.name}>
                    {city.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="pt-4 flex flex-col gap-3">
              <button
                type="submit"
                disabled={loading}
                className="w-full py-4 rounded-2xl font-extrabold text-white transition-all shadow-xl active:scale-95 bg-[#0071e3] hover:bg-[#0077ed]"
              >
                {loading ? "Saving..." : "Save Profile"}
              </button>

              <button
                type="button"
                onClick={handleSkip}
                className="w-full py-3 text-slate-400 hover:text-slate-600 font-bold transition-colors text-sm"
              >
                Skip for now
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
