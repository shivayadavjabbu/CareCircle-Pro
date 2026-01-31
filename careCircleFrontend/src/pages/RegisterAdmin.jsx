import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/authApi";
export default function RegisterAdmin() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(""); // Clear previous errors
        setSuccessMessage(""); // Clear previous success messages

        try {
            await register(email, password, "ROLE_ADMIN");

            setSuccessMessage("OTP Sent to email! Redirecting to verification...");
            setTimeout(() => navigate("/verify-account", { state: { email, role: "ROLE_ADMIN" } }), 1500);

        } catch (error) {
            console.error("Registration error:", error);
            const msg = error.message.toLowerCase();
            if (/exist|already|taken|registered|conflict/i.test(msg)) {
                setError("Email already exists. Please login.");
                setTimeout(() => navigate("/admin-login"), 2000);
            } else {
                setError(error.message || "Server error. Please try again later.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen pt-28 flex items-center justify-center bg-gradient-to-br from-[#fdfbfb] to-[#ebedee] p-5">
            <div className="bg-white w-full max-w-[420px] p-[30px] rounded-[14px] shadow-[0_12px_35px_rgba(0,0,0,0.18)]">
                <h2 className="text-center mb-5 text-[#2d3748] font-bold text-2xl">Register as Admin</h2>

                {error && (
                    <div className="text-center text-red-600 font-semibold mb-3 p-2 bg-red-50 rounded-lg animate-in fade-in slide-in-from-top-1 duration-300">
                        ⚠️ {error}
                    </div>
                )}

                {successMessage && (
                    <div className="text-center text-emerald-600 font-semibold mb-3 p-2 bg-emerald-50 rounded-lg animate-in fade-in slide-in-from-top-1 duration-300">
                        ✅ {successMessage}
                    </div>
                )}

                <form onSubmit={handleRegister}>
                    <input
                        className="w-full p-3 mb-[14px] rounded-lg border border-[#cbd5e0] text-sm focus:outline-none focus:border-[#4299e1] focus:ring-2 focus:ring-[#4299e1]/20"
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <input
                        className="w-full p-3 mb-[14px] rounded-lg border border-[#cbd5e0] text-sm focus:outline-none focus:border-[#4299e1] focus:ring-2 focus:ring-[#4299e1]/20"
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <button
                        className="w-full p-3 border-none rounded-lg bg-[#4299e1] text-white text-base font-semibold cursor-pointer transition-all duration-300 hover:bg-[#3182ce] disabled:opacity-70 disabled:cursor-not-allowed shadow-md"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? "Registering..." : "Register"}
                    </button>
                </form>

                <p className="mt-5 text-sm text-center text-gray-600">
                    Already have an account? <a href="/admin-login" className="text-[#4299e1] font-medium hover:underline">Login here</a>
                </p>
            </div>
        </div>
    );
}
