import "../style/admin.css";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import { useEffect, useRef, useState } from "react";
import ActivityItem from "./ActivityItem";
import axios from "../api/axios";
import useAlert from "../hook/useAlert";

const AdminPage = () => {
  const axiosPrivate = useAxiosPrivate();
  const [category, setCategory] = useState("");
  const [activities, setActivities] = useState([]);
  const [categories, setCategories] = useState([]);
  const isMounted = useRef(false);
  const { showMsg } = useAlert();

  useEffect(() => {
    const controller = new AbortController();

    const getActivities = async () => {
      try {
        const res = await axiosPrivate.get(
          `private/admin/activity/all?date=${new Date().toISOString()}`,
          {
            controller: controller.signal,
          }
        );
        setActivities(res.data);
      } catch (err) {
        console.log(err.response);
      }
    };

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
      getActivities();
      getCategories();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [axiosPrivate]);

  const submitHandle = async (e) => {
    e.preventDefault();

    if (categories?.includes(category)) {
      setCategory("");
      showMsg("error", "Category already exists.", 5000);
      return;
    }

    try {
      await axiosPrivate.post(
        `private/admin/put/category`,
        JSON.stringify({ category }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      setCategories((prev) => [...prev, category].sort());
      setCategory("");
      showMsg("success", "New category added.", 5000);
    } catch (err) {
      showMsg("error", "Failed to create category.", 5000);
    }
  };

  return (
    <div className="admin">
      <div className="upper">
        <h2>Insert new Category</h2>
        <form onSubmit={submitHandle}>
          <input
            type="text"
            placeholder="Category*"
            autoComplete="off"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
          />
          <button>Add</button>
        </form>
        <div className="categories">
          {categories &&
            categories.map((c, i) => (
              <p key={i} className="category">
                {c}
              </p>
            ))}
        </div>
      </div>
      <div className="lower">
        <h2>Today's activities</h2>
        <div className="header">
          <div />
          <h3>Username</h3>
          <h3>Description</h3>
          <h3>Date</h3>
        </div>
        <div className="line" />
        <div className="activities">
          {activities &&
            activities
              .sort((a, b) => new Date(b.date) - new Date(a.date))
              .map((a, i) => <ActivityItem key={i} activity={a} />)}
        </div>
        <div className="line" />
      </div>
    </div>
  );
};

export default AdminPage;
