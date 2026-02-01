import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Auth & User Profile Service (via Gateway)
      "/auth": "http://localhost:8080",
      "/caregiver": "http://localhost:8080",
      "/parents": "http://localhost:8080",
      "/admin": "http://localhost:8080",

      // Matching/Booking Service (via Gateway)
      "/cities": "http://localhost:8080",
      "/services": "http://localhost:8080",
      "/matching": "http://localhost:8080",
      "/bookings": "http://localhost:8080",

      // Communication Service (via Gateway)
      "/communication-service": "http://localhost:8080",
    },
  },
});
