import { useState, createContext } from "react";
import "../style/alert.css";

const AlertContext = createContext({});

export const AlertProvider = ({ children }) => {
  const AlertType = {
    INFO: "info",
    ERROR: "error",
    SUCCESS: "success",
  };

  const [message, setMessage] = useState("Invalid password or username.");
  const [alertType, setAlertType] = useState(AlertType.INFO);
  const [isVisible, setIsVisible] = useState(false);

  const showMsg = (type, msg, duration) => {
    setIsVisible(true);
    setAlertType(type);
    setMessage(msg);
    duration > 0 &&
      setTimeout(() => {
        setIsVisible(false);
      }, duration);
  };

  const hideMsg = () => {
    setIsVisible(false);
  };

  return (
    <AlertContext.Provider value={{ showMsg, hideMsg }}>
      {children}
      {isVisible && (
        <article className={`alert ${alertType}`}>{message}</article>
      )}
    </AlertContext.Provider>
  );
};

export default AlertContext;
