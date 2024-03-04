import { useLocation, Navigate, Outlet } from "react-router-dom";
import useAuth from "../hook/useAuth";

const RequireAuth = ({ allowedRoles }) => {
  const { auth } = useAuth();
  const location = useLocation();

  return auth?.roles?.find((role) => allowedRoles.includes(role)) ||
    !allowedRoles ? (
    <Outlet />
  ) : (
    <Navigate to="/explorer" state={{ from: location }} replace />
  );
};

export default RequireAuth;
