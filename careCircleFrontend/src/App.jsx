import { BrowserRouter, Routes, Route } from "react-router-dom";

// Layout
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

// Pages
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import RegisterParent from "./pages/RegisterParent";
import RegisterNanny from "./pages/RegisterNanny";
import RegisterAdmin from "./pages/RegisterAdmin";
import ParentDashboard from "./pages/ParentDashboard";
import ParentProfile from "./pages/ParentProfile";
import BabyDetails from "./pages/BabyDetails";
import ForgotPassword from "./pages/ForgotPassword";
import UpdatePassword from "./pages/UpdatePassword";
import VerifyAccount from "./pages/VerifyAccount";
import AdminLogin from "./pages/AdminLogin";
import AdminDashboard from "./pages/AdminDashboard";
import AdminProfile from "./pages/AdminProfile";
import ManageCaregivers from "./pages/ManageCaregivers";
import ManageBookings from "./pages/ManageBookings";
import ManageServices from "./pages/ManageServices";

// New Nanny pages
import NannyProfile from "./pages/NannyProfile";
import CaregiverDashboard from "./pages/CaregiverDashboard";
// import NannyQualification from "./pages/NannyQualification";
// import NannyCertification from "./pages/NannyCertification";

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        {/* Home */}
        <Route path="/" element={<Home />} />

        {/* Auth */}
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/update-password" element={<UpdatePassword />} />
        <Route path="/verify-account" element={<VerifyAccount />} />

        {/* Registration */}
        <Route path="/register" element={<Register />} />
        <Route path="/register-parent" element={<RegisterParent />} />
        <Route path="/register-nanny" element={<RegisterNanny />} />
        <Route path="/register-admin" element={<RegisterAdmin />} />

        {/* Parent flow */}
        <Route path="/parent-dashboard" element={<ParentDashboard />} />
        <Route path="/parent-profile" element={<ParentProfile />} />
        <Route path="/baby-details" element={<BabyDetails />} />

        {/* Admin flow */}
        <Route path="/admin-login" element={<AdminLogin />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
        <Route path="/admin-profile" element={<AdminProfile />} />
        <Route path="/admin/nannies" element={<ManageCaregivers />} />
        <Route path="/admin/bookings" element={<ManageBookings />} />
        <Route path="/admin/services" element={<ManageServices />} />

        {/* Nanny onboarding flow */}
        <Route path="/caregiver-dashboard" element={<CaregiverDashboard />} />
        <Route path="/nanny-profile" element={<NannyProfile />} />
        {/* <Route path="/nanny-qualification" element={<NannyQualification />} />
        <Route path="/nanny-certification" element={<NannyCertification />} /> */}

        {/* Fallback: redirect unknown paths to home */}
        <Route path="*" element={<Home />} />
      </Routes>

      <Footer />
    </BrowserRouter>
  );
}
