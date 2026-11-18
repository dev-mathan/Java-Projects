import { useEffect, useState } from "react";
import {
  getAccountsByUser,
  createAccount,
  deleteAccount,
  errMsg,
} from "../api/api";
import { formatMoney } from "../utils.js";
import Alert from "../components/Alert.jsx";
import UserSelect from "../components/UserSelect.jsx";

const emptyForm = { accountNo: "", balance: "" };

export default function Accounts() {
  const [uid, setUid] = useState("");
  const [accounts, setAccounts] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [alert, setAlert] = useState(null);

  const load = (userId = uid) => {
    if (!userId) {
      setAccounts([]);
      return;
    }
    getAccountsByUser(userId)
      .then((res) => setAccounts(res.data))
      .catch((e) => setAlert({ type: "error", text: errMsg(e) }));
  };

  useEffect(() => {
    load(uid);
  }, [uid]);

  const submit = async (e) => {
    e.preventDefault();
    if (!uid) {
      setAlert({ type: "error", text: "Select a user first" });
      return;
    }
    try {
      await createAccount(uid, {
        accountNo: form.accountNo,
        balance: Number(form.balance || 0),
      });
      setAlert({ type: "success", text: "Account created" });
      setForm(emptyForm);
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  const remove = async (a) => {
    if (!window.confirm(`Delete account ${a.accountNo}?`)) return;
    try {
      await deleteAccount(uid, a.a_id);
      setAlert({ type: "success", text: "Account deleted" });
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  return (
    <>
      <h1>Accounts</h1>
      <Alert alert={alert} />

      <section className="card">
        <h2>Add account</h2>
        <form className="form-grid" onSubmit={submit}>
          <UserSelect value={uid} onChange={setUid} />
          <label className="field">
            <span>Account number</span>
            <input
              value={form.accountNo}
              onChange={(e) => setForm({ ...form, accountNo: e.target.value })}
              placeholder="ACC1001"
              required
            />
          </label>
          <label className="field">
            <span>Opening balance</span>
            <input
              type="number"
              step="0.01"
              min="0"
              value={form.balance}
              onChange={(e) => setForm({ ...form, balance: e.target.value })}
              placeholder="5000"
              required
            />
          </label>
          <div className="actions">
            <button type="submit" className="btn primary">
              Create account
            </button>
          </div>
        </form>
      </section>

      <section className="card">
        <h2>Accounts of selected user</h2>
        <table>
          <thead>
            <tr>
              <th>Account ID</th>
              <th>Account no.</th>
              <th>Balance</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {accounts.length === 0 && (
              <tr>
                <td colSpan="4" className="empty">
                  {uid ? "No accounts for this user" : "Select a user"}
                </td>
              </tr>
            )}
            {accounts.map((a) => (
              <tr key={a.a_id}>
                <td>{a.a_id}</td>
                <td>{a.accountNo}</td>
                <td>{formatMoney(a.balance)}</td>
                <td className="row-actions">
                  <button
                    className="btn small danger"
                    onClick={() => remove(a)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </>
  );
}
