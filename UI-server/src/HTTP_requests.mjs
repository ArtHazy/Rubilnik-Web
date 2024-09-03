import { CORE_SERVER_URL } from "./values.mjs";

let onerror = (e) => {alert(e.message);}

export function http_user_verify({email, password}, onload){
    let isOk, id;
    const req = new XMLHttpRequest();
    req.open('POST', CORE_SERVER_URL+"/user/verify", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200; id = JSON.parse(req.responseText).id;}
    req.onerror = onerror;
    req.send(JSON.stringify({email, password}));
    console.log('isOk',isOk);
    return {isOk, id};
}
export function http_post_user({name, email, password}, onload){
    let isOk, id;

    const req = new XMLHttpRequest();
    req.open('POST', CORE_SERVER_URL+"/user", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200; console.log('req',req);  id = JSON.parse(req.responseText).id; }
    req.onerror = onerror;
    req.send(JSON.stringify({name, email, password}));
    console.log('isOk',isOk);
    return {isOk, id};
}
export function http_post_user_get({email, password}, onload){
    let isOk, user;
    const req = new XMLHttpRequest();
    req.open('POST', CORE_SERVER_URL+"/user/get", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200; console.log('req',req);  user = JSON.parse(req.responseText); }
    req.onerror = onerror;
    req.send(JSON.stringify({name, email, password}));
    console.log('isOk',isOk);
    console.log('user',user);
    return {isOk, user};
}
// export function http_delete_user({id}, onload){
//     let isOk;
//     const req = new XMLHttpRequest();
//     req.open('DELETE', CORE_SERVER_URL+"/user", false)
//     req.setRequestHeader('Content-Type', 'application/json');
//     req.onload = ()=>{ onload(); isOk=req.status==200 }
//     req.onerror = onerror;
//     req.send(JSON.stringify({id}));
//     console.log('isOk',isOk);
//     return isOk;
// }

export function http_put_user(validation={id,password},user={name,email,password}, onload){
    let isOk;
    const req = new XMLHttpRequest();
    req.open('PUT', CORE_SERVER_URL+"/user", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200 }
    req.onerror = onerror;
    req.send(JSON.stringify({validation, user}));
    console.log('isOk',isOk);
    return isOk;
}

/**
 * @param { {id:string, password:string} } validation 
 * @param { {title:string, questions:[{title:string,choices:[{title:string,correct:boolean}]}]} } quiz 
 * @param {Function} onload
 * @returns
 */
export function http_post_quiz(validation={id,password},quiz, onload){
    let isOk, id;
    const req = new XMLHttpRequest();
    req.open('POST', CORE_SERVER_URL+"/quiz", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200; id = JSON.parse(req.responseText).id }
    req.onerror = onerror;
    req.send(JSON.stringify( {validation,quiz} ));
    return {isOk, id};
}

/**
 * @param { {id:string, title:string, questions:[{title:string,choices:[{title:string,correct:boolean}]}]} } quiz 
 * @param {Function} onload
 */
export function http_put_quiz(validation={id,password}, quiz, onload){
    let isOk;
    const req = new XMLHttpRequest();
    req.open('PUT', CORE_SERVER_URL+"/quiz", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200; }
    req.onerror = onerror;
    req.send(JSON.stringify( {validation,quiz} ));
    console.log('isOk',isOk);
    return isOk;
}
export function http_delete_quiz(validation={id,password}, id, onload){
    let isOk;
    const req = new XMLHttpRequest();
    req.open('DELETE', CORE_SERVER_URL+"/quiz", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ onload(); isOk=req.status==200; }
    req.onerror = onerror;
    req.send(JSON.stringify( {validation,id} ));
    console.log('isOk',isOk);
    return isOk;
}


