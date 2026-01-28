import { Link } from "react-router-dom";

export default function ServiceCard({ title, description }) {
  return (
    <div className="card">
      <h3>{title}</h3>
      <p>{description}</p>
      <Link to="/register">
        <button>Register Now</button>
      </Link>
    </div>
  );
}
