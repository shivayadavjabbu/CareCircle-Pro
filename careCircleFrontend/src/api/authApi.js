import API_BASE_URL from "./api";

async function handleResponse(response) {
    const text = await response.text();
    let data = null;
    try {
        data = JSON.parse(text);
    } catch (e) {
        // Not JSON
    }

    if (!response.ok) {
        const message = data?.message || data?.error || text || "Request failed";
        throw new Error(message);
    }

    return data || text;
}

export async function login(email, password, role) {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, role }),
    });
    return handleResponse(response);
}

export async function forgotPassword(email, role) {
    const response = await fetch(`${API_BASE_URL}/auth/forgot-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, role }),
    });
    return handleResponse(response);
}

export async function resetPassword(email, otp, newPassword, role) {
    const response = await fetch(`${API_BASE_URL}/auth/reset-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, otp, newPassword, role }),
    });
    return handleResponse(response);
}

export async function verifyAccount(email, otp, role) {
    const response = await fetch(`${API_BASE_URL}/auth/verify-account`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, otp, role }),
    });
    return handleResponse(response);
}

export async function register(email, password, role) {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, role }),
    });
    return handleResponse(response);
}
