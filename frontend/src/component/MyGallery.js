import { useEffect, useRef, useState } from "react";
import useAxiosPrivate from "../hook/useAxiosPrivate";
import axios from "../api/axios";
import GalleryGrid from "./GalleryGrid";
import Searchbar from "./Searchbar";
import usePopup from "../hook/usePopup";
import UploadGallery from "./UploadGallery";

const MyGallery = () => {
  const isMounted = useRef(false);
  const [galleries, setGalleries] = useState([]);
  const [categories, setCategories] = useState([]);
  const axiosPrivate = useAxiosPrivate();
  const [filters, setFilters] = useState([]);
  const [search, setSearch] = useState("");
  const { show } = usePopup();
  const [sortBy, setSortBy] = useState(0);
  const sortByOptions = ["Latest", "Oldest", "Highest score", "Post number"];

  useEffect(() => {
    const controller = new AbortController();

    const getGalleries = async () => {
      try {
        const res = await axiosPrivate.get("private/user/gallery/self", {
          controller: controller.signal,
        });
        setGalleries(res.data);
      } catch (err) {
        console.log(err);
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
      getGalleries();
      getCategories();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [axiosPrivate]);

  const sort = (arr) => {
    if (sortBy === 0) return arr;
    else if (sortBy === 1) return arr.reverse();
    else if (sortBy === 2) return arr.sort((a, b) => b["score"] - a["score"]);
    else return arr.sort((a, b) => b["numberOfPosts"] - a["numberOfPosts"]);
  };

  return (
    <div className="explorer">
      <Searchbar
        categories={categories}
        setCategories={setFilters}
        setSearch={setSearch}
        sortByOptions={sortByOptions}
        setSortBy={setSortBy}
      />
      <section className="user-feature">
        <button onClick={() => show(<UploadGallery categories={categories} />)}>
          New Gallery
        </button>
      </section>
      <GalleryGrid
        items={sort(
          galleries.filter(
            (gallery) =>
              (filters.length === 0 ||
                gallery.categories.some((c) => filters.includes(c))) &&
              (search.length === 0 ||
                gallery.title.toLowerCase().startsWith(search.toLowerCase()))
          )
        )}
      />
    </div>
  );
};

export default MyGallery;
