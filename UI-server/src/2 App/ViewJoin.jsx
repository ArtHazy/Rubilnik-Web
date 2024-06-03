import { useState } from "react"
import { getSelf, putSelf } from "../functions.mjs"
import { limits } from "../values.mjs"

export const ViewJoin = () => {
    let self = getSelf();
    if (!self) self = {}
    console.log(self);

    return <div className="ViewJoin">
        <header>ViewJoin</header>
        <div className="form">
            <vstack>name:<input id='name' type="text" value={self.name} maxLength={limits.maxNameLength} onChange={(e)=>{self.name = e.target.value, putSelf(self)}} /></vstack>
            <vstack>key:<RoomKeyInput/></vstack>
            <button style={{width:"100%"}} onClick={(e)=>{navigatePlay()}}>join</button>
        </div>
    </div>

    function navigatePlay(){
        putSelf(self)
        let inputs = document.querySelector('.RoomKeyInput')
        let roomkey = ''
        inputs.childNodes.forEach((child)=>{
            roomkey += child.value
        })
        roomkey=roomkey.toUpperCase()
        // alert(roomkey)
        window.location = '/play/'+roomkey
    }
}

const RoomKeyInput = () => {
    function switchTarget(e){
        e.target.value.length==1? e.target.nextElementSibling?.focus() : null
        // e.target.value.length==0? e.target.previousElementSibling?.focus() : null
    }
    function switchTarget_(e){
        // alert(e.key);
        e.target.value.length==0 && e.key=='Backspace'? e.target.previousElementSibling?.focus():null
    }
    return <div className="RoomKeyInput" style={{display:"flex", justifyContent:"safe center"}}>
        {new Array(4).fill(<input maxLength={1} style={{width:'1em', textAlign:'center', textTransform:'capitalize'}} onChange={(e)=>switchTarget(e)} onKeyDown={(e)=>switchTarget_(e)} />)}
    </div>
}



