import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/auth-service": {
        target: "http://127.0.0.1:8080",
        changeOrigin: true,
        secure: false,
      },
      "/user-profile-service": {
        target: "http://127.0.0.1:8080",
        changeOrigin: true,
        secure: false,
      },
      "/matching-booking-service": {
        target: "http://127.0.0.1:8080",
        changeOrigin: true,
        secure: false,
      },
      "/communication-service": {
        target: "http://127.0.0.1:8080",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
