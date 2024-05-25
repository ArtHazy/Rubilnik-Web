import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './main.scss'
import { createBrowserRouter,RouterProvider} from "react-router-dom";
import { ViewLogin } from './ViewLogin.jsx';
import { ViewRegister } from './ViewRegister.jsx';

export const SERVER_URL = "http://192.168.0.7:3000"

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
  },
  {
    path: "/login",
    element: <ViewLogin />,
  },
  {
    path: "/register",
    element: <ViewRegister />,
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <RouterProvider router={router} />
)
