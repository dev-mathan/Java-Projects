import axios from "axios";

// Backend base URL. Change it here, or set VITE_API_URL in a .env file.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
});

// Pull the readable error message sent by GlobalExceptionHandler.
export const errMsg = (e) =>
  e?.response?.data?.error || e?.message || "Something went wrong";

/* ---------- Users ---------- */
export const getUsers = () => api.get("/user");
export const createUser = (user) => api.post("/user", user);
export const updateUser = (uid, user) => api.put(`/user/${uid}`, user);
export const deleteUser = (uid) => api.delete(`/user/${uid}`);

/* ---------- Accounts ---------- */
export const getAccountsByUser = (uid) => api.get(`/account/user/${uid}`);
export const createAccount = (uid, account) =>
  api.post(`/account/${uid}`, account);
export const deleteAccount = (uid, accountId) =>
  api.delete(`/account/user/${uid}/account/${accountId}`);

/* ---------- Loans ---------- */
export const getLoansByUser = (uid) => api.get(`/loan/user/${uid}`);
export const createLoan = (uid, loan) => api.post(`/loan/${uid}`, loan);
export const updateLoanStatus = (loanId, status) =>
  api.patch(`/loan/${loanId}/status`, null, { params: { status } });
export const deleteLoan = (loanId) => api.delete(`/loan/${loanId}`);

/* ---------- Transactions ---------- */
export const createTransaction = (accountId, txn) =>
  api.post(`/transaction/account/${accountId}`, txn);
export const getTransactionsByUserPaged = (uid, page, size) =>
  api.get(`/transaction/user/${uid}`, { params: { page, size } });
export const getTransactionsByUserAndDate = (uid, start, end) =>
  api.get(`/transaction/user/${uid}/date-range`, { params: { start, end } });
export const deleteTransaction = (tid) => api.delete(`/transaction/${tid}`);

export default api;
