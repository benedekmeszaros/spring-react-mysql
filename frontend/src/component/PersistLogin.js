import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import useAuth from "../hook/useAuth";
import useRefreshToken from "../hook/useRefreshToken";

const PersistLogin = () => {
  const [isLoading, setIsLoading] = useState(true);
  const { auth } = useAuth();
  const refresh = useRefreshToken();

  useEffect(() => {
    let isMounted = true;
    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (err) {
        console.log(err);
      } finally {
        isMounted && setIsLoading(false);
      }

      return () => {
        isMounted = false;
      };
    };

    !auth.accessToken ? verifyRefreshToken() : setIsLoading(false);
  }, [auth.accessToken, refresh]);

  return <>{isLoading ? <p>Loading...</p> : <Outlet />}</>;
};

export default PersistLogin;
