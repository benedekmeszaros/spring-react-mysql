import "../style/post.css";
import format from "../helper/dateFormater";
import { useEffect, useRef, useState } from "react";
import axios from "../api/axios";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import Comment from "./Comment";
import useAuth from "../hook/useAuth";
import useGallery from "../hook/useGallery";

const Post = ({ post, setNumberOfComments }) => {
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState("");
  const [voteStatus, setVoteStatus] = useState(0);
  const { auth, user } = useAuth();
  const isMounted = useRef(false);
  const axiosPrivate = useAxiosPrivate();
  const { gallery, setGallery, posts, setPosts, setSpecificator } =
    useGallery();

  useEffect(() => {
    const controller = new AbortController();

    const getComments = async () => {
      try {
        const res = await axios.get(`public/post/${post.id}/comment/all`, {
          controller: controller.signal,
        });
        setComments(res.data);
        setSpecificator(undefined);
      } catch (err) {
        console.log(err);
      }
    };

    const getVoteStatus = async () => {
      try {
        const res = await axiosPrivate.get(
          `private/user/post/${post.id}/vote`,
          {
            controller: controller.signal,
          }
        );
        setVoteStatus(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (isMounted.current) {
      getComments();
      auth.accessToken && getVoteStatus();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [post.id, auth.accessToken, axiosPrivate, setSpecificator]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axiosPrivate.post(
        `private/user/post/${post.id}/put/comment`,
        JSON.stringify({ content }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      setComments((prev) => [...prev, res.data]);
      setNumberOfComments((prev) => prev + 1);
      setContent("");
    } catch (err) {
      console.log(err);
    }
  };

  const changeVote = async (value) => {
    const newValue = voteStatus !== value ? value : 0;
    const diff = newValue - voteStatus;
    post.score += diff;
    setPosts(posts.map((p) => (p.id === post.id ? post : p)));
    setGallery({
      ...gallery,
      score: gallery.score + diff,
    });
    setVoteStatus(newValue);

    try {
      await axiosPrivate.post(
        `private/user/post/${post.id}/update/vote?status=${newValue}`,
        {},
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
    } catch (err) {
      console.log(err.response);
    }
  };

  return (
    <div className="post">
      <div className="left">
        <h1>{post.title}</h1>
        <p className="date">Posted: {format(post.createDate)}</p>
        <p>{post.description}</p>
        <div className="image">
          <img src={post.image} alt="img" />
        </div>
        <div className="featured">
          <button
            className={voteStatus === 1 ? "btn-vote selected" : "btn-vote "}
            onClick={() => changeVote(1)}
            disabled={!auth.accessToken || post.username === user.username}
          >
            Up
          </button>
          <p>{post.score}</p>
          <button
            className={voteStatus === -1 ? "btn-vote selected" : "btn-vote "}
            onClick={() => changeVote(-1)}
            disabled={!auth.accessToken || post.username === user.username}
          >
            Down
          </button>
        </div>
      </div>
      <div className="right">
        <h2>Comments</h2>
        <div className="comments">
          {comments &&
            comments.map((comment, i) => <Comment key={i} comment={comment} />)}
        </div>
        {auth.accessToken && (
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Comment..."
              value={content}
              onChange={(e) => setContent(e.target.value)}
              required
              autoComplete="off"
            />
            <button className="btn-send">Send</button>
          </form>
        )}
      </div>
    </div>
  );
};

export default Post;
