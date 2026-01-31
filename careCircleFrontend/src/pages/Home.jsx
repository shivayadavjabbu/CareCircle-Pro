import React from "react";
import { useNavigate } from "react-router-dom";
import heroImage from "../assets/happy_nanny_hero.png";

export default function Home() {
  const navigate = useNavigate();

  // Mock Data
  const nannies = [
    { id: 1, name: "Sarah M.", role: "Certified Nanny", rating: 4.9, price: 25, experience: "5 Years", badges: ["CPR Certified", "Newborn Care"] },
    { id: 2, name: "Jessica T.", role: "Babysitter", rating: 4.8, price: 18, experience: "3 Years", badges: ["Homework Help", "Pet Friendly"] },
    { id: 3, name: "Emily R.", role: "Housekeeper & Nanny", rating: 5.0, price: 30, experience: "7 Years", badges: ["Cooking", "Cleaning", "First Aid"] },
    { id: 4, name: "Michael B.", role: "Male Nanny", rating: 4.9, price: 22, experience: "4 Years", badges: ["Sports Coaching", "Tutoring"] },
    { id: 5, name: "David L.", role: "Special Needs Care", rating: 5.0, price: 35, experience: "8 Years", badges: ["Certified", "Therapy Support"] },
    { id: 6, name: "Amanda W.", role: "Au Pair", rating: 4.7, price: 20, experience: "2 Years", badges: ["Multi-lingual", "Driver"] },
  ];

  const scrollToNannies = () => {
    const section = document.getElementById("nanny-listings");
    if (section) {
      section.scrollIntoView({ behavior: "smooth" });
    }
  };

  return (
    <div className="min-h-screen bg-[#f5f5f7]">

      {/* Hero Section */}
      <div className="relative w-full h-[85vh] flex items-center justify-center text-center px-6">
        <div
          className="absolute inset-0 z-0 bg-cover bg-center"
          style={{ backgroundImage: `url(${heroImage})` }}
        >
          <div className="absolute inset-0 bg-black/30 backdrop-blur-[2px]"></div>
        </div>

        <div className="relative z-10 max-w-4xl mx-auto text-white">
          <h1 className="text-[64px] leading-[1.05] font-semibold tracking-tight mb-6 drop-shadow-lg">
            Care that feels like family.
          </h1>
          <p className="text-[24px] font-medium leading-relaxed max-w-2xl mx-auto mb-10 text-white/90 drop-shadow-md">
            Safe, reliable, and loving care for your little ones. Connected with the tap of a button.
          </p>
          <div className="flex justify-center gap-6">
            <button
              onClick={scrollToNannies}
              className="bg-white text-[#1d1d1f] text-[19px] font-medium px-8 py-4 rounded-full hover:bg-white/90 transition-all active:scale-95 shadow-lg"
            >
              Find a Caregiver
            </button>
            <button
              onClick={() => navigate("/register", { state: { role: "ROLE_CARETAKER" } })}
              className="bg-[#0071e3] text-white text-[19px] font-medium px-8 py-4 rounded-full hover:bg-[#0077ed] transition-all active:scale-95 shadow-lg"
            >
              Join as Caregiver
            </button>
          </div>
        </div>
      </div>

      {/* Listings Grid (Target for Scroll) */}
      <div id="nanny-listings" className="max-w-[1200px] mx-auto px-6 py-24 bg-[#f5f5f7]">
        <div className="flex justify-between items-end mb-12">
          <h2 className="text-[40px] font-semibold text-[#1d1d1f]">Verified Professionals</h2>
          <button onClick={() => navigate("/find-nanny")} className="text-[#0071e3] text-[17px] hover:underline">View all ›</button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {nannies.map((nanny) => (
            <div key={nanny.id} className="card-apple group cursor-pointer border border-transparent hover:border-[#0071e3]/30">
              <div className="flex justify-between items-start mb-6">
                <div>
                  <h3 className="text-[22px] font-semibold text-[#1d1d1f] leading-tight">{nanny.name}</h3>
                  <p className="text-[#86868b] font-medium mt-1">{nanny.role}</p>
                </div>
                <div className="flex items-center bg-[#f5f5f7] px-3 py-1.5 rounded-full">
                  <span className="text-[#f5a623] text-sm mr-1">★</span>
                  <span className="text-sm font-bold text-[#1d1d1f]">{nanny.rating}</span>
                </div>
              </div>
              <div className="grid grid-cols-2 gap-y-4 gap-x-6 text-[15px] mb-6">
                <div className="flex flex-col">
                  <span className="text-[#86868b] text-xs uppercase tracking-wide">Experience</span>
                  <span className="font-medium text-[#1d1d1f]">{nanny.experience}</span>
                </div>
                <div className="flex flex-col text-right">
                  <span className="text-[#86868b] text-xs uppercase tracking-wide">Rate</span>
                  <span className="font-medium text-[#1d1d1f]">${nanny.price}/hr</span>
                </div>
              </div>
              <div className="flex flex-wrap gap-2">
                {nanny.badges.map(badge => (
                  <span key={badge} className="text-[11px] font-medium bg-[#f5f5f7] text-[#1d1d1f] px-2.5 py-1 rounded-md">
                    {badge}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Stacking/Folding Cards Section - Rich White Theme */}
      <div className="bg-white py-24 border-t border-[#d2d2d7]/30">
        <div className="max-w-[700px] mx-auto px-6 text-center mb-20">
          <h2 className="text-[56px] font-semibold tracking-tight text-[#1d1d1f] mb-4">Why CareCircle?</h2>
          <p className="text-[24px] text-[#86868b]">Thoughtfully designed for peace of mind.</p>
        </div>

        <div className="max-w-[800px] mx-auto px-6 space-y-24 pb-32">

          {/* Card 1 */}
          <div className="sticky top-[120px] bg-[#fbfbfd] p-12 rounded-[40px] shadow-[0_20px_40px_-15px_rgba(0,0,0,0.1)] border border-[#d2d2d7]/50 transition-transform duration-500 ease-out origin-top hover:scale-[1.02]">
            <div className="flex flex-col md:flex-row items-center gap-10">
              <div className="w-20 h-20 bg-[#e3f2fd] rounded-3xl flex items-center justify-center text-4xl shrink-0 text-[#0071e3]">
                🛡️
              </div>
              <div>
                <h3 className="text-[32px] font-semibold text-[#1d1d1f] mb-4">Safety First</h3>
                <p className="text-[#86868b] text-[20px] leading-relaxed">
                  Every caregiver is thoroughly vetted. We perform background checks, identity verification, and continuous monitoring to ensure your family's safety.
                </p>
              </div>
            </div>
          </div>

          {/* Card 2 */}
          <div className="sticky top-[160px] bg-[#fbfbfd] p-12 rounded-[40px] shadow-[0_20px_40px_-15px_rgba(0,0,0,0.1)] border border-[#d2d2d7]/50 transition-transform duration-500 ease-out origin-top hover:scale-[1.02]">
            <div className="flex flex-col md:flex-row items-center gap-10">
              <div className="w-20 h-20 bg-[#e8f5e9] rounded-3xl flex items-center justify-center text-4xl shrink-0 text-[#34c759]">
                💳
              </div>
              <div>
                <h3 className="text-[32px] font-semibold text-[#1d1d1f] mb-4">Seamless Payments</h3>
                <p className="text-[#86868b] text-[20px] leading-relaxed">
                  Secure, cashless transactions. Handle hourly rates, tips, and reimbursements directly within the app. Simple, transparent, and fast.
                </p>
              </div>
            </div>
          </div>

          {/* Card 3 */}
          <div className="sticky top-[200px] bg-[#fbfbfd] p-12 rounded-[40px] shadow-[0_20px_40px_-15px_rgba(0,0,0,0.1)] border border-[#d2d2d7]/50 transition-transform duration-500 ease-out origin-top hover:scale-[1.02]">
            <div className="flex flex-col md:flex-row items-center gap-10">
              <div className="w-20 h-20 bg-[#f3e5f5] rounded-3xl flex items-center justify-center text-4xl shrink-0 text-[#bf5af2]">
                🤝
              </div>
              <div>
                <h3 className="text-[32px] font-semibold text-[#1d1d1f] mb-4">Perfect Match</h3>
                <p className="text-[#86868b] text-[20px] leading-relaxed">
                  We don't just show you lists. We connect you with caregivers who match your specific needs, values, and scheduling requirements.
                </p>
              </div>
            </div>
          </div>

        </div>
      </div>

    </div>
  );
}
