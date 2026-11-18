import { NavLink, Navigate, Route, Routes } from "react-router-dom";
import Users from "./pages/Users.jsx";
import Accounts from "./pages/Accounts.jsx";
import Loans from "./pages/Loans.jsx";
import Transactions from "./pages/Transactions.jsx";

export default function App() {
  return (
    <>
      <header className="navbar">
        <div className="brand">Banking App</div>
        <nav>
          <NavLink to="/users">Users</NavLink>
          <NavLink to="/accounts">Accounts</NavLink>
          <NavLink to="/loans">Loans</NavLink>
          <NavLink to="/transactions">Transactions</NavLink>
        </nav>
      </header>

      <main className="container">
        <Routes>
          <Route path="/" element={<Navigate to="/users" replace />} />
          <Route path="/users" element={<Users />} />
          <Route path="/accounts" element={<Accounts />} />
          <Route path="/loans" element={<Loans />} />
          <Route path="/transactions" element={<Transactions />} />
          <Route path="*" element={<Navigate to="/users" replace />} />
        </Routes>
      </main>
    </>
  );
}
