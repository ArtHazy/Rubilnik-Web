import { useState } from "react";
import { limits } from "../values.mjs";
import { getSelf, putSelf } from "../functions.mjs"

export const ViewSelf = () => {
    const [flag,setFlag] = useState(false);
    function upd(){ putSelf(self), setFlag(!flag) }

    const self = getSelf();

    return <div className="ViewSelf">
        <header>ViewSelf</header>
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
        </div>
    </div>
}