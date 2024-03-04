import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "../api/axios";
import "../style/gallery.css";
import format from "../helper/dateFormater";
import PostGrid from "./PostGrid";
import useAuth from "../hook/useAuth";
import UploadPost from "./UploadPost";
import usePopup from "../hook/usePopup";
import useGallery from "../hook/useGallery";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import Post from "./Post";
import Searchbar from "./Searchbar";
import useAlert from "../hook/useAlert";
import GalleryUpdate from "./GalleryUpdate";

const Gallery = () => {
  const { id } = useParams();
  const { auth, user, setUser } = useAuth();
  const isMounted = useRef(false);
  const { gallery, setGallery, specificator } = useGallery();
  const { posts, setPosts } = useGallery();
  const { show, isVisible } = usePopup();
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const location = useLocation();
  const [sortBy, setSortBy] = useState(0);
  const [search, setSearch] = useState("");
  const sortByOptions = ["Latest", "Oldest", "Highest score"];
  const { showMsg } = useAlert();

  useEffect(() => {
    const controller = new AbortController();

    const getGallery = async () => {
      try {
        const res = await axios.get(`public/gallery/${id}`, {
          controller: controller.signal,
        });
        setGallery(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    const getPosts = async () => {
      try {
        const res = await axios.get(`public/gallery/${id}/post/all`, {
          controller: controller.signal,
        });
        setPosts(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (isMounted.current) {
      getGallery();
      getPosts();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [id, setGallery, setPosts]);

  useEffect(() => {
    if (specificator && posts) {
      const target = posts.find((p) => p.id === Number(specificator));
      !isVisible && target && show(<Post post={target} />);
    }
  }, [posts, specificator, isVisible, show]);

  const deleteGallery = async () => {
    try {
      await axiosPrivate.post(
        `private/user/delete/gallery/${gallery.id}`,
        {},
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      showMsg("success", "Gallery deleted.", 5000);
      navigate(`/gallery/self`, {
        state: { from: location },
        replace: true,
      });
    } catch (err) {
      showMsg("error", "Failed to deleted.", 5000);
    }
  };

  const showProfile = (e) => {
    navigate(`/profile/${gallery.userId}`, {
      state: { from: location },
    });
  };

  const toggleFollow = async () => {
    if (user.followings?.includes(gallery.username)) {
      try {
        await axiosPrivate.post(`private/user/unfollow?id=${gallery.userId}`);
        setUser({
          ...user,
          followings: user.followings.filter(
            (name) => name !== gallery.username
          ),
        });
      } catch (err) {
        console.log(err.response);
      }
    } else {
      try {
        await axiosPrivate.post(`private/user/follow?id=${gallery.userId}`);
        setUser({
          ...user,
          followings: user.followings.filter(
            (name) => name !== gallery.username
          ),
        });
        setUser({
          ...user,
          followings: [...user.followings, gallery.username],
        });
      } catch (err) {
        console.log(err.response);
      }
    }
  };

  const sort = (arr) => {
    if (sortBy === 0) return arr;
    else if (sortBy === 1) return arr.reverse();
    else return arr.sort((a, b) => b["score"] - a["score"]);
  };

  return (
    <section className="gallery">
      <div className="hero">
        {auth.accessToken && user.username === gallery.username && (
          <button className="btn-delete" onClick={deleteGallery}>
            Delete
          </button>
        )}

        {auth.accessToken && user.username === gallery.username && (
          <button
            className="btn-edit"
            onClick={() => show(<GalleryUpdate gallery={gallery} />)}
          >
            Edit
          </button>
        )}

        <div className="thumbnail">
          <img src={gallery.image} alt="thumbnail" />
        </div>
        <div className="user">
          <img onClick={showProfile} src={gallery.userImage} alt="icon" />
          <div>
            <h3>{gallery.username}</h3>
            <p className="date">{format(gallery.createDate)}</p>
          </div>
          {auth.accessToken && user.username !== gallery.username && (
            <button onClick={toggleFollow} className="btn-follow">
              {user.followings && user.followings.includes(gallery.username)
                ? "Unfollow"
                : "Follow"}
            </button>
          )}
        </div>
        <div className="info">
          <h1>{gallery.title}</h1>
          <div>
            <p>Post {posts.length}</p>
            <p>Score {gallery.score}</p>
          </div>
        </div>
      </div>
      <div className="description">
        {user.username === gallery.username && (
          <button
            onClick={() =>
              show(<UploadPost gallery={gallery} setPosts={setPosts} />)
            }
            className="btn-new-post"
          >
            New Post
          </button>
        )}
        <h3>Description</h3>
        <p>{gallery.description}</p>
      </div>
      <Searchbar
        sortByOptions={sortByOptions}
        setSortBy={setSortBy}
        setSearch={setSearch}
      />
      <PostGrid
        items={sort(
          posts.filter((post) =>
            post.title.toLowerCase().startsWith(search.toLowerCase())
          )
        )}
      />
    </section>
  );
};

export default Gallery;
