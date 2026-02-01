import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

export const searchCaregivers = async (city = "", serviceId = "", page = 0, limit = 10) => {
    let url = `${API_BASE_URL}/matching/search?page=${page}&limit=${limit}`;
    if (city) url += `&city=${encodeURIComponent(city)}`;
    if (serviceId) url += `&serviceId=${serviceId}`;

    const res = await fetch(url, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) throw new Error("Failed to search caregivers");
    return res.json();
};

export const getActiveServices = async () => {
    const res = await fetch(`${API_BASE_URL}/services`, {
        method: "GET",
        headers: getHeaders(),
    });
    if (!res.ok) throw new Error("Failed to fetch services");
    return res.json();
};

export const createBooking = async (bookingData) => {
    const res = await fetch(`${API_BASE_URL}/bookings`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(bookingData),
    });
    if (!res.ok) throw new Error("Failed to create booking");
    return res.json();
};
