import axios from "axios";
const BASE_URL = "http://localhost:7777/peek";

export default axios.create({
  baseURL: BASE_URL,
});

export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "applicatin/json" },
  withCredentials: true,
});
