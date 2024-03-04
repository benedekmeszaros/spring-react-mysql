import { useLocation, useNavigate, useParams } from "react-router-dom";
import "../style/profile.css";
import { useEffect, useRef, useState } from "react";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import axios from "../api/axios";
import format from "../helper/dateFormater";
import useAuth from "../hook/useAuth";
import usePopup from "../hook/usePopup";
import UpdateProfile from "./UpdateProfile";
import GalleryGrid from "./GalleryGrid";

const Profile = () => {
  const { id } = useParams();
  const { auth, user, setUser } = useAuth();
  const axiosPrivate = useAxiosPrivate();
  const isMounted = useRef(false);
  const [profile, setProfile] = useState({});
  const [galleries, setGalleries] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const { show } = usePopup();

  useEffect(() => {
    const controller = new AbortController();
    const getProfile = async () => {
      if (!id || id === user.id) {
        try {
          const res = await axiosPrivate.get("private/user/self", {
            controller: controller.signal,
          });
          setProfile(res.data);
        } catch (err) {
          navigate("/explorer", { state: { from: location }, replace: true });
        }
      } else {
        try {
          const res = await axios.get(`public/user/${id}`, {
            controller: controller.signal,
          });
          setProfile(res.data);
        } catch (err) {
          navigate("/explorer", { state: { from: location }, replace: true });
        }
      }
    };

    if (isMounted.current) {
      getProfile();
    }
    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [id, user, user.id, axiosPrivate, location, navigate]);

  useEffect(() => {
    const controller = new AbortController();

    const getGalleries = async () => {
      try {
        const res = await axios.get(`public/gallery/user/${profile.id}/all`, {
          controller: controller.signal,
        });
        setGalleries(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (profile.id) getGalleries();
  }, [profile.id]);

  const toggleFollow = async () => {
    if (user.followings?.includes(profile.username)) {
      try {
        await axiosPrivate.post(`private/user/unfollow?id=${profile.id}`);
        setUser({
          ...user,
          followings: user.followings.filter(
            (name) => name !== profile.username
          ),
        });
      } catch (err) {
        console.log(err.response);
      }
    } else {
      try {
        await axiosPrivate.post(`private/user/follow?id=${profile.id}`);
        setUser({
          ...user,
          followings: user.followings.filter(
            (name) => name !== profile.username
          ),
        });
        setUser({
          ...user,
          followings: [...user.followings, profile.username],
        });
      } catch (err) {
        console.log(err.response);
      }
    }
  };

  return (
    <section className="profile">
      <div className="panel">
        <img className="icon" src={profile.image} alt="icon" />
        <div className="details">
          <div className="info">
            <div>
              <h1>{profile.username}</h1>
              <p className="date">Joined: {format(profile.registrationDate)}</p>
              <p>{profile.description || "No description."}</p>
            </div>
            <div className="holder">
              {auth.accessToken && user.username !== profile.username && (
                <button onClick={toggleFollow}>
                  {user.followings && user.followings.includes(profile.username)
                    ? "Unfollow"
                    : "Follow"}
                </button>
              )}
            </div>
          </div>
          <div className="featured">
            {user.username === profile.username && (
              <button
                onClick={() =>
                  show(
                    <UpdateProfile profile={profile} setProfile={setProfile} />
                  )
                }
              >
                Edit
              </button>
            )}
            <div>
              <p>Followers {profile?.followers?.length}</p>
              <p>Galleries {profile.numberOfGalleries}</p>
            </div>
          </div>
        </div>
      </div>
      <div className="explorer">
        <GalleryGrid items={galleries} />
      </div>
    </section>
  );
};

export default Profile;
