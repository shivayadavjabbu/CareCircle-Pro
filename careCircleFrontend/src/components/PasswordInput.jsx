import React, { useState, useEffect } from "react";

const PasswordInput = ({
    id,
    name, // Added name prop
    value,
    onChange,
    placeholder = "Password",
    showStrengthMeter = true,
    className = "",
    ...props // Capture other props
}) => {
    const [showPassword, setShowPassword] = useState(false);
    const [strength, setStrength] = useState(0); // 0-4
    const [isFocused, setIsFocused] = useState(false);

    const calculateStrength = (pass) => {
        let score = 0;
        if (!pass) return 0;
        if (pass.length > 5) score += 1;
        if (pass.length > 8) score += 1;
        if (/[0-9]/.test(pass)) score += 1;
        if (/[^A-Za-z0-9]/.test(pass)) score += 1;
        return score;
    };

    useEffect(() => {
        setStrength(calculateStrength(value));
    }, [value]);

    const getStrengthInfo = () => {
        switch (strength) {
            case 0: return { label: "Enter password", color: "bg-gray-200", text: "text-gray-400" };
            case 1: return { label: "Weak", color: "bg-[#ff3b30]", text: "text-[#ff3b30]" };
            case 2: return { label: "Medium", color: "bg-[#ff9500]", text: "text-[#ff9500]" };
            case 3: return { label: "Strong", color: "bg-[#0071e3]", text: "text-[#0071e3]" };
            case 4: return { label: "Very Strong", color: "bg-[#34c759]", text: "text-[#34c759]" };
            default: return { label: "", color: "bg-gray-200", text: "text-gray-400" };
        }
    };

    const { label, color, text } = getStrengthInfo();

    return (
        <div className="w-full">
            {/* Input Wrapper */}
            <div
                className={`relative transition-all duration-300 ${className} ${isFocused ? "ring-4 ring-[#0071e3]/10 z-10" : ""
                    }`}
            >
                <input
                    id={id}
                    name={name} // Pass name prop
                    type={showPassword ? "text" : "password"}
                    value={value}
                    onChange={onChange}
                    onFocus={() => setIsFocused(true)}
                    onBlur={() => setIsFocused(false)}
                    placeholder={placeholder}
                    className="w-full h-[56px] px-4 text-[17px] bg-white bg-opacity-80 backdrop-blur-md outline-none text-[#1d1d1f] placeholder-[#86868b] rounded-xl transition-all"
                    required
                    {...props} // Spread remaining props
                />

                {/* Visual Toggle */}
                <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-4 top-1/2 -translate-y-1/2 p-2 rounded-full hover:bg-gray-100 transition-colors text-[#0071e3] font-medium text-sm"
                    tabIndex="-1"
                >
                    {showPassword ? "Hide" : "Show"}
                </button>
            </div>

            {/* Dynamic Strength Meter */}
            {showStrengthMeter && (
                <div className={`overflow-hidden transition-all duration-500 ease-in-out ${value ? "max-h-20 opacity-100 mt-3" : "max-h-0 opacity-0"}`}>

                    {/* Progress Bar (Segmented style) */}
                    <div className="flex gap-1 h-1.5 mb-2">
                        {[1, 2, 3, 4].map((step) => (
                            <div
                                key={step}
                                className={`flex-1 rounded-full transition-all duration-500 ${strength >= step ? color : "bg-gray-200"
                                    }`}
                            />
                        ))}
                    </div>

                    {/* Text Label */}
                    <div className="flex justify-between items-center px-1">
                        <span className={`text-xs font-semibold tracking-wide transition-colors duration-300 ${text}`}>
                            {label}
                        </span>
                        {strength === 4 && (
                            <span className="text-xs text-[#34c759] font-medium animate-pulse">✓ Perfect</span>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default PasswordInput;
