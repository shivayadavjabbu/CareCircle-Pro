import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const [userRole, setUserRole] = useState(localStorage.getItem("role"));
  const [userEmail, setUserEmail] = useState(localStorage.getItem("userEmail") || "");

  useEffect(() => {
    // Sync auth state whenever the location changes (e.g., after login/logout)
    setIsLoggedIn(!!localStorage.getItem("token"));
    setUserRole(localStorage.getItem("role"));
    setUserEmail(localStorage.getItem("userEmail") || "");
  }, [location]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("adminProfileCreated");
    setIsLoggedIn(false);
    setUserRole(null);
    navigate("/");
  };

  const toggleDropdown = (name) => {
    setActiveDropdown(activeDropdown === name ? null : name);
  };

  return (
    <nav className="fixed top-0 left-0 right-0 z-[1000] transition-all duration-300 px-6 py-4">
      <div className="max-w-7xl mx-auto glass-card rounded-[2rem] px-8 py-3.5 flex justify-between items-center font-sans border-white/40">
        <div
          className="text-2xl font-black tracking-tighter text-slate-900 cursor-pointer select-none group flex items-center gap-2"
          onClick={() => navigate("/")}
        >
          <div className="w-8 h-8 bg-indigo-600 rounded-lg flex items-center justify-center text-white text-lg rotate-3 group-hover:rotate-0 transition-transform">C</div>
          CareCircle
        </div>

        <div className="flex gap-4 items-center ml-auto">
          {!isLoggedIn ? (
            <>
              {/* Log in Dropdown */}
              <div
                className="relative group"
                onMouseEnter={() => setActiveDropdown("login")}
                onMouseLeave={() => setActiveDropdown(null)}
              >
                <button
                  onClick={() => toggleDropdown("login")}
                  className="flex items-center gap-2 py-2.5 px-6 rounded-full text-sm font-bold text-slate-600 hover:text-indigo-600 hover:bg-indigo-50 transition-all cursor-pointer outline-none"
                >
                  Log in <span className={`text-[10px] opacity-50 transition-transform duration-300 ${activeDropdown === "login" ? "rotate-180" : ""}`}>▼</span>
                </button>

                <div className={`absolute top-full right-0 pt-2 w-full min-w-[200px] transition-all duration-300 z-[1001] ${activeDropdown === "login" ? "opacity-100 visible translate-y-0" : "opacity-0 invisible -translate-y-2 pointer-events-none"}`}>
                  <div className="bg-white/95 backdrop-blur-xl shadow-2xl rounded-2xl py-3 border border-slate-100 shadow-indigo-100/50">
                    <button
                      className="w-full text-left py-3 px-6 text-sm font-semibold text-slate-700 hover:bg-indigo-50 hover:text-indigo-600 transition-colors"
                      onClick={() => { navigate("/login", { state: { role: "ROLE_PARENT" } }); setActiveDropdown(null); }}
                    >
                      👨‍👩‍👧 Parent Portal
                    </button>
                    <button
                      className="w-full text-left py-3 px-6 text-sm font-semibold text-slate-700 hover:bg-indigo-50 hover:text-indigo-600 transition-colors"
                      onClick={() => { navigate("/login", { state: { role: "ROLE_CARETAKER" } }); setActiveDropdown(null); }}
                    >
                      👩‍⚕️ Caregiver Hub
                    </button>
                    <div className="h-px bg-slate-100 my-2 mx-4"></div>
                    <button
                      className="w-full text-left py-3 px-6 text-sm font-semibold text-slate-700 hover:bg-slate-50 hover:text-slate-900 transition-colors"
                      onClick={() => { navigate("/admin-login"); setActiveDropdown(null); }}
                    >
                      🛡️ Administrator
                    </button>
                  </div>
                </div>
              </div>

              {/* Sign up Dropdown */}
              <div
                className="relative group"
                onMouseEnter={() => setActiveDropdown("signup")}
                onMouseLeave={() => setActiveDropdown(null)}
              >
                <button
                  onClick={() => toggleDropdown("signup")}
                  className="flex items-center gap-2 py-3 px-8 rounded-full bg-slate-900 text-white text-sm font-bold hover:bg-indigo-600 hover:-translate-y-0.5 hover:shadow-xl hover:shadow-indigo-200 transition-all active:scale-95 cursor-pointer outline-none"
                >
                  Get Started <span className={`text-[10px] opacity-50 transition-transform duration-300 ml-1 ${activeDropdown === "signup" ? "rotate-180" : ""}`}>▼</span>
                </button>

                <div className={`absolute top-full right-0 pt-2 w-full min-w-[220px] transition-all duration-300 z-[1001] ${activeDropdown === "signup" ? "opacity-100 visible translate-y-0" : "opacity-0 invisible -translate-y-2 pointer-events-none"}`}>
                  <div className="bg-white/95 backdrop-blur-xl shadow-2xl rounded-2xl py-3 border border-slate-100 shadow-indigo-100/50">
                    <button
                      className="w-full text-left py-4 px-6 group/item"
                      onClick={() => { navigate("/register-parent"); setActiveDropdown(null); }}
                    >
                      <div className="font-bold text-slate-900 group-hover/item:text-indigo-600 transition-colors">I'm a Parent</div>
                      <div className="text-[11px] text-slate-500">I need childcare services</div>
                    </button>
                    <button
                      className="w-full text-left py-4 px-6 group/item"
                      onClick={() => { navigate("/register-nanny"); setActiveDropdown(null); }}
                    >
                      <div className="font-bold text-slate-900 group-hover/item:text-indigo-600 transition-colors">I'm a Nanny</div>
                      <div className="text-[11px] text-slate-500">I want to provide care</div>
                    </button>
                  </div>
                </div>
              </div>
            </>
          ) : (
            <>
              {userEmail && (
                <span className="hidden md:inline-block text-xs font-semibold text-slate-500 bg-slate-100/50 px-3 py-1.5 rounded-full border border-slate-200/50">
                  {userEmail}
                </span>
              )}
              <button
                onClick={() => {
                  if (userRole === "ROLE_PARENT") navigate("/parent-dashboard");
                  else if (userRole === "ROLE_CARETAKER") navigate("/nanny-profile");
                  else if (userRole === "ROLE_ADMIN") navigate("/admin-dashboard");
                }}
                className="py-2.5 px-6 rounded-full text-sm font-bold text-slate-600 hover:text-indigo-600 hover:bg-indigo-50 transition-all cursor-pointer outline-none"
              >
                Dashboard
              </button>
              <button
                onClick={handleLogout}
                className="py-2.5 px-6 rounded-full bg-red-50 text-red-600 text-sm font-bold hover:bg-red-100 transition-all active:scale-95 cursor-pointer outline-none"
              >
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
