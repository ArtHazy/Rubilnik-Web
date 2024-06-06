import { useState } from "react";
import { ViewJoin } from "./ViewJoin";
import { ViewLibrary } from "./ViewLibrary";
import { ViewSelf } from "./ViewSelf";
import { getSelf, validateSelfInDB } from "../functions.mjs"

export const App = () => {
  const [view, setView] = useState(<ViewLibrary/>)

  if ( !getSelf() ) window.location.href = "/login"
  else {
    return <div className={"App"}>
      {view}
      <Navigation setView={setView} view={view}/>
    </div>
  }  
}

export default App;

/**
 * 
 * @param {(view: React.JSX.Element)=>{}} setView
 * @param {React.JSX.Element} view current view
 * @returns 
 */
export const Navigation = ({setView, view}) => 
  <div className="Navigation">
      <button onClick={()=>setView(<ViewLibrary/>)}>
          <span className="material-symbols-outlined">home</span>
      </button>
      <button onClick={()=>setView(<ViewJoin/>)}>
      <span className="material-symbols-outlined">hub</span>
      </button>
      <button onClick={()=>setView(<ViewSelf/>)}>
      <span className="material-symbols-outlined">person</span>
      </button>
  </div>

export const Actions = ({children}) => 
  <div className="Actions">
    {children}
  </div>