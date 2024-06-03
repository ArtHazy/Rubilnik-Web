import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './2 App/App.jsx'
import './main.scss'
import { createBrowserRouter,RouterProvider} from "react-router-dom";
import { ViewLogin } from './1 Authentication/ViewLogin.jsx';
import { ViewRegister } from './1 Authentication/ViewRegister.jsx';
import { ViewQuizEdit } from './2 App/ViewQuizEdit.jsx';
import { ViewQuestion } from './3 Play/ViewQuestion.jsx';
import { ViewLobby } from './3 Play/ViewLobby.jsx';
import { ViewResult } from './3 Play/ViewResult.jsx';
import { Play } from './3 Play/Play.jsx';
import { ViewJoin } from './2 App/ViewJoin.jsx';


const router = createBrowserRouter([
  { path: "/", element: <App/> },
  { path: "/login", element: <ViewLogin/> },
  { path: "/register", element: <ViewRegister/> },
  { path: "/edit-quiz/:ind", element: <ViewQuizEdit/> },
  { path: "/play/:roomId", element: <Play/> },
  { path: "/join", element: <ViewJoin/> },

  //! FOR TESTING
    // { path: "/test/p", element: <Play/> },  
    // { path: "/test/q", element: <ViewQuestion/> }, 
    // { path: "/test/l", element: <ViewLobby/> },  
    // { path: "/test/r", element: <ViewResult/> },  
  //!
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <RouterProvider router={router} />
)