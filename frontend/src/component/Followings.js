import useAxiosPrivate from "../hook/useAxiosPrivate";
import "../style/followings.css";
import FollowingItem from "./FollowingItem";
import { useEffect, useRef, useState } from "react";

const Followings = () => {
  const isMounted = useRef(false);
  const [followings, setFollowings] = useState([]);
  const axiosPrivate = useAxiosPrivate();

  useEffect(() => {
    const controller = new AbortController();

    const getFollowings = async () => {
      try {
        const res = await axiosPrivate.get("private/user/following/all", {
          controller: controller.signal,
        });
        setFollowings(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (isMounted.current) {
      getFollowings();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [axiosPrivate]);

  return (
    <section className="followings">
      {followings?.length > 0 ? (
        <div className="grid">
          {followings.map((following, i) => (
            <FollowingItem key={i} following={following} />
          ))}
        </div>
      ) : (
        <div className="info">You have not following any one yet.</div>
      )}
    </section>
  );
};

export default Followings;
