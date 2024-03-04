import { createContext, useState } from "react";

const GalleryContext = createContext({});

export const GalleryProvider = ({ children }) => {
  const [gallery, setGallery] = useState({});
  const [posts, setPosts] = useState([]);
  const [specificator, setSpecificator] = useState(undefined);

  return (
    <GalleryContext.Provider
      value={{
        gallery,
        setGallery,
        posts,
        setPosts,
        specificator,
        setSpecificator,
      }}
    >
      {children}
    </GalleryContext.Provider>
  );
};

export default GalleryContext;
