import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token"); // Assuming token is stored as 'token'
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

export const createParentProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/profile`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || `Failed to create parent profile (${res.status})`);
    }
    return res.json();
};

export const getParentProfile = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/profile/me`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) throw new Error("Failed to fetch parent profile");
    return res.json();
};

export const addChild = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/children`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to add child");
    return res.json();
};

export const getChildren = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/children`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) throw new Error("Failed to fetch children");
    return res.json();
};

export const getVerifiedCaregivers = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/caregivers`, {
        method: "GET",
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }, // Only Auth header required for this GET
    });
    if (!res.ok) throw new Error("Failed to fetch caregivers");
    return res.json();
};

export const getCaregiverDetails = async (id) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/caregivers/${id}`, {
        method: "GET",
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
    if (!res.ok) throw new Error("Failed to fetch caregiver details");
    return res.json();
};
