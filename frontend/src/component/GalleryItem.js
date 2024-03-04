import format from "../helper/dateFormater";
import { useNavigate, useLocation } from "react-router-dom";
import useGallery from "../hook/useGallery";

const GalleryItem = ({ gallery }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { setGallery, setPosts } = useGallery();

  const showGallery = () => {
    setGallery({});
    setPosts([]);
    navigate(`/gallery/${gallery.id}`, {
      state: { from: location },
    });
  };

  const showProfile = (e) => {
    e.stopPropagation();
    navigate(`/profile/${gallery.userId}`, {
      state: { from: location },
    });
  };

  return (
    <div onClick={showGallery} className="item">
      <div className="user">
        <img onClick={showProfile} src={gallery.userImage} alt="icon" />
        <div>
          <h3>{gallery.username}</h3>
          <p className="date">{format(gallery.createDate)}</p>
        </div>
      </div>
      <h2>{gallery.title}</h2>
      <div className="thumbnail">
        <img src={gallery.image} alt="thumbnail" />
      </div>
      <div className="featured">
        <p>Posts {gallery.numberOfPosts}</p>
        <p>Score {gallery.score}</p>
      </div>
    </div>
  );
};

export default GalleryItem;
