import { useState, createContext } from "react";
import "../style/popup.css";

const PopupContext = createContext({});

export const PopupProvider = ({ children }) => {
  const [content, setContent] = useState(<>empty</>);
  const [isVisible, setIsVisible] = useState(false);

  const show = (element) => {
    setIsVisible(true);
    setContent(element);
  };

  return (
    <PopupContext.Provider
      value={{ isVisible, setIsVisible, setContent, show }}
    >
      {isVisible && (
        <article className="overlayer">
          <div className="container">
            <button onClick={(e) => setIsVisible(false)} className="btn-close">
              x
            </button>
            {content}
          </div>
        </article>
      )}
      {children}
    </PopupContext.Provider>
  );
};

export default PopupContext;
