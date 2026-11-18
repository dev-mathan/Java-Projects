// "2026-10-12T11:45" (from datetime-local) -> "2026-10-12T11:45:00" (backend format)
export const toApiDateTime = (value) => {
  if (!value) return null;
  return value.length === 16 ? `${value}:00` : value;
};

export const formatDateTime = (value) =>
  value ? new Date(value).toLocaleString() : "-";

export const formatMoney = (value) =>
  Number(value ?? 0).toLocaleString("en-IN", {
    style: "currency",
    currency: "INR",
  });
