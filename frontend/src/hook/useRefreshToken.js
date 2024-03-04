import axios from "../api/axios";
import useAuth from "./useAuth";

const useRefreshToken = () => {
  const { setAuth } = useAuth();

  const refresh = async () => {
    const res = await axios.get("/auth/refresh", {
      withCredentials: true,
    });
    setAuth((prev) => {
      return {
        ...prev,
        accessToken: res.data.accessToken,
        roles: res.data.roles,
      };
    });
    return res.data.accessToken;
  };

  return refresh;
};

export default useRefreshToken;
