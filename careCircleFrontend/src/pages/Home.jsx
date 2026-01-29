import { useNavigate } from "react-router-dom";
import heroImage from "../assets/happy_nanny_hero.png";

export default function Home() {
  const navigate = useNavigate();

  return (
    <div className="bg-slate-50 min-h-screen font-sans overflow-x-hidden">
      {/* Hero Section */}
      <section className="relative min-h-[90vh] flex items-center justify-center pt-20 pb-20 px-6 bg-gradient-to-br from-white via-slate-50 to-indigo-50/30">
        <div className="absolute inset-0 z-0 overflow-hidden pointer-events-none">
          <div className="absolute -top-[10%] -left-[10%] w-[40%] h-[40%] bg-indigo-200/20 blur-[120px] rounded-full animate-float"></div>
          <div className="absolute top-[20%] -right-[5%] w-[30%] h-[30%] bg-pink-200/20 blur-[100px] rounded-full animate-float" style={{ animationDelay: '-1.5s' }}></div>
        </div>

        <div className="container mx-auto grid md:grid-cols-2 gap-12 items-center relative z-10">
          <div className="text-left animate-fade-in-up">
            <span className="inline-block px-4 py-1.5 mb-6 text-sm font-bold tracking-wider text-indigo-600 uppercase bg-indigo-50 rounded-full">
              Trusted Childcare Solutions
            </span>
            <h1 className="text-5xl md:text-7xl font-extrabold text-slate-900 leading-[1.1] mb-6">
              Modern Care for <br />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-violet-600">
                Modern Families
              </span>
            </h1>
            <p className="text-lg text-slate-600 mb-10 max-w-lg leading-relaxed">
              Connect with top-rated, background-checked nannies and babysitters in your neighborhood. Simple, secure, and stress-free.
            </p>
            <div className="flex flex-wrap gap-4">
              <button
                onClick={() => navigate("/register-parent")}
                className="px-8 py-4 bg-indigo-600 text-white font-bold rounded-2xl shadow-xl shadow-indigo-200 hover:bg-indigo-700 hover:-translate-y-1 transition-all active:scale-95 btn-premium"
              >
                Hire a Nanny
              </button>
              <button
                onClick={() => navigate("/register-nanny")}
                className="px-8 py-4 bg-white text-slate-900 font-bold rounded-2xl border-2 border-slate-100 shadow-lg hover:border-slate-200 hover:-translate-y-1 transition-all active:scale-95 btn-premium"
              >
                Become a Sitter
              </button>
            </div>
          </div>

          <div className="relative animate-fade-in-up" style={{ animationDelay: '0.2s' }}>
            <div className="relative z-10 rounded-[2.5rem] overflow-hidden shadow-2xl transform hover:scale-[1.02] transition-transform duration-500">
              <img
                src={heroImage}
                alt="Happy Nanny"
                className="w-full h-auto object-cover"
              />
              <div className="absolute bottom-6 left-6 right-6 p-6 glass-card rounded-2xl border border-white/30">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-green-400 rounded-full flex items-center justify-center text-white text-xl">✓</div>
                  <div>
                    <p className="font-bold text-slate-900">100% Verified Care</p>
                    <p className="text-sm text-slate-600">Background checks completed</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-24 px-6 bg-white relative z-10">
        <div className="container mx-auto">
          <div className="text-center max-w-3xl mx-auto mb-16 animate-fade-in-up">
            <h2 className="text-3xl md:text-5xl font-extrabold text-slate-900 mb-6">Why CareCircle?</h2>
            <p className="text-slate-600 text-lg">We provide the most reliable and convenient platform for childcare services.</p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {[
              { title: "Safe & Secure", desc: "Rigorous background checks for every professional.", icon: "🛡️" },
              { title: "Easy Booking", desc: "Find and book near you in just a few clicks.", icon: "📱" },
              { title: "Reliable Care", desc: "Trusted by thousands of happy families.", icon: "❤️" },
            ].map((f, i) => (
              <div
                key={i}
                className="p-10 rounded-3xl bg-slate-50 border border-slate-100 hover:shadow-2xl hover:shadow-indigo-100 transition-all duration-300 hover:-translate-y-2 animate-fade-in-up"
                style={{ animationDelay: `${0.1 * (i + 1)}s` }}
              >
                <div className="text-4xl mb-6">{f.icon}</div>
                <h3 className="text-xl font-bold text-slate-900 mb-3">{f.title}</h3>
                <p className="text-slate-600 leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-24 px-6">
        <div className="container mx-auto max-w-5xl bg-indigo-600 rounded-[3rem] p-12 md:p-20 text-center text-white relative overflow-hidden shadow-2xl shadow-indigo-300 animate-fade-in-up">
          <div className="absolute top-0 right-0 w-64 h-64 bg-white/10 rounded-full -translate-y-1/2 translate-x-1/2 blur-3xl"></div>
          <div className="relative z-10">
            <h2 className="text-3xl md:text-5xl font-extrabold mb-8">Ready to find the perfect care?</h2>
            <p className="text-indigo-100 text-lg mb-12 max-w-2xl mx-auto">Join the CareCircle community today and experience the future of childcare services.</p>
            <div className="flex flex-wrap justify-center gap-6">
              <button
                onClick={() => navigate("/register-parent")}
                className="px-10 py-5 bg-white text-indigo-600 font-bold rounded-2xl hover:bg-slate-50 transition-all active:scale-95 shadow-xl"
              >
                Get Started Now
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
