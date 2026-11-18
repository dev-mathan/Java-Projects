# Banking Frontend (React + Vite)

React frontend for the Spring Boot `banking-app` backend.

## Pages
- **Users** - add, edit, delete users
- **Accounts** - pick a user, view/create/delete their accounts
- **Loans** - pick a user, create loans, change status, delete
- **Transactions** - add a transaction to an account, view a user's
  transactions (paged) or filter by date range

## Run

1. Start MySQL and the backend (`http://localhost:8080`).
2. Make sure the backend has the `CorsConfig` class allowing
   `http://localhost:5173`.
3. In this folder:

```bash
npm install
npm run dev
```

4. Open http://localhost:5173

## Change the backend URL

Edit `baseURL` in `src/api/api.js`, or create a `.env` file:

```
VITE_API_URL=http://localhost:8080/api
```

## Project structure

```
src
├── api/api.js            all backend calls (axios)
├── components/           UserSelect, Alert
├── pages/                Users, Accounts, Loans, Transactions
├── utils.js              date and money formatting
├── App.jsx               navbar + routes
├── main.jsx              entry point
└── index.css             styles
```
