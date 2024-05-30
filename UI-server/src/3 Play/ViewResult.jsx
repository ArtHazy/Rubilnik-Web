import { useEffect, useState } from "react"

export const ViewResult = ({usersChoices, isHost, roomId, socket, quiz, roommates}) => {

  // const [usersScores, setUsersScores] = useState([])
  const [usersScores, setUsersScores] = useState([{userId:'AAAA',userScore:12}]) //! FOR TEST

  // useEffect(() => {
  //   socket.on('scores',({usersScores})=>{
  //     console.log('scores received', usersScores);
  //     setUsersScores(usersScores)
  //   })
  //   if (isHost) {
  //     let correctChoices = getCorrectChoices()
  //     console.log('correctChoices', correctChoices);
  //     let usersScores = calculateScores(usersChoices, correctChoices)
  //     usersScores.sort((a,b)=>a.userScore - b.userScore)
  //     socket.emit('scores', {roomId, usersScores})
  //   }
  // },[])

  return (
    <div className="ViewResult">
      <div className="scores">
        <div className="entry"> <div>place</div><div>name</div><div>score</div> </div>
        {console.log({roommates, usersScores, usersChoices})}

        {usersScores.map((entry, index)=>
          <div key={index} className="entry">
            <div className={index == 0 ? "index first" : "index"}>{index + 1}</div>
            <div className={index == 0 ? "name first" : "name"}>{roommates[entry.userId]?.id + " : " + roommates[entry.userId]?.name}</div>
            <div className={index == 0 ? "score first" : "score"}>{entry.userScore}</div>
          </div>)}
      </div>
    </div>
  )







  

  function getCorrectChoices(){
    let correctChoices = quiz.questions.map((question)=> {
      let qCorrectChoices = []
      question.choices.forEach((choice, index)=>{choice.isCorrect? qCorrectChoices.push(index) : null})
      return qCorrectChoices
    })
    return correctChoices
  }

  /**
   * @param {{}} usersChoices {userId:[questionIndex: [choiceIndex,...], ...]}
   * @param {[]} correctChoices [questionIndex: [choiceIndex,...], ...]
   * @returns {[]} 
   */
  function calculateScores(usersChoices, correctChoices) {
    console.log('calculating scores');
    console.log('usersChoices:', usersChoices);

    Object.keys(usersChoices).forEach(userId => {
      let userChoices = usersChoices[userId] // [questionIndex: choiceIndex, ...]
      let userScore = 0

      userChoices.forEach((userQuestionChoices, ind)=>{
        let questionCorrectChoices = correctChoices[ind]
        console.log('questionCorrectChoices', questionCorrectChoices);
        if (Array.isArray(userQuestionChoices)) {
          let userCorrectQuestionChoices = userQuestionChoices.filter((value)=>questionCorrectChoices.includes(value))
          console.log('userCorrectQuestionChoices', userCorrectQuestionChoices);
          userScore += userCorrectQuestionChoices.length / questionCorrectChoices.length
        }
      })
      usersScores.push({userId, userName:undefined, userScore}) // userId undefined / userName undefined / userScore ok
    })
    console.log(usersScores);
    return usersScores
  }
}