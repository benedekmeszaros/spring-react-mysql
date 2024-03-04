import { useState } from "react";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import { useNavigate, useLocation } from "react-router-dom";
import "../style/uploadgallery.css";
import usePopup from "../hook/usePopup";
import useAlert from "../hook/useAlert";

const UploadGallery = ({ categories }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [image, setImage] = useState(null);
  const [filters, setFilters] = useState([]);
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const location = useLocation();
  const { setIsVisible } = usePopup();
  const { showMsg } = useAlert();

  const handleButtonClick = (i) => {
    const item = categories[i];
    if (filters.includes(item)) {
      setFilters(filters.filter((filter) => filter !== item));
    } else {
      setFilters([...filters, item]);
    }
  };

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
    formData.append("categories", filters);
    try {
      const res = await axiosPrivate.post(
        "private/user/put/gallery",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      setIsVisible(false);
      showMsg("success", "Gallery created successfully.", 5000);
      navigate(`/gallery/${res.data}`, {
        state: { from: location },
      });
    } catch (err) {
      if (!err.response) showMsg("error", "Server problem.", 5000);
      else if (err?.response?.status === 403)
        showMsg("error", "Title is already taken.", 5000);
      else showMsg("error", "Failed to create.", 5000);
    }
  };

  const validate = () => {
    return title.length > 0 && image != null && categories.length > 0;
  };

  return (
    <section className="upload-gallery">
      <h1>New Gallery</h1>
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
        />
        <div className="categories">
          {categories.map((c, i) => (
            <button
              type="button"
              onClick={() => handleButtonClick(i)}
              className={
                filters.includes(categories[i])
                  ? "category-toggle toggled"
                  : "category-toggle"
              }
              key={i}
            >
              {c}
            </button>
          ))}
        </div>
        <button disabled={!validate()} className="btn-create">
          Create
        </button>
      </form>
    </section>
  );
};

export default UploadGallery;
