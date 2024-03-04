import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import Sidebar from "./Sidebar";
import "../style/layout.css";

const Layout = () => {
  return (
    <main>
      <div>
        <Navbar />
      </div>
      <div className="layout">
        <Sidebar />
        <div className="content">
          <Outlet />
        </div>
      </div>
    </main>
  );
};

export default Layout;
