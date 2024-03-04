import { useContext } from "react";
import PopupContext from "../context/PopupProvider";

const usePopup = () => {
  return useContext(PopupContext);
};

export default usePopup;
