import { useEffect, useState } from "react";
import "../style/searchbar.css";

const Searchbar = ({
  categories,
  setCategories,
  setSearch,
  sortByOptions,
  setSortBy,
}) => {
  const [filters, setFilters] = useState([]);

  const handleButtonClick = (i) => {
    const item = categories[i];
    if (filters.includes(item)) {
      setFilters(filters.filter((filter) => filter !== item));
    } else {
      setFilters([...filters, item]);
    }
  };

  useEffect(() => {
    setCategories && setCategories(filters);
  }, [filters, setCategories]);

  return (
    <section className="searchbar">
      <div className="controlls">
        <input
          className="search-input"
          type="text"
          placeholder="Search..."
          onChange={(e) => setSearch && setSearch(e.target.value)}
        />
        {sortByOptions && (
          <div className="sort">
            <label htmlFor="options">Sort by</label>
            <select
              onChange={(e) => setSortBy(Number(e.target.value))}
              id="options"
            >
              {sortByOptions.map((s, i) => (
                <option key={i} value={i}>
                  {s}
                </option>
              ))}
            </select>
          </div>
        )}
      </div>
      <div className="categories">
        {categories &&
          categories.map((c, i) => (
            <button
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
    </section>
  );
};

export default Searchbar;
