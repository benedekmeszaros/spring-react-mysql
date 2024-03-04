import React from "react";
import { useState } from "react";
import usePopup from "../hook/usePopup";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import useAuth from "../hook/useAuth";
import "../style/uploadgallery.css";
import useAlert from "../hook/useAlert";

const UpdateProfile = ({ profile, setProfile }) => {
  const [email, setEmail] = useState(profile.email);
  const [description, setDescription] = useState(profile.description);
  const [image, setImage] = useState(null);
  const { setUser } = useAuth();
  const { setIsVisible } = usePopup();
  const axiosPrivate = useAxiosPrivate();
  const { showMsg } = useAlert();

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && file.type.startsWith("image/")) setImage(file);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    if (email !== profile.email) formData.append("email", email);
    formData.append("description", description);
    if (image != null) formData.append("image", image);

    try {
      const res = await axiosPrivate.post(`private/user/update`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      setUser(res.data);
      setIsVisible(false);
      showMsg("success", "Profile updated.", 5000);
      window.location.reload();
    } catch (err) {
      if (!err.response) showMsg("error", "Server problem.", 5000);
      else if (err?.response?.status === 403)
        showMsg("error", "E-mail address is already taken.", 5000);
      else showMsg("error", "Failed to update.", 5000);
    }
  };

  return (
    <section className="upload-gallery">
      <h1>Edit Profile</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          autoComplete="off"
          placeholder="E-mail*"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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
          accept="image/*"
          onChange={handleFileChange}
        />
        <button className="btn-create">Update</button>
      </form>
    </section>
  );
};

export default UpdateProfile;
