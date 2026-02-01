import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { login, register } from "../api/authApi";
import { getParentProfile, getCaregiverProfile, getAdminProfile } from "../api/profileApi";
import { jwtDecode } from "jwt-decode";

export default function AuthPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const [isLogin, setIsLogin] = useState(location.state?.isLogin !== false);
    const [role, setRole] = useState(location.state?.role || "ROLE_PARENT");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        console.log("AuthPage: Location state received", location.state);
        if (location.state) {
            if (location.state.isLogin !== undefined) setIsLogin(location.state.isLogin);
            if (location.state.role) setRole(location.state.role);
        }
    }, [location.state]);

    const handleAuth = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        try {
            if (isLogin) {
                const data = await login(email, password, role);
                localStorage.setItem("token", data.accessToken);
                const decoded = jwtDecode(data.accessToken);
                localStorage.setItem("role", decoded.role || role);
                localStorage.setItem("userEmail", decoded.sub || email);

                let profile = null;
                console.log("Fetching profile for role:", role);
                if (role === "ROLE_PARENT") profile = await getParentProfile();
                else if (role === "ROLE_CARETAKER" || role === "ROLE_CAREGIVER") profile = await getCaregiverProfile();
                else if (role === "ROLE_ADMIN") profile = await getAdminProfile();

                console.log("Profile fetched:", profile);

                if (role === "ROLE_CARETAKER" || role === "ROLE_CAREGIVER") {
                    if (profile) navigate("/nanny-dashboard");
                    else navigate("/nanny-profile");
                } else if (role === "ROLE_ADMIN") {
                    navigate("/admin-dashboard");
                } else {
                    if (profile) navigate("/parent-dashboard");
                    else navigate("/profile-setup");
                }
            } else {
                await register(email, password, role);
                navigate("/verify-account", { state: { email, role } });
            }
        } catch (err) {
            setError(err.message || "Authentication failed");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen pt-28 flex items-center justify-center bg-slate-50 p-6">
            <div className="w-full max-w-md p-8 rounded-3xl shadow-xl bg-white border border-slate-100">
                <h2 className="text-3xl font-black text-slate-900 text-center mb-8">
                    {isLogin ? "Welcome Back" : "Join CareCircle"}
                </h2>

                <div className="flex p-1.5 bg-slate-100 rounded-2xl mb-8">
                    <button onClick={() => setRole("ROLE_PARENT")} className={`flex-1 py-2.5 rounded-xl text-sm font-bold transition-all ${role === "ROLE_PARENT" ? "bg-white text-indigo-600 shadow-sm" : "text-slate-500 hover:text-slate-700"}`}>Parent</button>
                    <button onClick={() => setRole("ROLE_CARETAKER")} className={`flex-1 py-2.5 rounded-xl text-sm font-bold transition-all ${role === "ROLE_CARETAKER" || role === "ROLE_CAREGIVER" ? "bg-white text-indigo-600 shadow-sm" : "text-slate-500 hover:text-slate-700"}`}>Caregiver</button>
                </div>

                <form onSubmit={handleAuth} className="space-y-6">
                    <div className="space-y-1">
                        <label className="text-xs font-bold text-slate-500 ml-1 uppercase">Email Address</label>
                        <input type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all" required />
                    </div>
                    <div className="space-y-1">
                        <label className="text-xs font-bold text-slate-500 ml-1 uppercase">Password</label>
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="w-full p-4 rounded-2xl bg-slate-50 border border-slate-100 focus:border-indigo-500 outline-none transition-all" required />
                    </div>

                    {error && <div className="p-4 bg-red-50 text-red-600 rounded-2xl text-sm font-bold flex items-center gap-2">⚠️ {error}</div>}

                    <button type="submit" disabled={loading} className="w-full py-4 bg-indigo-600 text-white rounded-2xl font-black shadow-xl shadow-indigo-200 hover:bg-indigo-700 active:scale-[0.98] transition-all disabled:opacity-50">
                        {loading ? "Processing..." : isLogin ? "Login Now" : "Create Account"}
                    </button>
                </form>

                <p className="mt-8 text-center text-slate-500 text-sm font-medium">
                    {isLogin ? "New to CareCircle?" : "Already have an account?"}{" "}
                    <button onClick={() => setIsLogin(!isLogin)} className="text-indigo-600 font-bold hover:underline underline-offset-4">
                        {isLogin ? "Sign up here" : "Login here"}
                    </button>
                </p>
            </div>
        </div>
    );
}
