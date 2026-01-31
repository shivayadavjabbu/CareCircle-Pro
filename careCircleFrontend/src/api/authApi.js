import API_BASE_URL from "./api";

export async function login(email, password, role) {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password, role }),
    });

    if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || "Login failed");
    }

    const data = await response.json();
    return data;
}

export async function forgotPassword(email, role) {
    const response = await fetch(`${API_BASE_URL}/auth/forgot-password`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, role }),
    });

    if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || "Failed to send reset link");
    }

    return await response.text();
}

export async function resetPassword(email, otp, newPassword, role) {
    const response = await fetch(`${API_BASE_URL}/auth/reset-password`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, otp, newPassword, role }),
    });

    if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || "Password reset failed");
    }


    return await response.text();
}

export async function verifyAccount(email, otp, role) {
    const response = await fetch(`${API_BASE_URL}/auth/verify-account`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, otp, role }),
    });

    if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || "Verification failed");
    }

    return await response.text();
}

export async function register(email, password, role) {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password, role }),
    });

    const text = await response.text();
    let data = null;
    try {
        data = JSON.parse(text);
    } catch (e) {
        // Response is plain text
    }

    if (!response.ok) {
        const errorMsg = data?.message || data?.error || text || "Registration failed";
        throw new Error(errorMsg);
    }

    return text;
}
