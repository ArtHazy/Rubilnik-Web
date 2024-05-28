import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './2 App/App.jsx'
import './main.scss'
import { createBrowserRouter,RouterProvider} from "react-router-dom";
import { ViewLogin } from './1 Authentication/ViewLogin.jsx';
import { ViewRegister } from './1 Authentication/ViewRegister.jsx';
import { ViewQuizEdit } from './2 App/ViewQuizEdit.jsx';


const router = createBrowserRouter([
  { path: "/", element: <App/> },
  { path: "/login", element: <ViewLogin/> },
  { path: "/register", element: <ViewRegister/> },
  { path: "/edit-quiz/:ind", element: <ViewQuizEdit/> },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <RouterProvider router={router} />
)