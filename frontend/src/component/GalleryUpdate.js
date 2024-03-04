import { useState, useEffect, useRef } from "react";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import useAlert from "../hook/useAlert";
import axios from "../api/axios";

const GalleryUpdate = ({ gallery }) => {
  const [filters, setFilters] = useState(gallery.categories);
  const [categories, setCategories] = useState([]);
  const [description, setDescription] = useState(gallery.description);
  const [title, setTitle] = useState(gallery.title);
  const [image, setImage] = useState(null);
  const { showMsg } = useAlert();
  const axiosPrivate = useAxiosPrivate();
  const isMounted = useRef(false);

  useEffect(() => {
    const controller = new AbortController();

    const getCategories = async () => {
      try {
        const res = await axios.get("public/category/all", {
          controller: controller.signal,
        });
        setCategories(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (isMounted.current) {
      getCategories();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, []);

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

  const submitHandle = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    formData.append("title", title);
    formData.append("description", description);
    formData.append("categories", filters);
    if (image != null) formData.append("image", image);

    try {
      await axiosPrivate.post(
        `private/user/update/gallery/${gallery.id}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      window.location.reload();
      showMsg("success", "Gallery updated.", 5000);
    } catch (err) {
      if (!err.response) showMsg("error", "Server problem.", 5000);
      else if (err?.response?.status === 403)
        showMsg("error", "Title is already taken.", 5000);
      else showMsg("error", "Failed to update.", 5000);
    }
  };

  return (
    <div className="gallery-edit">
      <form onSubmit={submitHandle}>
        <h1>Edit gallery</h1>
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
        <input
          type="file"
          autoComplete="off"
          accept="image/*"
          onChange={handleFileChange}
        />
        <div className="categories">
          {categories &&
            categories.map((c, i) => (
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
        <div className="line" />
        <button disabled={!filters || filters.length === 0}>Update</button>
      </form>
    </div>
  );
};

export default GalleryUpdate;
