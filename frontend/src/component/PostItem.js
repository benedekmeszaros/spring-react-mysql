import usePopup from "../hook/usePopup";
import Post from "./Post";
import format from "../helper/dateFormater";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import useAuth from "../hook/useAuth";
import useAlert from "../hook/useAlert";
import { useState } from "react";
import UpdatePost from "./UpdatePost";

const PostItem = ({ post }) => {
  const { show } = usePopup();
  const { user } = useAuth();
  const axiosPrivate = useAxiosPrivate();
  const { showMsg } = useAlert();
  const [dynamicPost, setDynamicPost] = useState(post);
  const [numberOfComments, setNumberOfComments] = useState(
    post.numberOfComments
  );

  const deletePost = async (e) => {
    e.stopPropagation();
    try {
      await axiosPrivate.post(
        `private/user/gallery/${dynamicPost.id}/delete/post`,
        {},
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      window.location.reload();
      showMsg("success", "Post deleted successfully.", 5000);
    } catch (err) {
      showMsg("error", "Failed to delete post.", 5000);
    }
  };

  const updatePost = async (e) => {
    e.stopPropagation();
    show(<UpdatePost post={dynamicPost} setPost={setDynamicPost} />);
  };

  return (
    <div
      onClick={() =>
        show(
          <Post post={dynamicPost} setNumberOfComments={setNumberOfComments} />
        )
      }
      className="item"
    >
      {user.username === dynamicPost.username && (
        <button onClick={deletePost} className="btn-delete">
          Delete
        </button>
      )}
      {user.username === dynamicPost.username && (
        <button onClick={updatePost} className="btn-edit">
          Edit
        </button>
      )}
      <h3>{dynamicPost.title}</h3>
      <p className="date">{format(dynamicPost.createDate)}</p>
      <div className="thumbnail">
        <img src={dynamicPost.image} alt="thumbnail" />
      </div>
      <div className="featured">
        <p>Comments {numberOfComments}</p>
        <p> Score {dynamicPost.score}</p>
      </div>
    </div>
  );
};

export default PostItem;
