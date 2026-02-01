import { BrowserRouter, Routes, Route } from "react-router-dom";

// Layout
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

// 5 Core Pages
import Home from "./pages/Home";
import AuthPage from "./pages/AuthPage"; // Page 1
import ProfileSetupPage from "./pages/ProfileSetupPage"; // Page 2
import MainDashboard from "./pages/MainDashboard"; // Page 3
import BookingManagementPage from "./pages/BookingManagementPage"; // Page 4
import CommunicationPage from "./pages/CommunicationPage"; // Page 5
import NannyDashboard from "./pages/NannyDashboard";

// Auth Support
import VerifyAccount from "./pages/VerifyAccount";
import ForgotPassword from "./pages/ForgotPassword";
import UpdatePassword from "./pages/UpdatePassword";

// Legacy Pages (Restored)
import Login from "./pages/Login";
import Register from "./pages/Register";
import RegisterParent from "./pages/RegisterParent";
import RegisterNanny from "./pages/RegisterNanny";
import RegisterAdmin from "./pages/RegisterAdmin";
import ParentDashboard from "./pages/ParentDashboard";
import ParentProfile from "./pages/ParentProfile";
import NannyProfile from "./pages/NannyProfile";
import AdminDashboard from "./pages/AdminDashboard";
import AdminProfile from "./pages/AdminProfile";
import BabyDetails from "./pages/BabyDetails";
import AdminLogin from "./pages/AdminLogin";

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <div className="min-h-screen">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/auth" element={<AuthPage />} />
          <Route path="/login" element={<AuthPage />} />
          <Route path="/profile-setup" element={<ProfileSetupPage />} />
          <Route path="/dashboard" element={<MainDashboard />} />
          <Route path="/nanny-dashboard" element={<NannyDashboard />} />
          <Route path="/bookings" element={<BookingManagementPage />} />
          <Route path="/communication" element={<CommunicationPage />} />

          {/* Support */}
          <Route path="/verify-account" element={<VerifyAccount />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/update-password" element={<UpdatePassword />} />

          {/* Legacy Routes */}
          <Route path="/legacy/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/register-parent" element={<RegisterParent />} />
          <Route path="/register-nanny" element={<RegisterNanny />} />
          <Route path="/register-admin" element={<RegisterAdmin />} />
          <Route path="/parent-dashboard" element={<ParentDashboard />} />
          <Route path="/parent-profile" element={<ParentProfile />} />
          <Route path="/nanny-profile" element={<NannyProfile />} />
          <Route path="/admin-dashboard" element={<AdminDashboard />} />
          <Route path="/admin-profile" element={<AdminProfile />} />
          <Route path="/admin-login" element={<AdminLogin />} />
          <Route path="/baby-details" element={<BabyDetails />} />

          <Route path="*" element={<Home />} />
        </Routes>
      </div>
      <Footer />
    </BrowserRouter>
  );
}
