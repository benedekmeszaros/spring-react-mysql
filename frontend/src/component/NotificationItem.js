import format from "../helper/dateFormater";
import { useNavigate, useLocation } from "react-router-dom";
import useGallery from "../hook/useGallery";
import useAxiosPrivate from "../hook/useAxiosPrivate";

const NotificationItem = ({ notification, setNotifications }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { setSpecificator } = useGallery();
  const axiosPrivate = useAxiosPrivate();

  const hanldeAction = async () => {
    try {
      await axiosPrivate.post(
        `/private/user/notification/seen/${notification.id}`
      );
      setNotifications((prev) =>
        prev.map((item) =>
          item.id === notification.id ? { ...item, seen: true } : item
        )
      );
      const [url, specificator] = notification.action.split("~");
      if (specificator) setSpecificator(specificator);
      navigate(url, { state: { from: location } });
    } catch (err) {}
  };

  return (
    <div onClick={hanldeAction} className="item">
      <div>
        {!notification.seen && <div className={"hot"} />}
        <img src={notification?.userImage} alt="img" />
        <p>{notification?.content}</p>
      </div>
      <p className="date">{format(notification?.sentDate)}</p>
    </div>
  );
};

export default NotificationItem;
