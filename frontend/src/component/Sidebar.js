import useAuth from "../hook/useAuth";
import SidebarItem from "./SidebarItem";
import "../style/sidebar.css";

const Sidebar = () => {
  const { auth } = useAuth();
  const openLinks = [
    {
      url: "/explorer",
      label: "Explore",
      icon: "/icons/search.svg",
    },
  ];
  const privateLinks = [
    {
      url: "/explorer",
      label: "Explore",
      icon: "/icons/search.svg",
    },
    {
      url: "/profile/self",
      label: "Profile",
      icon: "/icons/profile.svg",
    },
    {
      url: "/gallery/self",
      label: "My Gallery",
      icon: "/icons/gallery.svg",
    },
    {
      url: "/followings",
      label: "Followings",
      icon: "/icons/follow.svg",
    },
    {
      url: "/admin",
      label: "Admin",
      role: "ADMIN",
      icon: "/icons/admin.svg",
    },
  ];

  const links = () => {
    return !auth?.accessToken
      ? openLinks
      : privateLinks.filter((l) => !l.role || auth.roles.includes(l.role));
  };

  return (
    <section className="sidebar">
      {links().map((l, i) => (
        <SidebarItem key={i} item={l} />
      ))}
    </section>
  );
};

export default Sidebar;
