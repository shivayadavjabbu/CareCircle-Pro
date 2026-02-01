import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

export const createAdminProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/admin/profile`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to create admin profile");
    return res.json();
};

export const getAdminProfile = async () => {
    const res = await fetch(`${API_BASE_URL}/admin/profile`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) throw new Error("Failed to fetch admin profile");
    return res.json();
};

export const updateAdminProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/admin/profile`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to update admin profile");
    return res.json();
};

export const verifyCaregiver = async (id, reason) => {
    const res = await fetch(`${API_BASE_URL}/admin/caregivers/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ reason }),
    });
    if (!res.ok) throw new Error("Failed to verify caregiver");
    return res.json();
};

export const verifyCapability = async (id, reason) => {
    const res = await fetch(`${API_BASE_URL}/admin/capabilities/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ reason }),
    });
    if (!res.ok) throw new Error("Failed to verify capability");
    return res.json();
};

export const verifyCertification = async (id, reason) => {
    const res = await fetch(`${API_BASE_URL}/admin/certifications/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ reason }),
    });
    if (!res.ok) throw new Error("Failed to verify certification");
    return res.json();
};
export const getAdminStatistics = async () => {
    const res = await fetch(`${API_BASE_URL}/admin/stats`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) throw new Error("Failed to fetch admin statistics");
    return res.json();
};
