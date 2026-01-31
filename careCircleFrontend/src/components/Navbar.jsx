import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";

export default function Navbar() {
  const [isScrolled, setIsScrolled] = useState(false);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 10);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userEmail");
    navigate("/");
  };

  return (
    <nav
      className={`fixed top-0 w-full z-50 transition-all duration-300 ${isScrolled
        ? "bg-white/90 backdrop-blur-md border-b border-[#d2d2d7]"
        : "bg-white/80 backdrop-blur-md border-b border-transparent"
        }`}
    >
      <div className="max-w-[980px] mx-auto px-6 h-[48px] flex justify-between items-center text-xs">
        {/* Logo / Brand */}
        <Link to="/" className="flex items-center text-[#1d1d1f] hover:opacity-70 transition-opacity">
          <img src={logo} alt="CareCircle" className="h-4 w-4 mr-2" />
          <span className="font-semibold text-[17px] tracking-tight">CareCircle</span>
        </Link>

        {/* Desktop Menu */}
        <div className="hidden md:flex items-center space-x-8 text-[#1d1d1f] text-[12px] font-normal tracking-wide">
          <Link to="/" className="hover:text-[#0071e3] transition-colors">Home</Link>
          <Link to="/find-nanny" className="hover:text-[#0071e3] transition-colors">Find Care</Link>
          <Link to="/register" state={{ role: "ROLE_CARETAKER" }} className="hover:text-[#0071e3] transition-colors">Become a Caregiver</Link>

          {token ? (
            <div className="relative group cursor-pointer hover:text-[#0071e3] transition-colors">
              <span>Account</span>
              <div className="absolute right-0 top-full pt-2 hidden group-hover:block">
                <div className="bg-white rounded-lg shadow-apple-card border border-[#d2d2d7] py-2 w-48 flex flex-col items-start overflow-hidden">
                  <Link to={role === "ROLE_PARENT" ? "/parent-dashboard" : "/nanny-profile"} className="w-full px-4 py-2 hover:bg-[#f5f5f7] text-left">Dashboard</Link>
                  <button onClick={handleLogout} className="w-full px-4 py-2 hover:bg-[#f5f5f7] text-left text-red-500">Log Out</button>
                </div>
              </div>
            </div>
          ) : (
            <div className="flex items-center gap-4">
              <Link to="/login" className="hover:text-[#0071e3] transition-colors">Sign in</Link>
              <Link
                to="/register"
                state={{ role: "ROLE_PARENT" }}
                className="bg-[#0071e3] text-white px-3 py-1 rounded-full hover:bg-[#0077ed] transition-colors opacity-100 font-medium"
              >
                Sign Up
              </Link>
            </div>
          )}
        </div>

        {/* Mobile Menu Icon */}
        <button className="md:hidden text-[#1d1d1f]" aria-label="Menu">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
            <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
          </svg>
        </button>
      </div>
    </nav>
  );
}
