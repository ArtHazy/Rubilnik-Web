import { limits } from "./values.mjs";

export function downloadObj(obj, title){
    let file = new File([JSON.stringify(obj)],'load.json');
    let fileURL = window.URL.createObjectURL(file);

    var fileLink = document.createElement('a');
    fileLink.href = fileURL;
    fileLink.download = title+'.json';
    fileLink.click();
}

export const putSelf = (self)=>{localStorage.setItem('self', JSON.stringify(self))}
export const getSelf = ()=>JSON.parse(localStorage.getItem('self'));
export const validateSelf = (self)=>{
    return Boolean(self?.id) //!
}

export function replaceValues(objReceiver, objGiver){
    Object.keys(objReceiver).forEach((key)=>{ delete objReceiver[key]; })
    Object.keys(objGiver).forEach((key)=>{ objReceiver[key] = objGiver[key]; })
}

export function loadQuizFromFile(file, upd){
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
