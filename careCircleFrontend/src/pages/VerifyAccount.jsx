import { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { verifyAccount } from "../api/authApi";
import logo from "../assets/logo.png";

export default function VerifyAccount() {
    const location = useLocation();
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [role, setRole] = useState("");
    const [otp, setOtp] = useState("");
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        if (location.state?.email && location.state?.role) {
            setEmail(location.state.email);
            setRole(location.state.role);
        } else {
            navigate("/login");
        }
    }, [location, navigate]);

    const handleVerify = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setMessage("");

        if (!otp) {
            setError("Please enter the verification code.");
            setLoading(false);
            return;
        }

        try {
            await verifyAccount(email, otp, role);
            setMessage("Verified!");
            setTimeout(() => {
                navigate("/login", { state: { email, role } });
            }, 1000);
        } catch (err) {
            setError(err.message || "Invalid code. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center pt-32 bg-[#f5f5f7] px-6">

            <div className="mb-10 text-center">
                <img src={logo} alt="Logo" className="h-12 w-12 mx-auto mb-4 opacity-80" />
                <h1 className="text-[28px] font-semibold text-[#1d1d1f]">Two-Factor Authentication</h1>
                <p className="text-[#86868b] mt-2 text-[17px]">
                    Enter the code sent to <span className="font-medium text-[#1d1d1f]">{email}</span>.
                </p>
            </div>

            <div className="w-full max-w-[440px] bg-white rounded-2xl p-10 shadow-sm border border-[#d2d2d7]">

                {error && (
                    <div className="mb-6 bg-[#fff2f2] p-3 rounded-xl border border-[#ff3b30]/20 text-[#ff3b30] text-sm font-medium text-center">
                        {error}
                    </div>
                )}

                {message && (
                    <div className="mb-6 bg-[#f2fff4] p-3 rounded-xl border border-[#34c759]/20 text-[#34c759] text-sm font-medium text-center">
                        {message}
                    </div>
                )}

                <form onSubmit={handleVerify} className="space-y-8">
                    <div>
                        <input
                            type="text"
                            value={otp}
                            onChange={(e) => setOtp(e.target.value)}
                            placeholder="Code"
                            className="input-apple text-center text-2xl tracking-widest font-mono"
                            maxLength={6}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="btn-apple-primary w-full py-3 text-[17px]"
                    >
                        {loading ? "Verifying..." : "Verify"}
                    </button>

                    <button
                        type="button"
                        onClick={() => navigate("/login")}
                        className="w-full text-center text-[#0071e3] text-sm hover:underline"
                    >
                        Back to Sign In
                    </button>
                </form>
            </div>
        </div>
    );
}
