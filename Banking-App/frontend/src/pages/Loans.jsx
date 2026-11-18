import { useEffect, useState } from "react";
import {
  getLoansByUser,
  createLoan,
  updateLoanStatus,
  deleteLoan,
  errMsg,
} from "../api/api";
import { formatDateTime, formatMoney, toApiDateTime } from "../utils.js";
import Alert from "../components/Alert.jsx";
import UserSelect from "../components/UserSelect.jsx";

const STATUSES = ["PENDING", "APPROVED", "REJECTED", "DISBURSED", "CLOSED"];

const emptyForm = {
  amount: "",
  balance: "",
  applyDate: "",
  approvalData: "",
  disbursementDate: "",
  status: "PENDING",
};

export default function Loans() {
  const [uid, setUid] = useState("");
  const [loans, setLoans] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [alert, setAlert] = useState(null);

  const load = (userId = uid) => {
    if (!userId) {
      setLoans([]);
      return;
    }
    getLoansByUser(userId)
      .then((res) => setLoans(res.data))
      .catch((e) => setAlert({ type: "error", text: errMsg(e) }));
  };

  useEffect(() => {
    load(uid);
  }, [uid]);

  const change = (field) => (e) =>
    setForm({ ...form, [field]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    if (!uid) {
      setAlert({ type: "error", text: "Select a user first" });
      return;
    }
    const amount = Number(form.amount);
    try {
      await createLoan(uid, {
        amount,
        balance: form.balance === "" ? amount : Number(form.balance),
        applyDate: toApiDateTime(form.applyDate),
        approvalData: toApiDateTime(form.approvalData),
        disbursementDate: toApiDateTime(form.disbursementDate),
        status: form.status,
      });
      setAlert({ type: "success", text: "Loan created" });
      setForm(emptyForm);
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  const changeStatus = async (loan, status) => {
    try {
      await updateLoanStatus(loan.loanId, status);
      setAlert({ type: "success", text: `Loan #${loan.loanId} is now ${status}` });
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  const remove = async (loan) => {
    if (!window.confirm(`Delete loan #${loan.loanId}?`)) return;
    try {
      await deleteLoan(loan.loanId);
      setAlert({ type: "success", text: "Loan deleted" });
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  return (
    <>
      <h1>Loans</h1>
      <Alert alert={alert} />

      <section className="card">
        <h2>Add loan</h2>
        <form className="form-grid" onSubmit={submit}>
          <UserSelect value={uid} onChange={setUid} />
          <label className="field">
            <span>Amount</span>
            <input
              type="number"
              step="0.01"
              min="0"
              value={form.amount}
              onChange={change("amount")}
              required
            />
          </label>
          <label className="field">
            <span>Balance (defaults to amount)</span>
            <input
              type="number"
              step="0.01"
              min="0"
              value={form.balance}
              onChange={change("balance")}
            />
          </label>
          <label className="field">
            <span>Status</span>
            <select value={form.status} onChange={change("status")}>
              {STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Apply date</span>
            <input
              type="datetime-local"
              value={form.applyDate}
              onChange={change("applyDate")}
            />
          </label>
          <label className="field">
            <span>Approval date</span>
            <input
              type="datetime-local"
              value={form.approvalData}
              onChange={change("approvalData")}
            />
          </label>
          <label className="field">
            <span>Disbursement date</span>
            <input
              type="datetime-local"
              value={form.disbursementDate}
              onChange={change("disbursementDate")}
            />
          </label>
          <div className="actions">
            <button type="submit" className="btn primary">
              Create loan
            </button>
          </div>
        </form>
      </section>

      <section className="card">
        <h2>Loans of selected user</h2>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Amount</th>
                <th>Balance</th>
                <th>Applied</th>
                <th>Approved</th>
                <th>Disbursed</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {loans.length === 0 && (
                <tr>
                  <td colSpan="8" className="empty">
                    {uid ? "No loans for this user" : "Select a user"}
                  </td>
                </tr>
              )}
              {loans.map((l) => (
                <tr key={l.loanId}>
                  <td>{l.loanId}</td>
                  <td>{formatMoney(l.amount)}</td>
                  <td>{formatMoney(l.balance)}</td>
                  <td>{formatDateTime(l.applyDate)}</td>
                  <td>{formatDateTime(l.approvalData)}</td>
                  <td>{formatDateTime(l.disbursementDate)}</td>
                  <td>
                    <select
                      className={`status ${(l.status || "").toLowerCase()}`}
                      value={l.status || ""}
                      onChange={(e) => changeStatus(l, e.target.value)}
                    >
                      {!STATUSES.includes(l.status) && (
                        <option value={l.status || ""}>{l.status || "-"}</option>
                      )}
                      {STATUSES.map((s) => (
                        <option key={s} value={s}>
                          {s}
                        </option>
                      ))}
                    </select>
                  </td>
                  <td className="row-actions">
                    <button
                      className="btn small danger"
                      onClick={() => remove(l)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </>
  );
}
