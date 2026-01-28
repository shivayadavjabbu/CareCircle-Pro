export const isValidPassword = (password) => {
  if (!password) return false;

  const minLength = password.length >= 8;
  const numberCount = password.replace(/[^0-9]/g, "").length >= 2;
  const hasSpecialChar = /[!@#$%^&*]/.test(password);

  return minLength && numberCount && hasSpecialChar;
};
