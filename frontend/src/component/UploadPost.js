import { useRef, useState } from "react";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import usePopup from "../hook/usePopup";
import useAlert from "../hook/useAlert";

const UploadPost = ({ gallery, setPosts }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [image, setImage] = useState(null);
  const axiosPrivate = useAxiosPrivate();
  const fileInputRef = useRef(null);
  const { setIsVisible } = usePopup();
  const { showMsg } = useAlert();

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && file.type.startsWith("image/")) setImage(file);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("title", title);
    formData.append("description", description);
    formData.append("image", image);

    try {
      const res = await axiosPrivate.post(
        `private/user/gallery/${gallery.id}/put/post`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      res?.data && setPosts((prev) => [res.data, ...prev]);
      setTitle("");
      setDescription("");
      setImage(null);
      setIsVisible(false);
      showMsg("success", "Post created successfully.", 5000);
      fileInputRef.current.value = "";
    } catch (err) {
      if (!err.response) showMsg("error", "Server problem.", 5000);
      else if (err?.response?.status === 403)
        showMsg("error", "Title is already used in this gallery.", 5000);
      else showMsg("error", "Failed to create.", 5000);
    }
  };

  return (
    <section className="upload-gallery">
      <h1>New Post</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          autoComplete="off"
          placeholder="Title*"
          required
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <textarea
          autoComplete="off"
          placeholder="Description..."
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <input
          type="file"
          autoComplete="off"
          required
          accept="image/*"
          onChange={handleFileChange}
          ref={fileInputRef}
        />
        <div className="line"></div>
        <button className="btn-create">Post</button>
      </form>
    </section>
  );
};

export default UploadPost;
