import useAuth from "../hook/useAuth";
import usePopup from "../hook/usePopup";
import SignIn from "./SignIn";
import useLogout from "../hook/useLogout";
import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import useRefreshToken from "../hook/useRefreshToken";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import Notification from "./Notification";
import useGallery from "../hook/useGallery";

const Navbar = () => {
  const logout = useLogout();
  const { auth, user, setUser } = useAuth();
  const { show } = usePopup();
  const { setGallery } = useGallery();
  const navigate = useNavigate();
  const refresh = useRefreshToken();
  const isMounted = useRef(false);
  const axiosPrivate = useAxiosPrivate();
  const [dropDown, setDropdown] = useState(false);

  useEffect(() => {
    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (err) {}
    };

    verifyRefreshToken();
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    const getUser = async () => {
      try {
        const res = await axiosPrivate.get("private/user/self");
        setUser(res.data);
      } catch (error) {}
    };

    auth.accessToken && isMounted.current && getUser();

    return () => {
      isMounted.current = true;
    };
  }, [auth, axiosPrivate, setUser]);

  const singOut = async () => {
    await logout();
    setDropdown(false);
    setUser({});
    setGallery({});
    navigate("/explorer");
  };

  return (
    <nav>
      <div onClick={() => navigate("/explorer")} className="logo">
        <img src={process.env.PUBLIC_URL + "/icons/peek.png"} alt="icon" />
        <h2>Peek</h2>
      </div>
      {dropDown && (
        <div className="drop-down">
          <button onClick={singOut}>Sign Out</button>
        </div>
      )}
      {auth?.accessToken ? (
        <div className="controlls">
          <Notification />
          <img
            onClick={() => setDropdown((prev) => !prev)}
            className="icon"
            src={user.image}
            alt="icon"
          />
        </div>
      ) : (
        <button className="btn-sign-in" onClick={() => show(<SignIn />)}>
          Sign-in
        </button>
      )}
    </nav>
  );
};

export default Navbar;
