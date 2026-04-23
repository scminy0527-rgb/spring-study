import MainPage from "../page/MainPage";

const { createBrowserRouter } = require("react-router-dom");

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainPage />,
  },
]);

export default router;
