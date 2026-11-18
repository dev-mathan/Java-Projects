import { useEffect, useState } from "react";
import { getUsers } from "../api/api";

export default function UserSelect({ value, onChange, label = "User" }) {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    getUsers()
      .then((res) => setUsers(res.data))
      .catch(() => setUsers([]));
  }, []);

  return (
    <label className="field">
      <span>{label}</span>
      <select value={value} onChange={(e) => onChange(e.target.value)}>
        <option value="">-- select user --</option>
        {users.map((u) => (
          <option key={u.uid} value={u.uid}>
            {u.uid} - {u.name}
          </option>
        ))}
      </select>
    </label>
  );
}
