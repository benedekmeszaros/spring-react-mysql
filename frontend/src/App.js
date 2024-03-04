import { Routes, Route } from "react-router-dom";
import Layout from "./component/Layout";
import Explorer from "./component/Explorer";
import Profile from "./component/Profile";
import RequireAuth from "./component/RequireAuth";
import Gallery from "./component/Gallery";
import MyGallery from "./component/MyGallery";
import Followings from "./component/Followings";
import AdminPage from "./component/AdminPage";
import NotFound from "./component/NotFound";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route path="/explorer" element={<Explorer />} />
        <Route path="/gallery/:id" element={<Gallery />} />

        <Route element={<RequireAuth allowedRoles={["USER"]} />}>
          <Route path="/profile/self" element={<Profile />} />
          <Route path="/gallery/self" element={<MyGallery />} />
          <Route path="/followings" element={<Followings />} />
        </Route>

        <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
          <Route path="/admin" element={<AdminPage />} />
        </Route>

        <Route path="/" element={<Explorer />} />
        <Route path="/profile/:id" element={<Profile />} />
      </Route>

      {/* catch all */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;
