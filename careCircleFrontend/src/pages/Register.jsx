import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { isValidPassword } from "../utils/passwordValidation";

export default function Register() {
  const navigate = useNavigate();

  const location = useLocation();
  const [form, setForm] = useState({
    role: location.state?.role || "ROLE_PARENT",
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    phone: "",
    city: ""
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const roles = [
    { value: "ROLE_PARENT", label: "👨‍👩‍👧 Parent", description: "Looking for childcare services" },
    { value: "ROLE_CARETAKER", label: "👩‍⚕️ Nanny/Caregiver", description: "Offering childcare services" },
    { value: "ROLE_ADMIN", label: "🛡️ Admin", description: "Platform administrator" }
  ];

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess(false);

    if (!isValidPassword(form.password)) {
      setError("Password must be 8+ chars, with 2 numbers and 1 special symbol.");
      return;
    }

    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match!");
      return;
    }

    try {
      setLoading(true);
      const response = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          role: form.role,
          name: form.name,
          email: form.email,
          password: form.password,
          phone: form.phone,
          city: form.city
        })
      });

      if (!response.ok) {
        const text = await response.text();
        let errorMsg = text || "Registration failed";
        try {
          const data = JSON.parse(text);
          errorMsg = data.message || data.error || text || "Registration failed";
        } catch (e) { }

        // Check for existing email keywords or 409 status ONLY on failure
        const isExistingEmail = response.status === 409 ||
          /exist|already|taken|conflict/i.test(errorMsg);

        if (isExistingEmail) {
          setError("Email already exists. Please login.");
          setTimeout(() => navigate("/login", { state: { role: form.role } }), 2000);
          return;
        }

        throw new Error(errorMsg);
      }

      setSuccess(true);
      setTimeout(() => {
        navigate(form.role === "ROLE_PARENT" ? "/parent-profile" : "/nanny-profile");
      }, 1500);

    } catch (error) {
      console.error("Registration error:", error);
      const msg = error.message.toLowerCase();
      if (/exist|already|taken|conflict/i.test(msg)) {
        setError("Email already exists. Please login.");
        setTimeout(() => navigate("/login", { state: { role: form.role } }), 2000);
      } else {
        setError(error.message || "Registration failed. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  const selectedRole = roles.find(r => r.value === form.role);

  return (
    <div className="min-h-screen pt-28 flex items-center justify-center bg-gradient-to-br from-slate-50 via-white to-indigo-50/30 p-6 font-sans">
      <div className="absolute inset-0 z-0 overflow-hidden pointer-events-none">
        <div className="absolute top-[10%] right-[10%] w-[30%] h-[35%] bg-indigo-200/20 blur-[100px] rounded-full animate-float"></div>
        <div className="absolute bottom-[10%] left-[10%] w-[30%] h-[35%] bg-pink-200/20 blur-[100px] rounded-full animate-float" style={{ animationDelay: '-1.5s' }}></div>
      </div>

      <div className="relative z-10 w-full max-w-[540px] animate-fade-in-up">
        <div className="glass-card rounded-[2.5rem] p-10 md:p-12 border-white/50 shadow-2xl">
          <div className="text-center mb-10">
            <div className={`w-20 h-20 rounded-2xl flex items-center justify-center text-white text-3xl mx-auto mb-6 shadow-xl rotate-3 ${form.role === "parent" ? "bg-brand-parent shadow-brand-parent/20" : "bg-brand-nanny shadow-brand-nanny/20"
              }`}>C</div>
            <h2 className="text-3xl font-extrabold text-slate-900 mb-2">Join CareCircle</h2>
            <p className="text-slate-500 font-medium tracking-tight">Create your {form.role === "ROLE_PARENT" ? "parent" : "caregiver"} account</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-sm font-bold text-slate-700 ml-1 mb-2">Who are you?</label>
              <div className="flex justify-center p-1 bg-slate-100 rounded-xl mb-4">
                <button
                  type="button"
                  onClick={() => setForm({ ...form, role: "ROLE_PARENT" })}
                  className={`flex-1 py-2 rounded-lg text-sm font-bold transition-all ${form.role === "ROLE_PARENT" ? "bg-white shadow-sm text-slate-900" : "text-slate-400 hover:text-slate-600"}`}
                >
                  Parent
                </button>
                <button
                  type="button"
                  onClick={() => setForm({ ...form, role: "ROLE_CARETAKER" })}
                  className={`flex-1 py-2 rounded-lg text-sm font-bold transition-all ${form.role === "ROLE_CARETAKER" ? "bg-white shadow-sm text-slate-900" : "text-slate-400 hover:text-slate-600"}`}
                >
                  Caregiver
                </button>
                <button
                  type="button"
                  onClick={() => setForm({ ...form, role: "ROLE_ADMIN" })}
                  className={`flex-1 py-2 rounded-lg text-sm font-bold transition-all ${form.role === "ROLE_ADMIN" ? "bg-white shadow-sm text-slate-900" : "text-slate-400 hover:text-slate-600"}`}
                >
                  Admin
                </button>
              </div>
              {selectedRole && <p className="mt-2 text-[11px] text-slate-500 italic px-3 py-1.5 bg-slate-50 rounded-lg border-l-2 border-indigo-500">{selectedRole.description}</p>}
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 ml-1">Full Name</label>
              <input name="name" value={form.name} onChange={handleChange} placeholder="Enter your full name" required className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50" />
            </div>

            <div className="grid md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="block text-sm font-bold text-slate-700 ml-1">Email</label>
                <input name="email" type="email" value={form.email} onChange={handleChange} placeholder="name@example.com" required className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50" />
              </div>
              <div className="space-y-2">
                <label className="block text-sm font-bold text-slate-700 ml-1">Phone</label>
                <input name="phone" value={form.phone} onChange={handleChange} placeholder="+91 00000 00000" required className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50" />
              </div>
            </div>

            <div className="grid md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="block text-sm font-bold text-slate-700 ml-1">Password</label>
                <input name="password" type="password" value={form.password} onChange={handleChange} placeholder="••••••••" required className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50" />
              </div>
              <div className="space-y-2">
                <label className="block text-sm font-bold text-slate-700 ml-1">Confirm</label>
                <input name="confirmPassword" type="password" value={form.confirmPassword} onChange={handleChange} placeholder="••••••••" required className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50" />
              </div>
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 ml-1">City</label>
              <input name="city" value={form.city} onChange={handleChange} placeholder="Enter your city" required className="input-premium focus:border-indigo-500 focus:ring-indigo-500/10 bg-slate-50/50" />
            </div>

            {error && (
              <div className="p-4 bg-red-50 border border-red-100 rounded-2xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2">
                <span className="text-red-500 text-lg">⚠️</span>
                <p className="text-sm font-bold text-red-600">{error}</p>
              </div>
            )}

            {success && (
              <div className="p-4 bg-green-50 border border-green-100 rounded-2xl flex items-center gap-3 animate-in fade-in slide-in-from-top-2">
                <span className="text-green-500 text-lg">✅</span>
                <p className="text-sm font-bold text-green-600">Account created! Redirecting...</p>
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className={`w-full py-4 rounded-2xl font-extrabold text-white transition-all shadow-xl active:scale-95 btn-premium mt-4 ${form.role === "ROLE_PARENT" ? "bg-brand-parent hover:bg-brand-parent-dark shadow-brand-parent/20" : "bg-brand-nanny hover:bg-brand-nanny-dark shadow-brand-nanny/20"
                }`}
            >
              {loading ? "Processing..." : "Create Account"}
            </button>
          </form>

          <div className="mt-8 pt-6 border-t border-slate-100 text-center">
            <p className="text-slate-500 text-sm font-medium">
              Already have an account?{" "}
              <button onClick={() => navigate("/login", { state: { role: form.role } })} className="font-bold text-indigo-600 hover:underline">Sign In</button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
