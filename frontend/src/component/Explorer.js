import { useEffect, useRef, useState } from "react";
import axios from "../api/axios";
import GalleryGrid from "./GalleryGrid";
import Searchbar from "./Searchbar";
import "../style/explorer.css";

const Explorer = () => {
  const isMounted = useRef(false);
  const [galleries, setGalleries] = useState([]);
  const [categories, setCategories] = useState([]);
  const [filters, setFilters] = useState([]);
  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState(0);
  const sortByOptions = ["Latest", "Oldest", "Highest score", "Post number"];

  useEffect(() => {
    const controller = new AbortController();

    const getGalleries = async () => {
      try {
        const res = await axios.get("public/gallery/all", {
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
  }, []);

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

export default Explorer;
