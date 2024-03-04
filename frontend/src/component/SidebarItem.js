import { useNavigate, useLocation } from "react-router-dom";

const SidebarItem = ({ item }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleClick = () => {
    navigate(item.url, { state: { from: location } });
  };

  return (
    <div onClick={handleClick} className="item">
      <img src={item.icon} alt="icon" />
      <p>{item.label}</p>
    </div>
  );
};

export default SidebarItem;
