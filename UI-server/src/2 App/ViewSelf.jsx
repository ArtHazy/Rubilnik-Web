import { useState } from "react";
import { limits } from "../values.mjs";
import { getSelf, putSelf, putSelfInDB, removeSelf } from "../functions.mjs"

export const ViewSelf = () => {
    const [flag,setFlag] = useState(false);
    const self = getSelf();
    function upd(){ putSelf(self), setFlag(!flag) }


    return <div className="ViewSelf">
        <header>Profile</header>
        <div className="form">
            <vstack>
                {console.log(self.id)}
                id:<input id='id' type="text" value={self.id} onChange={(e)=>{e.target.value=self.id}}/>
            </vstack>
            <vstack>
                name:<input id='name' type="text" value={self?.name} maxLength={limits.maxNameLength} onChange={(e)=>{self.name = e.target.value, upd()}} />
            </vstack>
            <vstack>
                password:<input id='password' type="password" value={self?.password} maxLength={limits.maxPassLength} onChange={(e)=>{self.password = e.target.value, upd()}} />
            </vstack>
            <vstack>
                <button onClick={()=>{
                    if(putSelfInDB(self)) {removeSelf(), window.location='/'}
                    else {confirm("Failed to save changes\nLog out without saving?")? (removeSelf(), window.location='/') : null}
                }}>log out</button>
            </vstack>
        </div>
    </div>
}