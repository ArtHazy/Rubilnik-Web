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


const router = createBrowserRouter([
  { path: "/", element: <App/> },
  { path: "/login", element: <ViewLogin/> },
  { path: "/register", element: <ViewRegister/> },
  { path: "/edit-quiz/:ind", element: <ViewQuizEdit/> },

  //! FOR TESTING
    { path: "/test/p", element: <Play/> },  
    { path: "/test/q", element: <ViewQuestion isHost={false} socket={{connected:true}} quiz={{questions:[{text:'title1', choices:[{text:'choice1'},{text:'choice2'},{text:'choice3'},{text:'choice4'}]},  {text:'title2',choices:[{text:'choice1', isCorrect:true},{text:'choice2'},{text:'choice3'}]}]}} quizLength={2} roomId={'OOOO'}/> }, 
    { path: "/test/l", element: <ViewLobby roomId={'AAAA'} socket={{connected:true}} /> },  
    { path: "/test/r", element: <ViewResult roommates={{id:'AAAA',name:'test'}}/> },  
  //!
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <RouterProvider router={router} />
)