import { useState } from "react";
import usePopup from "../hook/usePopup";
import SignIn from "./SignIn";
import axios from "../api/axios";
import useAlert from "../hook/useAlert";

const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
const EMAIL_REGEX = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$/;

const SignUp = () => {
  const { show } = usePopup();
  const { showMsg } = useAlert();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const submitHandle = async (e) => {
    e.preventDefault();
    try {
      await axios.post(
        "/auth/signup",
        JSON.stringify({ username, email, password }),
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      showMsg("success", "Registration success.", 5000);
      show(<SignIn />);
    } catch (err) {
      if (!err.response) showMsg("error", "Server problem.", 5000);
      if (err?.response?.status === 403)
        showMsg("error", "Username or e-mail address is already taken.", 5000);
      else showMsg("error", "Registration failed.", 5000);
    }
  };

  const validate = () => {
    return (
      USER_REGEX.test(username) &&
      EMAIL_REGEX.test(email) &&
      password.length > 2
    );
  };

  return (
    <section>
      <h1>Sign Up</h1>
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
          type="text"
          autoComplete="off"
          placeholder="E-mail*"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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
        <button disabled={!validate()}>Sign Up</button>
      </form>
      <button className="btn-show" onClick={() => show(<SignIn />)}>
        Back to Sign In
      </button>
    </section>
  );
};

export default SignUp;
