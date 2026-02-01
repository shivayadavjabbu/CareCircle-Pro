import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

export const createAdminProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/profile`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to create admin profile");
    return res.json();
};

export const verifyCaregiver = async (id, reason) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/caregivers/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ reason }),
    });
    if (!res.ok) throw new Error("Failed to verify caregiver");
    return res.json();
};

export const verifyCapability = async (id, reason) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/capabilities/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ reason }),
    });
    if (!res.ok) throw new Error("Failed to verify capability");
    return res.json();
};

export const verifyCertification = async (id, reason) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/certifications/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ reason }),
    });
    if (!res.ok) throw new Error("Failed to verify certification");
    return res.json();
};
