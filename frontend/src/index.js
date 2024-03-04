import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { AuthProvider } from "./context/AuthProvider";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { PopupProvider } from "./context/PopupProvider";
import { AlertProvider } from "./context/AlertProvider";
import { GalleryProvider } from "./context/GalleryProvider";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <AlertProvider>
          <GalleryProvider>
            <PopupProvider>
              <Routes>
                <Route path="/*" element={<App />} />
              </Routes>
            </PopupProvider>
          </GalleryProvider>
        </AlertProvider>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);
