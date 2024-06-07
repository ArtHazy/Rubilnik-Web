import { CORE_SERVER_URL, limits } from "./values.mjs";

export function downloadObj(obj, title){
    let file = new File([JSON.stringify(obj)],'load.json');
    let fileURL = window.URL.createObjectURL(file);

    var fileLink = document.createElement('a');
    fileLink.href = fileURL;
    fileLink.download = title+'.json';
    fileLink.click();
}

export const putSelf = (self)=>{localStorage.setItem('self', JSON.stringify(self))}
export const removeSelf = (self)=>{localStorage.removeItem('self')}
export function putSelfInDB(self){
    let isOk
    let req = new XMLHttpRequest();
    req.open('PUT', CORE_SERVER_URL+'/user', false);
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ isOk=req.status==200 }
    req.send(JSON.stringify(self))
    console.log('putSelfInDB', isOk);
    return isOk
}

export const getSelf = ()=>JSON.parse(localStorage.getItem('self'));
export function validateSelfInDB(self){
    let isOk;
    const req = new XMLHttpRequest();
    req.open('POST', CORE_SERVER_URL+"/user/verify", false)
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = ()=>{ isOk=req.status==200 }
    req.send(JSON.stringify({email: self.email, password: self.password}));
    console.log('isOk',isOk);
    return isOk;
}

export function replaceValues(objReceiver, objGiver){
    Object.keys(objReceiver).forEach((key)=>{ delete objReceiver[key]; })
    Object.keys(objGiver).forEach((key)=>{ objReceiver[key] = objGiver[key]; })
}

export function loadQuizFromFile(file, quiz, upd){
    console.log(file);
    if (file instanceof File) {
        if (file.size>limits.maxQuizFileSise) { alert('file size is too big') }
        else {
            let fr = new FileReader();
            fr.readAsText(file)
            fr.onload = (e)=>{
                let ft = e.target.result
                console.log(file.size, ft.byteLength);
                let loadedQuiz = JSON.parse(ft)
                replaceValues(quiz, loadedQuiz)
                upd()
            }
        }
    }
}
