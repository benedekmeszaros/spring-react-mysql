import { useState } from "react";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import useAlert from "../hook/useAlert";
import usePopup from "../hook/usePopup";

const UpdatePost = ({ post, setPost }) => {
  const [description, setDescription] = useState(post.description);
  const [title, setTitle] = useState(post.title);
  const axiosPrivate = useAxiosPrivate();
  const { showMsg } = useAlert();
  const { setIsVisible } = usePopup();

  const updatePost = async () => {
    try {
      await axiosPrivate.post(
        `private/user/post/${post.id}/update`,
        JSON.stringify({ description, title }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      post.description = description;
      post.title = title;
      setPost(post);
      setIsVisible(false);
      showMsg("success", "Post updated successfully.", 5000);
    } catch (err) {
      showMsg("error", "Failed to update post.", 5000);
    }
  };

  return (
    <div className="post-update">
      <h1>Edit post</h1>
      <label htmlFor="title">Title</label>
      <input
        id="title"
        type="text"
        value={title}
        required
        onChange={(e) => setTitle(e.target.value)}
      />
      <label form="desc">Description</label>
      <textarea
        id="desc"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        required
      />
      <div className="line" />
      <button onClick={updatePost}>Update</button>
    </div>
  );
};

export default UpdatePost;
