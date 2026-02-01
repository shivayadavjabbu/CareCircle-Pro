import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/auth": "http://localhost:8080",
      "/caregiver": "http://localhost:8080",
      "/parents": "http://localhost:8080",
      "/admin": "http://localhost:8080",
      "/cities": "http://localhost:8085",
    },
  },
});
