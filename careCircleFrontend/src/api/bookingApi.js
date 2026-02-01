import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

export const updateCaregiverServices = async (data) => {
    const res = await fetch(`${API_BASE_URL}/caregiver/services`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    return res.json();
};

export const getCaregiverServices = async () => {
    const res = await fetch(`${API_BASE_URL}/caregiver/services`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const getCaregiverCertifications = async () => {
    const res = await fetch(`${API_BASE_URL}/caregiver/certifications`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const addCertification = async (data) => {
    const res = await fetch(`${API_BASE_URL}/caregiver/certifications`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    return res.json();
};

export const getCaregiverAvailability = async () => {
    const res = await fetch(`${API_BASE_URL}/caregiver/availability`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const updateCaregiverAvailability = async (data) => {
    const res = await fetch(`${API_BASE_URL}/caregiver/availability`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    return res.json();
};

export const createBooking = async (data) => {
    const res = await fetch(`${API_BASE_URL}/bookings`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(data),
    });
    return res.json();
};

export const getMyBookings = async () => {
    const res = await fetch(`${API_BASE_URL}/bookings/my`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const acceptBooking = async (id) => {
    const res = await fetch(`${API_BASE_URL}/caregiver/bookings/${id}/accept`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

export const rejectBooking = async (id) => {
    const res = await fetch(`${API_BASE_URL}/caregiver/bookings/${id}/reject`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

export const cancelBooking = async (id) => {
    const res = await fetch(`${API_BASE_URL}/bookings/${id}/cancel`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

export const completeBooking = async (id) => {
    const res = await fetch(`${API_BASE_URL}/bookings/${id}/complete`, {
        method: "POST",
        headers: getHeaders(),
    });
    return res.json();
};

export const rateBooking = async (id, rating, comment) => {
    const res = await fetch(`${API_BASE_URL}/bookings/${id}/rate`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ rating, comment }),
    });
    return res.json();
};

export const getActiveCaregiverBookings = async () => {
    const res = await fetch(`${API_BASE_URL}/caregiver/bookings/active`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const getPendingCaregiverBookings = async () => {
    const res = await fetch(`${API_BASE_URL}/caregiver/bookings/pending`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const getBookingsByStatus = async (status) => {
    const res = await fetch(`${API_BASE_URL}/bookings/${status.toLowerCase()}`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};
