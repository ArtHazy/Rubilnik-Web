import { useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { limits } from "../values.mjs"
import { Actions } from "./App"
import { downloadObj, getSelf, loadQuizFromFile, putSelf} from "../functions.mjs"
import { startRoomAsHost } from "./ViewLibrary"

let del = <span className="material-symbols-outlined">remove</span>
let drop_down = <span className="material-symbols-outlined">arrow_drop_down</span>
let drop_up = <span className="material-symbols-outlined">arrow_drop_up</span>

export const ViewQuizEdit = () => {
    const {ind} = useParams()
    const self = getSelf()
    const quiz = self.quizzes[ind]
    Array.isArray(quiz.questions)? null : quiz.questions = []
    const questions = quiz.questions
    const [focus, set_focus] = useState(-1) // question focus
    const [flag,setFlag] = useState(false)
    const navigate = useNavigate()

    function upd(){ putSelf(self), setFlag(!flag)}

    return <div className="ViewQuizEdit">
        <div className="header">
            <input type="text" value={quiz?.title} onChange={e=>{ quiz.title = e.target.value ,upd() }}/>
            <div>questions:</div>
        </div>
        <div className="grid">
            {questions.map((q,i)=>{
                Array.isArray(q.choices)? null : q.choices = []
                const choices = q.choices
                return [
                    <Question questions={questions} key={i*2} question={q} ind={i} upd={upd} isFocus={focus==i} set_focus={set_focus}/>,
                    <Choices choices={choices} upd={upd} isFocus={focus==i}/>
                ]
            })}
            {questions.length<limits.maxQuestionsLength? <button onClick={()=>{questions.push({title:'new'}), upd()}}><span className="material-symbols-outlined">add</span></button>:null}
        </div>



        <Actions>
            <button onClick={()=>{downloadObj(quiz, quiz.title)}}><span class="material-symbols-outlined">download</span></button>
            <label htmlFor="file-input"><button onClick={()=>{document.getElementById("file-input").click()}} ><span class="material-symbols-outlined">upload</span></button></label>
            <input style={{display:"none"}} id="file-input" type="file" onChange={(e)=>loadQuizFromFile(e.target.files[0], quiz, upd)}/>
            <button onClick={()=>startRoomAsHost(self.id, quiz, ind, navigate)}><span class="material-symbols-outlined">play_arrow</span></button>
        </Actions>
    </div>
}

const Question = ({questions, question, ind, isFocus, set_focus, upd}) => 
    <div className="question">
        <div>{ind}</div>
        <input type="text" value={question.title} onChange={(e)=>{ question.title = e.target.value; upd(); }}/>
        <button className="remove"onClick={()=>{Array.isArray(questions)? (console.log('delete: ', questions.splice(ind,1)), upd()) : null}}>{del}</button>
        <button className="question" onClick={()=>{isFocus? set_focus(-1):set_focus(ind)}}> {isFocus? drop_up : drop_down } </button>
    </div>

const Choices = ({choices,upd,isFocus}) => 
    <div className={"choices "+(isFocus? 'focus' : null)}>
        <div className="grid">
            {choices.map((c,i) => {
                typeof c.title == 'string'? null : c.title = ''
                return <div className="choice">
                    <input type="checkbox" checked={c.isCorrect} onChange={(e)=>{c.isCorrect? c.isCorrect = e.target.value : c.isCorrect = false, c.isCorrect=!c.isCorrect ,upd()}} />
                    <input type="text" value={c.title} onChange={(e)=>{c.title=e.target.value, upd()}}/>
                    <button className="remove" onClick={()=>{console.log(choices.splice(i,1)), upd()}}>{del}</button>
                </div>
            })}
            {choices.length<limits.maxChoicesLength? <button onClick={ ()=>{Array.isArray(choices)? choices.push({}):null, upd()} }>add</button>: null}
        </div>
    </div>