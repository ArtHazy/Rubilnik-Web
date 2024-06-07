import { useState } from "react";
import { limits } from "../values.mjs";
import { getSelf, putSelf } from "../functions.mjs"
import { useNavigate } from "react-router-dom";


export const ViewLibrary = () => {
    let self = getSelf();
    let quizzes = self?.quizzes
    const [flag,setFlag] = useState(false);
    function upd(){ putSelf(self), setFlag(!flag) }

    return <div className="ViewLibrary">
        <header>Your library</header>
        <div className="grid">
            {Array.isArray(quizzes)? quizzes.map((q,i)=>
                <QuizTile quiz={q} ind={i} upd={upd} self={self} quizzes={quizzes}/>
            ): null}
            

            {(!quizzes || quizzes.length<limits.maxQuizzesLength)? <button id="add" onClick={()=>{
                    Array.isArray(quizzes)? (quizzes.push({}), upd()) 
                    :                       (self.quizzes=[], self.quizzes.push({title:'new'}), upd())
                }}><span className="material-symbols-outlined">add</span></button>
            : null}
        </div>
    </div>
}

export const QuizTile = ({quiz, ind, upd, self, quizzes}) => {
    const navigate = useNavigate()
    return <div className='QuizTile'>
        <button id="del" onClick={()=>{Array.isArray(quizzes) && confirm("delete?")? (console.log('deleted: ',quizzes.splice(ind,1)) , upd()) : null}}><span className="material-symbols-outlined">delete</span></button>
        <button id="edit" onClick={()=>{console.log(quiz), window.location='/edit-quiz/'+ind}}> {quiz.title}</button>
        <button id="run" onClick={()=>startRoomAsHost(self.id, quiz, ind, navigate)}><span className="material-symbols-outlined">play_arrow</span></button>
    </div>
}

export function startRoomAsHost(id, quiz, ind, navigate){
    navigate(`/play/${id}`, {state: {quiz, ind}})
}