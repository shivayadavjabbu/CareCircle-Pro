import API_BASE_URL from "./api";

const getHeaders = () => {
    const token = localStorage.getItem("token");
    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
};

export const createChatRoom = async (bookingId) => {
    const res = await fetch(`${API_BASE_URL}/communication-service/chats/rooms`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ bookingId }),
    });
    return res.json();
};

export const getRoomMessages = async (roomId) => {
    const res = await fetch(`${API_BASE_URL}/chats/rooms/${roomId}/messages`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const sendMessage = async (roomId, content) => {
    const res = await fetch(`${API_BASE_URL}/chats/rooms/${roomId}/messages`, {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify({ content }),
    });
    return res.json();
};

export const getNotifications = async () => {
    const res = await fetch(`${API_BASE_URL}/communication-service/notifications`, {
        method: "GET",
        headers: getHeaders(),
    });
    return res.json();
};

export const markNotificationRead = async (id) => {
    const res = await fetch(`${API_BASE_URL}/notifications/${id}/read`, {
        method: "PATCH",
        headers: getHeaders(),
    });
    return res.json();
};
