import { useLocation, useNavigate } from "react-router-dom";
import format from "../helper/dateFormater";
import useGallery from "../hook/useGallery";

const ActivityItem = ({ activity }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { setSpecificator } = useGallery();

  const hanldeAction = async () => {
    const [url, specificator] = activity.action.split("~");
    if (specificator) setSpecificator(specificator);
    navigate(url, { state: { from: location } });
  };

  return (
    <div onClick={hanldeAction} className="item">
      <img src={activity.image} alt="img" />
      <h3>{activity.username}</h3>
      <p>{activity.description}</p>
      <p className="date">{format(activity.date)}</p>
    </div>
  );
};

export default ActivityItem;
