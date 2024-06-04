import { useState } from "react"
import { useParams } from "react-router-dom"
import { limits } from "../values.mjs"
import { Actions } from "./App"
import { downloadObj, getSelf, loadQuizFromFile, putSelf} from "../functions.mjs"

export const ViewQuizEdit = () => {
    const {ind} = useParams()
    const self = getSelf()
    const quiz = self.quizzes[ind]
    Array.isArray(quiz.questions)? null : quiz.questions = []
    const questions = quiz.questions
    const [focus, set_focus] = useState(-1) // question focus
    const [flag,setFlag] = useState(false)
    function upd(){ putSelf(self), setFlag(!flag)}

    return <div className="ViewQuizEdit">
        <div className="vstack">
            <input value={quiz?.title} onChange={e=>{ quiz.title = e.target.value ,upd() }}/>
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
            <input style={{display:"none"}} id="file-input" type="file" onChange={(e)=>{loadQuizFromFile(e.target.files[0], upd)}}/>
            <button onClick={()=>{navigate(`/play/${user.id}`, {state: {quiz, ind: quizInd}})}}><span class="material-symbols-outlined">play_arrow</span></button>
        </Actions>
    </div>
}

const Question = ({questions, question, ind, isFocus, set_focus, upd}) => <button className="question" onClick={()=>{isFocus? set_focus(-1):set_focus(ind)}}>
    <div>{ind}</div>
    <div>{question.title}</div>
    <button onClick={()=>{Array.isArray(questions)? (console.log('delete: ', questions.splice(ind,1)), upd()) : null}}>del</button>
</button>

const Choices = ({choices,upd,isFocus}) => <div className={"choices "+(isFocus? 'focus' : null)}>
    <div className="grid">
        {choices.map((c,i) => {
            typeof c.title == 'string'? null : c.title = ''
            return <div className="choice">
                <input type="checkbox" checked={c.isCorrect} onChange={(e)=>{c.isCorrect? c.isCorrect = e.target.value : c.isCorrect = false, c.isCorrect=!c.isCorrect ,upd()}} />
                <input type="text" value={c.title} onChange={(e)=>{c.title=e.target.value, upd()}}/>
                <button onClick={()=>{console.log(choices.splice(i,1)), upd()}}>del</button>
            </div>
        })}
        {choices.length<limits.maxChoicesLength? <button onClick={ ()=>{Array.isArray(choices)? choices.push({}):null, upd()} }>add</button>: null}
    </div>
</div>