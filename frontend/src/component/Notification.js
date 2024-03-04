import { useEffect, useState, useRef } from "react";
import "../style/notification.css";
import NotificationItem from "./NotificationItem";
import useAxiosPrivate from "../hook/useAxiosPrivate";

const Notification = () => {
  const isMounted = useRef(false);
  const [dropdown, setDropdown] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const axiosPrivate = useAxiosPrivate();

  const toggleDropdown = () => {
    setDropdown((prev) => !prev);
  };

  useEffect(() => {
    const controller = new AbortController();

    const getNotifications = async () => {
      try {
        const res = await axiosPrivate.get("private/user/notification/all", {
          controller: controller.signal,
        });
        setNotifications(res.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (isMounted.current) {
      getNotifications();
    }

    return () => {
      isMounted.current = true;
      isMounted.current && controller.abort();
    };
  }, [axiosPrivate]);

  const getContent = () => {
    return notifications && notifications.length === 0 ? (
      <div className="dropdown">empty</div>
    ) : (
      <div className="dropdown">
        {notifications.map((n, i) => (
          <NotificationItem
            key={i}
            notification={n}
            setNotifications={setNotifications}
          />
        ))}
      </div>
    );
  };

  return (
    <section onClick={toggleDropdown} className="notification">
      <img className="icon" src="/icons/bell.svg" alt="icon" />
      {notifications?.filter((n) => !n.seen).length > 0 && (
        <div className="btn-toggle">
          <p>{notifications?.filter((n) => !n.seen).length}</p>
        </div>
      )}
      {dropdown && getContent()}
    </section>
  );
};

export default Notification;
