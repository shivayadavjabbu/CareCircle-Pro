import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

// Parent APIs
export const createParentProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/profile`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to create parent profile");
    return res.json();
};

export const getParentProfile = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/profile/me`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) {
        if (res.status === 404) return null;
        throw new Error("Failed to fetch parent profile");
    }
    return res.json();
};

export const updateParentProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/parents/profile`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to update parent profile");
    return res.json();
};

// Children APIs
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

// Caregiver APIs
export const createCaregiverProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/caregiver/profile`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to create caregiver profile");
    return res.json();
};

export const getCaregiverProfile = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/caregiver/profile/me`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) {
        if (res.status === 404) return null;
        throw new Error("Failed to fetch caregiver profile");
    }
    return res.json();
};

export const updateCaregiverProfile = async (data) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/caregiver/profile`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to update caregiver profile");
    return res.json();
};

// Admin APIs
export const getAdminProfile = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/profile`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) {
        if (res.status === 404) return null;
        throw new Error("Failed to fetch admin profile");
    }
    return res.json();
};

export const getAdminStats = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/stats`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const getAdminParents = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/parents`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const getAdminCaregivers = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/caregivers`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const verifyCaregiver = async (id) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/caregiver/${id}/verify`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

export const rejectCaregiver = async (id) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/caregiver/${id}/reject`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

export const disableCaregiver = async (id) => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/admin/caregivers/${id}/disable`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

// Fetch cities for dropdown
export const getCities = async () => {
    const res = await fetch(`${API_BASE_URL}/user-profile-service/api/lookup/cities`, {
        method: "GET",
        headers: { "Content-Type": "application/json" }
    });
    if (!res.ok) throw new Error("Failed to fetch cities");
    return res.json();
};
