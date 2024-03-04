import GalleryItem from "./GalleryItem";

const GalleryGrid = ({ items }) => {
  return (
    <section className="grid">
      {items.map((item, i) => (
        <GalleryItem key={i} gallery={item} />
      ))}
    </section>
  );
};

export default GalleryGrid;
