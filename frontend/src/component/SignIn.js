import { useState } from "react";
import usePopup from "../hook/usePopup";
import SignUp from "./SignUp";
import "../style/signin.css";
import axios from "../api/axios";
import useAlert from "../hook/useAlert";
import useAuth from "../hook/useAuth";

const SignIn = () => {
  const { show, setIsVisible } = usePopup();
  const { showMsg } = useAlert();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { setAuth } = useAuth();

  const submitHandle = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(
        "/auth/signin",
        JSON.stringify({ username, password }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      const accessToken = res?.data?.accessToken;
      const roles = res?.data?.roles;
      setAuth({ accessToken, roles });
      setUsername("");
      setPassword("");
      setIsVisible(false);
    } catch (err) {
      if (!err.response) showMsg("error", "Server problem.", 5000);
      else showMsg("error", "Invalid username or password.", 5000);
    }
  };

  return (
    <section>
      <h1>Sign In</h1>
      <form className="sign-form" onSubmit={submitHandle}>
        <input
          type="text"
          autoComplete="off"
          placeholder="Username*"
          required
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          autoComplete="off"
          placeholder="Password*"
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <div className="line" />
        <button disabled={username.length < 3 || password.length < 3}>
          Sign In
        </button>
      </form>
      <button
        className="btn-show"
        onClick={() => {
          show(<SignUp />);
        }}
      >
        Sign Up
      </button>
    </section>
  );
};

export default SignIn;
