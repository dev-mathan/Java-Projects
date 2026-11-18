import { useEffect, useState } from "react";
import {
  getAccountsByUser,
  createTransaction,
  getTransactionsByUserPaged,
  getTransactionsByUserAndDate,
  deleteTransaction,
  errMsg,
} from "../api/api";
import { formatDateTime, formatMoney, toApiDateTime } from "../utils.js";
import Alert from "../components/Alert.jsx";
import UserSelect from "../components/UserSelect.jsx";

const TYPES = ["DEPOSIT", "WITHDRAW", "TRANSFER"];
const PAGE_SIZE = 10;

export default function Transactions() {
  const [alert, setAlert] = useState(null);

  /* ----- add transaction ----- */
  const [addUid, setAddUid] = useState("");
  const [accounts, setAccounts] = useState([]);
  const [form, setForm] = useState({
    accountId: "",
    amount: "",
    type: "DEPOSIT",
    date: "",
  });

  /* ----- view transactions ----- */
  const [viewUid, setViewUid] = useState("");
  const [mode, setMode] = useState("paged"); // "paged" | "range"
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");
  const [rows, setRows] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    setForm((f) => ({ ...f, accountId: "" }));
    if (!addUid) {
      setAccounts([]);
      return;
    }
    getAccountsByUser(addUid)
      .then((res) => setAccounts(res.data))
      .catch((e) => setAlert({ type: "error", text: errMsg(e) }));
  }, [addUid]);

  const loadPaged = (uid = viewUid, p = page) => {
    if (!uid) {
      setRows([]);
      setTotalPages(0);
      return;
    }
    getTransactionsByUserPaged(uid, p, PAGE_SIZE)
      .then((res) => {
        setRows(res.data.content || []);
        setTotalPages(res.data.totalPages ?? 0);
      })
      .catch((e) => setAlert({ type: "error", text: errMsg(e) }));
  };

  const loadRange = () => {
    if (!viewUid) {
      setAlert({ type: "error", text: "Select a user to view" });
      return;
    }
    if (!start || !end) {
      setAlert({ type: "error", text: "Choose both start and end date" });
      return;
    }
    getTransactionsByUserAndDate(
      viewUid,
      toApiDateTime(start),
      toApiDateTime(end)
    )
      .then((res) => {
        setRows(res.data);
        setTotalPages(0);
      })
      .catch((e) => setAlert({ type: "error", text: errMsg(e) }));
  };

  useEffect(() => {
    if (mode === "paged") loadPaged(viewUid, page);
  }, [viewUid, page, mode]);

  const refresh = () => (mode === "paged" ? loadPaged() : loadRange());

  const submit = async (e) => {
    e.preventDefault();
    if (!form.accountId) {
      setAlert({ type: "error", text: "Select a user and an account" });
      return;
    }
    try {
      await createTransaction(form.accountId, {
        amount: Number(form.amount),
        type: form.type,
        date: toApiDateTime(form.date), // null -> backend uses current time
      });
      setAlert({ type: "success", text: "Transaction added" });
      setForm({ ...form, amount: "", date: "" });
      refresh();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  const remove = async (t) => {
    if (!window.confirm(`Delete transaction #${t.tid}?`)) return;
    try {
      await deleteTransaction(t.tid);
      setAlert({ type: "success", text: "Transaction deleted" });
      refresh();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  const showAll = () => {
    setMode("paged");
    setPage(0);
  };

  return (
    <>
      <h1>Transactions</h1>
      <Alert alert={alert} />

      <section className="card">
        <h2>Add transaction</h2>
        <form className="form-grid" onSubmit={submit}>
          <UserSelect value={addUid} onChange={setAddUid} />
          <label className="field">
            <span>Account</span>
            <select
              value={form.accountId}
              onChange={(e) => setForm({ ...form, accountId: e.target.value })}
            >
              <option value="">-- select account --</option>
              {accounts.map((a) => (
                <option key={a.a_id} value={a.a_id}>
                  {a.a_id} - {a.accountNo}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Type</span>
            <select
              value={form.type}
              onChange={(e) => setForm({ ...form, type: e.target.value })}
            >
              {TYPES.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Amount</span>
            <input
              type="number"
              step="0.01"
              min="0.01"
              value={form.amount}
              onChange={(e) => setForm({ ...form, amount: e.target.value })}
              required
            />
          </label>
          <label className="field">
            <span>Date (optional, defaults to now)</span>
            <input
              type="datetime-local"
              value={form.date}
              onChange={(e) => setForm({ ...form, date: e.target.value })}
            />
          </label>
          <div className="actions">
            <button type="submit" className="btn primary">
              Add transaction
            </button>
          </div>
        </form>
      </section>

      <section className="card">
        <h2>View transactions by user</h2>
        <div className="form-grid">
          <UserSelect
            value={viewUid}
            onChange={(v) => {
              setViewUid(v);
              setPage(0);
            }}
          />
          <label className="field">
            <span>From</span>
            <input
              type="datetime-local"
              value={start}
              onChange={(e) => setStart(e.target.value)}
            />
          </label>
          <label className="field">
            <span>To</span>
            <input
              type="datetime-local"
              value={end}
              onChange={(e) => setEnd(e.target.value)}
            />
          </label>
          <div className="actions">
            <button
              className="btn primary"
              onClick={() => {
                setMode("range");
                loadRange();
              }}
            >
              Filter by date
            </button>
            <button className="btn" onClick={showAll}>
              Show all
            </button>
          </div>
        </div>

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Account</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Date</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {rows.length === 0 && (
                <tr>
                  <td colSpan="6" className="empty">
                    {viewUid ? "No transactions found" : "Select a user"}
                  </td>
                </tr>
              )}
              {rows.map((t) => (
                <tr key={t.tid}>
                  <td>{t.tid}</td>
                  <td>{t.account?.accountNo ?? "-"}</td>
                  <td>
                    <span className={`badge ${(t.type || "").toLowerCase()}`}>
                      {t.type}
                    </span>
                  </td>
                  <td>{formatMoney(t.amount)}</td>
                  <td>{formatDateTime(t.date)}</td>
                  <td className="row-actions">
                    <button
                      className="btn small danger"
                      onClick={() => remove(t)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {mode === "paged" && totalPages > 1 && (
          <div className="pager">
            <button
              className="btn small"
              disabled={page === 0}
              onClick={() => setPage(page - 1)}
            >
              Previous
            </button>
            <span>
              Page {page + 1} of {totalPages}
            </span>
            <button
              className="btn small"
              disabled={page + 1 >= totalPages}
              onClick={() => setPage(page + 1)}
            >
              Next
            </button>
          </div>
        )}
      </section>
    </>
  );
}
