import { useEffect, useState } from "react";
import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
  errMsg,
} from "../api/api";
import Alert from "../components/Alert.jsx";

const emptyForm = { name: "", email: "", phone: "" };

export default function Users() {
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [alert, setAlert] = useState(null);

  const load = () =>
    getUsers()
      .then((res) => setUsers(res.data))
      .catch((e) => setAlert({ type: "error", text: errMsg(e) }));

  useEffect(() => {
    load();
  }, []);

  const change = (field) => (e) =>
    setForm({ ...form, [field]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await updateUser(editingId, form);
        setAlert({ type: "success", text: "User updated" });
      } else {
        await createUser(form);
        setAlert({ type: "success", text: "User added" });
      }
      setForm(emptyForm);
      setEditingId(null);
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  const startEdit = (u) => {
    setEditingId(u.uid);
    setForm({ name: u.name || "", email: u.email || "", phone: u.phone || "" });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setForm(emptyForm);
  };

  const remove = async (u) => {
    if (
      !window.confirm(
        `Delete ${u.name}? Their accounts and loans will be deleted too.`
      )
    )
      return;
    try {
      await deleteUser(u.uid);
      setAlert({ type: "success", text: "User deleted" });
      load();
    } catch (err) {
      setAlert({ type: "error", text: errMsg(err) });
    }
  };

  return (
    <>
      <h1>Users</h1>
      <Alert alert={alert} />

      <section className="card">
        <h2>{editingId ? `Edit user #${editingId}` : "Add user"}</h2>
        <form className="form-grid" onSubmit={submit}>
          <label className="field">
            <span>Name</span>
            <input value={form.name} onChange={change("name")} required />
          </label>
          <label className="field">
            <span>Email</span>
            <input
              type="email"
              value={form.email}
              onChange={change("email")}
              required
            />
          </label>
          <label className="field">
            <span>Phone</span>
            <input value={form.phone} onChange={change("phone")} required />
          </label>
          <div className="actions">
            <button type="submit" className="btn primary">
              {editingId ? "Update" : "Add user"}
            </button>
            {editingId && (
              <button type="button" className="btn" onClick={cancelEdit}>
                Cancel
              </button>
            )}
          </div>
        </form>
      </section>

      <section className="card">
        <h2>All users</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {users.length === 0 && (
              <tr>
                <td colSpan="5" className="empty">
                  No users yet
                </td>
              </tr>
            )}
            {users.map((u) => (
              <tr key={u.uid}>
                <td>{u.uid}</td>
                <td>{u.name}</td>
                <td>{u.email}</td>
                <td>{u.phone}</td>
                <td className="row-actions">
                  <button className="btn small" onClick={() => startEdit(u)}>
                    Edit
                  </button>
                  <button
                    className="btn small danger"
                    onClick={() => remove(u)}
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
