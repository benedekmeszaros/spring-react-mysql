import PostItem from "../component/PostItem";
import "../style/postgrid.css";

const PostGrid = ({ items }) => {
  return (
    <section className="grid">
      {items.map((item, i) => (
        <PostItem key={i} post={item} />
      ))}
    </section>
  );
};

export default PostGrid;
