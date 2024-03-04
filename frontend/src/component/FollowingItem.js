import { Link } from "react-router-dom";

const FollowingItem = ({ following }) => {
  return (
    <div className="item">
      <img src={following.image} alt="img" />
      <h3>{following.username}</h3>
      <p className="date">Followers {following?.followers?.length}</p>
      <Link className="btn-details" to={`/profile/${following.id}`}>
        Details
      </Link>
    </div>
  );
};

export default FollowingItem;
