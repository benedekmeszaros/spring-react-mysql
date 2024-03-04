import { useContext } from "react";
import GalleryContext from "../context/GalleryProvider";

const useGallery = () => {
  return useContext(GalleryContext);
};

export default useGallery;
