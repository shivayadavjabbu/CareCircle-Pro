import API_BASE_URL from "./api";

export const getActiveCities = async () => {
    console.log("Fetching cities from:", `${API_BASE_URL}/cities`);
    const res = await fetch(`${API_BASE_URL}/cities`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (!res.ok) {
        console.error("Cities fetch failed:", res.status, res.statusText);
        throw new Error("Failed to fetch cities");
    }
    const data = await res.json();
    console.log("Cities fetched:", data);
    return data;
};
