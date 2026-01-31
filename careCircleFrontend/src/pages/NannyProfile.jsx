import { useState, useEffect } from "react";
import {
  createCaregiverProfile,
  addCertification,
  addCapability,
  getCaregiverProfile
} from "../api/caregiverApi";
export default function NannyProfile() {
  const [message, setMessage] = useState("");

  /* ================= PROFILE ================= */
  const [profile, setProfile] = useState({
    fullName: "",
    phoneNumber: "",
    age: "",
    gender: "",
    address: "",
    city: "",
    bio: "",
    experienceYears: "",
  });

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getCaregiverProfile();
        if (data) {
          setProfile({
            ...data,
            age: data.age || "", // Ensure controlled component
            experienceYears: data.experienceYears || ""
          });
          console.log("Profile loaded");
        }
      } catch (err) {
        // Ignore error if profile doesn't exist yet
        console.log("No existing profile found or fetch error:", err);
      }
    };
    fetchProfile();
  }, []);

  /* ================= CERTIFICATION (TEXT ONLY) ================= */
  const [certification, setCertification] = useState({
    certificationName: "",
    issuedBy: "",
    validTill: "",
  });

  /* ================= CAPABILITY ================= */
  const [capability, setCapability] = useState({
    serviceType: "INFANT_CARE", // Matching enum from guide example usually, or keep simple
    description: "",
    minChildAge: 0,
    maxChildAge: 10,
    requiresCertification: true,
  });

  /* ================= HELPERS ================= */
  const handleChange = (e, setter, state) => {
    setter({ ...state, [e.target.name]: e.target.value });
  };

  /* ================= PROFILE SUBMIT ================= */
  const submitProfile = async () => {
    try {
      const payload = {
        fullName: profile.fullName.trim(),
        phoneNumber: profile.phoneNumber.trim(),
        age: Number(profile.age),
        gender: profile.gender.toUpperCase(),
        address: profile.address.trim(),
        city: profile.city.trim(),
        bio: profile.bio.trim(),
        experienceYears: Number(profile.experienceYears || 0),
      };

      await createCaregiverProfile(payload);
      setMessage("✅ Profile saved successfully! You can now add certifications and capabilities below.");
    } catch (error) {
      console.error("PROFILE ERROR:", error);
      setMessage("❌ " + (error.message || "Failed to save profile"));
    }
  };

  /* ================= CERTIFICATION SUBMIT ================= */
  const submitCertification = async () => {
    if (!certification.certificationName || !certification.issuedBy) {
      setMessage("❌ Please complete certification details");
      return;
    }

    try {
      const payload = {
        certificationName: certification.certificationName.trim(),
        issuedBy: certification.issuedBy.trim(),
        validTill: certification.validTill,
      };

      await addCertification(payload);
      setMessage("✅ Certification added successfully");

      // Reset
      setCertification({
        certificationName: "",
        issuedBy: "",
        validTill: "",
      });
    } catch (error) {
      console.error("CERTIFICATION ERROR:", error);
      setMessage("❌ " + (error.message || "Failed to add certification"));
    }
  };

  /* ================= CAPABILITY SUBMIT ================= */
  const submitCapability = async () => {
    try {
      await addCapability(capability);
      setMessage("✅ Capability added successfully");
    } catch (error) {
      console.error("CAPABILITY ERROR:", error);
      setMessage("❌ " + (error.message || "Failed to add capability"));
    }
  };

  /* ================= UI ================= */
  return (
    <div className="max-w-[700px] mx-auto mt-32 mb-10 p-8 bg-white rounded-2xl shadow-[0_10px_30px_rgba(0,0,0,0.12)] font-sans">
      <h2 className="text-center text-2xl font-bold text-gray-800 mb-6">Nanny / Caregiver Onboarding</h2>

      {message && <p className={`mb-5 text-center font-semibold ${message.includes("✅") ? "text-green-600" : "text-red-600"}`}>{message}</p>}

      {/* ================= PROFILE ================= */}
      <h3 className="mt-8 mb-3 text-lg font-bold text-gray-700 border-b-2 border-gray-100 pb-1.5">Profile Details</h3>

      <div className="flex flex-col gap-3.5">
        <input name="fullName" placeholder="Full Name *" value={profile.fullName}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <input name="phoneNumber" placeholder="Phone Number *" value={profile.phoneNumber}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <input type="number" name="age" placeholder="Age *" value={profile.age}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <select name="gender" value={profile.gender}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 bg-white"
        >
          <option value="">Select Gender *</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
          <option value="Other">Other</option>
        </select>

        <textarea name="address" placeholder="Address *" value={profile.address}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300 resize-y min-h-[80px]"
        />

        <input name="city" placeholder="City *" value={profile.city}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <input type="number" name="experienceYears" value={profile.experienceYears}
          placeholder="Experience (years)"
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <textarea name="bio" placeholder="Short Bio" value={profile.bio}
          onChange={(e) => handleChange(e, setProfile, profile)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300 resize-y min-h-[90px]"
        />

        <button onClick={submitProfile} className="w-full p-3.5 bg-gradient-to-r from-blue-600 to-blue-400 text-white font-bold rounded-xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-[0_8px_18px_rgba(49,130,206,0.35)] active:translate-y-0">
          Save Profile
        </button>
      </div>

      {/* ================= CERTIFICATION ================= */}
      <h3 className="mt-10 mb-3 text-lg font-bold text-gray-700 border-b-2 border-gray-100 pb-1.5">Certifications</h3>

      <div className="flex flex-col gap-3.5">
        <input
          name="certificationName"
          placeholder="Certification Name *"
          value={certification.certificationName}
          onChange={(e) => handleChange(e, setCertification, certification)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <input
          name="issuedBy"
          placeholder="Issued By *"
          value={certification.issuedBy}
          onChange={(e) => handleChange(e, setCertification, certification)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300"
        />

        <input
          type="date"
          name="validTill"
          value={certification.validTill}
          onChange={(e) => handleChange(e, setCertification, certification)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 bg-white"
        />

        <button onClick={submitCertification} className="w-full p-3.5 bg-gradient-to-r from-blue-600 to-blue-400 text-white font-bold rounded-xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-[0_8px_18px_rgba(49,130,206,0.35)] active:translate-y-0">
          Add Certification
        </button>
      </div>

      {/* ================= CAPABILITY ================= */}
      <h3 className="mt-10 mb-3 text-lg font-bold text-gray-700 border-b-2 border-gray-100 pb-1.5">Capabilities</h3>

      <div className="flex flex-col gap-3.5">
        <textarea
          name="description"
          placeholder="Describe your caregiving service"
          value={capability.description}
          onChange={(e) => handleChange(e, setCapability, capability)}
          className="w-full p-3 text-sm border-2 border-gray-100 rounded-xl transition-all duration-300 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 placeholder-gray-300 resize-y min-h-[90px]"
        />

        <button onClick={submitCapability} className="w-full p-3.5 bg-gradient-to-r from-blue-600 to-blue-400 text-white font-bold rounded-xl transition-all duration-300 hover:-translate-y-0.5 hover:shadow-[0_8px_18px_rgba(49,130,206,0.35)] active:translate-y-0">
          Add Capability
        </button>
      </div>
    </div>
  );
}
