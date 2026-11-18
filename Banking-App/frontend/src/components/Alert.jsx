export default function Alert({ alert }) {
  if (!alert) return null;
  return <div className={`alert ${alert.type}`}>{alert.text}</div>;
}
