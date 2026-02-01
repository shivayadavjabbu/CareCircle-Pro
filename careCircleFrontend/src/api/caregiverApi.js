import API_BASE_URL from "./api";

const getHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
};

async function handleResponse(response) {
  const text = await response.text();
  let data = null;
  try {
    data = JSON.parse(text);
  } catch (e) {
    // Not JSON
  }

  if (!response.ok) {
    if (response.status === 404) return null;
    const message = data?.message || data?.error || text || "Request failed";
    throw new Error(message);
  }

  return data || text;
}

export const createCaregiverProfile = async (data) => {
  const res = await fetch(`${API_BASE_URL}/user-profile-service/caregiver/profile`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
};

export const getCaregiverProfile = async () => {
  const res = await fetch(`${API_BASE_URL}/user-profile-service/caregiver/profile/me`, {
    method: "GET",
    headers: getHeaders(),
  });
  return handleResponse(res);
};

export const updateCaregiverProfile = async (data) => {
  const res = await fetch(`${API_BASE_URL}/user-profile-service/caregiver/profile`, {
    method: "PUT",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
};

export const addCapability = async (data) => {
  const res = await fetch(`${API_BASE_URL}/matching-booking-service/caregiver/capabilities`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
};

export const getCapabilities = async () => {
  const res = await fetch(`${API_BASE_URL}/matching-booking-service/caregiver/capabilities`, {
    method: "GET",
    headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
  });
  return handleResponse(res);
};

export const addCertification = async (data) => {
  const res = await fetch(`${API_BASE_URL}/matching-booking-service/caregiver/certifications`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
};

export const getCertifications = async () => {
  const res = await fetch(`${API_BASE_URL}/matching-booking-service/caregiver/certifications`, {
    method: "GET",
    headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
  });
  return handleResponse(res);
};
