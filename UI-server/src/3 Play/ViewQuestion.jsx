/* eslint-disable react/prop-types */

import { useEffect, useState } from "react"
import { getSelf } from "../functions.mjs";

let self = getSelf()

export const ViewQuestion = ({isHost, quiz, socket, roomId, quizLength}) => {

  const [currentQuestionInd, setCurrrentQuestionInd] = useState(0)
  const [currentQuestion, setCurrentQuestion] = useState(quiz?.questions[currentQuestionInd])
  const [isRevealed, setIsRevealed] = useState(false)

  const isLastQ = (currentQuestionInd == quiz?.questions.length-1)
  let progress = currentQuestionInd / quizLength

  const [flag, setFlag] = useState(false)
  function upd(){setFlag(!flag)}

  useEffect(() => {
    console.log(socket);
    isHost? setTimeout(()=>socket.emit('next',({roomId, questionInd: currentQuestionInd, question: currentQuestion })), 500)  //!
    : null

    socket.on('next',({questionInd, question})=>{console.log("VQ got next"); setIsRevealed(false),setCurrrentQuestionInd(questionInd),setCurrentQuestion(question)})
    socket.on('reveal',({correctChoicesInd})=>{setIsRevealed(true);console.log('revealed', isRevealed);})
  },[])

  function renderChoices(isRevealed) {
    const letters = ["A", "B", "C", "D"]

    return currentQuestion?.choices.map((choice,ind) => 
      isHost? 
        <div className={"choice _"+ind+" "+(choice.isCorrect?"correct ":" ")+(isRevealed?"revealed ":" ")} key={JSON.stringify(choice)} 
          onClick={ (!isHost && !isRevealed)? ()=>socket.emit('choice', ({roomId, userId: self.id, questionInd: currentQuestionInd, choices: [ind] })) : null }
        >
          {choice.title}
          <div className="letter">{letters[ind]}</div>
        </div>
      : 
        <button className={"choice _"+ind+" "+(choice.isCorrect?"correct ":" ")+(isRevealed?"revealed ":" ")} key={JSON.stringify(choice)} 
          onClick={ (!isHost && !isRevealed)? ()=>socket.emit('choice', ({roomId, userId: self.id, questionInd: currentQuestionInd, choices: [ind] })) : null }
        >
          {choice.title}
          <div className="letter">{letters[ind]}</div>
        </button>
    )
  }

  function revealCorrect(){
    setIsRevealed(true)
    let correctChoicesInd = []
    quiz?.questions[currentQuestionInd].choices.forEach((choice, index)=>{choice.isCorrect? correctChoicesInd.push(index) : null})
    socket.emit('reveal', {roomId, correctChoicesInd})
  }

  if (socket && socket.connected) return (
    <div className="ViewQuestion">
      <div className="head">
        <div className="title">{currentQuestion?.title}</div>
        <hstack>
          <progress value={progress}></progress>
          <div className="numbers">{currentQuestionInd+"/"+quizLength}</div>
        </hstack>
      </div>
      <div className="body">
        <div className="choices">{ renderChoices(isRevealed) }</div>
      </div>
      <div className="action-buttons">
        {isHost? <button onClick={()=>revealCorrect()}>reveal</button> : null}
        {isHost? <button className="question_next_btn" onClick={()=>{
          !isLastQ? socket.emit('next', {roomId, questionInd: currentQuestionInd+1, question: quiz.questions[currentQuestionInd+1]})
          :         socket.emit('end', {roomId} )
        }}> {!isLastQ? 'next' : 'end'} </button> 
        : null}
      </div>
    </div>
  ) 
  else return <div>Failed to connect socket</div>
}