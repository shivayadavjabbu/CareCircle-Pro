import React, { useState, useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const [activeDropdown, setActiveDropdown] = useState(null);
  const navRef = useRef(null);

  useEffect(() => {
    setIsLoggedIn(!!localStorage.getItem("token"));
    setActiveDropdown(null);
  }, [location]);

  // Global click listener to close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (navRef.current && !navRef.current.contains(event.target)) {
        setActiveDropdown(null);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    setIsLoggedIn(false);
    navigate("/");
  };

  const navToAuth = (e, isLogin, role) => {
    e.preventDefault();
    e.stopPropagation();
    console.log("Navigating to auth", { isLogin, role });
    navigate("/auth", { state: { isLogin, role } });
    setActiveDropdown(null);
  };

  const toggleDropdown = (e, name) => {
    e.preventDefault();
    e.stopPropagation();
    setActiveDropdown(activeDropdown === name ? null : name);
  };

  return (
    <nav ref={navRef} className="fixed top-6 left-0 right-0 z-[1000] px-6">
      <div className="max-w-7xl mx-auto p-4 bg-white/90 backdrop-blur-2xl rounded-[2.5rem] border border-white/40 shadow-2xl flex justify-between items-center px-10">
        <div className="text-2xl font-black text-slate-900 cursor-pointer flex items-center gap-2 group" onClick={() => navigate("/")}>
          <div className="w-10 h-10 bg-indigo-600 rounded-2xl flex items-center justify-center text-white text-xl rotate-3 group-hover:rotate-0 transition-all duration-300 shadow-xl shadow-indigo-200">C</div>
          <span className="tracking-tighter font-black">CareCircle</span>
        </div>

        <div className="flex gap-4 items-center">
          {!isLoggedIn ? (
            <div className="flex gap-2">
              {/* Login Dropdown */}
              <div className="relative">
                <button
                  onClick={(e) => toggleDropdown(e, 'login')}
                  className={`py-3.5 px-8 rounded-full text-sm font-black transition-all ${activeDropdown === 'login' ? 'bg-indigo-50 text-indigo-600' : 'text-slate-700 hover:bg-slate-50'}`}
                >
                  Login
                </button>
                {activeDropdown === 'login' && (
                  <div className="absolute top-full mt-4 left-0 w-52 bg-white rounded-[2rem] shadow-2xl border border-slate-100 py-4 z-[1001] animate-in fade-in slide-in-from-top-2 duration-200">
                    <button onClick={(e) => navToAuth(e, true, "ROLE_PARENT")} className="w-full text-left px-8 py-3 text-sm font-bold text-slate-600 hover:bg-indigo-50 hover:text-indigo-600 transition-colors">Parent Login</button>
                    <button onClick={(e) => navToAuth(e, true, "ROLE_CARETAKER")} className="w-full text-left px-8 py-3 text-sm font-bold text-slate-600 hover:bg-indigo-50 hover:text-indigo-600 transition-colors">Caregiver Login</button>
                    <button onClick={(e) => navToAuth(e, true, "ROLE_ADMIN")} className="w-full text-left px-8 py-3 text-sm font-bold text-slate-600 hover:bg-indigo-50 hover:text-indigo-600 transition-colors">Admin Login</button>
                  </div>
                )}
              </div>

              {/* Sign Up Dropdown */}
              <div className="relative">
                <button
                  onClick={(e) => toggleDropdown(e, 'signup')}
                  className={`py-3.5 px-8 rounded-full text-sm font-black transition-all shadow-xl ${activeDropdown === 'signup' ? 'bg-indigo-700 text-white scale-95' : 'bg-slate-900 text-white hover:bg-indigo-600 shadow-indigo-100'}`}
                >
                  Sign Up
                </button>
                {activeDropdown === 'signup' && (
                  <div className="absolute top-full mt-4 right-0 w-72 bg-white rounded-[2rem] shadow-2xl border border-slate-100 py-6 z-[1001] animate-in fade-in slide-in-from-top-2 duration-200">
                    <button onClick={(e) => navToAuth(e, false, "ROLE_PARENT")} className="w-full text-left px-8 py-4 group hover:bg-slate-50 transition-colors">
                      <div className="font-black text-slate-900 group-hover:text-indigo-600 transition-colors">I'm a Parent</div>
                      <div className="text-[10px] text-slate-400 font-bold uppercase tracking-widest mt-0.5">Looking for child care</div>
                    </button>
                    <button onClick={(e) => navToAuth(e, false, "ROLE_CARETAKER")} className="w-full text-left px-8 py-4 group hover:bg-slate-50 transition-colors">
                      <div className="font-black text-slate-900 group-hover:text-indigo-600 transition-colors">I'm a Caregiver</div>
                      <div className="text-[10px] text-slate-400 font-bold uppercase tracking-widest mt-0.5">Offering care services</div>
                    </button>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className="flex gap-4">
              <button
                onClick={() => {
                  const role = localStorage.getItem("role");
                  if (role === "ROLE_CARETAKER") navigate("/caretaker-dashboard");
                  else if (role === "ROLE_PARENT") navigate("/parent-dashboard");
                  else if (role === "ROLE_ADMIN") navigate("/admin-dashboard");
                  else navigate("/dashboard");
                }}
                className="py-3 px-6 text-sm font-black text-slate-500 hover:text-indigo-600 transition-all"
              >
                Dashboard
              </button>
              <button onClick={() => navigate("/bookings")} className="py-3 px-6 text-sm font-black text-slate-500 hover:text-indigo-600 transition-all">Bookings</button>
              <button onClick={() => navigate("/communication")} className="py-3 px-6 text-sm font-black text-slate-500 hover:text-indigo-600 transition-all">Inbox</button>
              <button onClick={handleLogout} className="py-3 px-8 rounded-full bg-red-50 text-red-600 text-sm font-black hover:bg-red-100 transition-all">Logout</button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
