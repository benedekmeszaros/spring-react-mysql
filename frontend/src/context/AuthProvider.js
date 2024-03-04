import { createContext, useState } from "react";

const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const persistedValue = JSON.parse(localStorage.getItem("persist")) || false;
  const [auth, setAuth] = useState({});
  const [user, setUser] = useState(persistedValue);

  return (
    <AuthContext.Provider value={{ auth, setAuth, user, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
