import format from "../helper/dateFormater";

const Comment = ({ comment }) => {
  return (
    <div className="comment">
      <div className="user">
        <img src={comment.userImage} alt="icon" />
        <h3>{comment.username}</h3>
      </div>
      <p>{comment.content}</p>
      <p className="date">{format(comment.createDate)}</p>
    </div>
  );
};

export default Comment;
