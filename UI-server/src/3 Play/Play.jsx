import { useLocation, useParams } from "react-router-dom"
import { getSelf, putSelf, validateSelfInDB } from "../functions.mjs"
import { io } from "socket.io-client";
import { CORE_SERVER_URL } from "../values.mjs";
import { ViewLobby } from "./ViewLobby";
import { ViewQuestion } from "./ViewQuestion";
import { ViewResult } from "./ViewResult";
import { useEffect, useState } from "react";
import { ViewError } from "../ViewError";

export const Play = () => {
    const {state} = useLocation()
    const {roomId} = useParams()
    
    const [socket, setSocket] = useState(null)
    const [joined, setJoined] = useState(false)
    const [roommates, setRoommates] = useState({})
    const [self, setSelf] = useState(getSelf())
    const [isAuthorized, setIsAuthorized] = useState(validateSelfInDB(self))
    const isHost = state? true : false
    const gameStates = ['lobby', 'live', 'finished']
    const [gameState, setGameState] = useState(gameStates[0])
    const [quiz] = useState(state?.quiz)
    const [quizLength, setQuizLength] = useState(quiz?.questions.length)
    console.log('quizLength', quizLength);
    const [e, setE] = useState(null)
    const [usersChoices, setUsersChoices] = useState({})
  
    if (isHost && !isAuthorized) { return <ViewError text={"You're not authorized"} />}
    
    useEffect(() =>{
      const usersChoices_temp = {}
      let socket = io(CORE_SERVER_URL)
      setSocket(socket)
  
      let userName = self.name
      let userId = self.id

      socket.emit(isHost? 'create' : 'join', {roomId, userName, userId, quizLength} )

      socket.on('join', ({userName, userId, roommates})=>{setRoommates(roommates)});
      socket.on('leave', ({userName, userId, socketId, roommates})=>{setRoommates(roommates)});
      socket.on('bark', ({msg}) => { setTimeout(()=>{alert(msg)}, 1) } );
      socket.on('create',()=>{});
      socket.on('start',()=>{console.log('Play got start'); setGameState(gameStates[1])})
      // socket.on('next',()=>{console.log('Play got next'); setGameState(gameStates[1])})
      socket.on('end',({})=>{setUsersChoices(usersChoices_temp); setGameState(gameStates[2])})
      socket.on('joined',({roommates, guestId, quizLength, e})=>{
        console.log(quizLength);
        setE(e); putSelf(self), setRoommates(roommates), setQuizLength(quizLength), setJoined(true)
      })
      console.log('isAuthorized', isAuthorized);
      !isAuthorized? socket.on('disconnect',()=>{ delete self.id, putSelf(self)}) : null
  
      // map player's choices for later evaluation
      if (isHost){
        socket.on('choice',({userId, questionInd, choices})=>{
          !usersChoices_temp[userId]? usersChoices_temp[userId] = [] : null
          usersChoices_temp[userId][questionInd] = choices
          console.log('usersChoices:', usersChoices_temp)
        })
      }
      return () => {socket.off(), socket.disconnect();};
    }, []);


    if (!socket || !socket.connected) return <ViewError code={''} text={'failed to connect to the server'}><button onClick={()=>{window.location.reload()}}>retry</button></ViewError>
    if (e) return <ViewError code={404} text={e}/>
    
    return <div className="Play">
      {!joined? <div> failed to join the room. Maybe it doesn't exist </div> : null}
      {joined && gameState === gameStates[0] ? <ViewLobby isHost={isHost} socket={socket} roomId={roomId} /> : null}
      {joined && gameState === gameStates[1] ? <ViewQuestion isHost={isHost} socket={socket} roomId={roomId} quiz={quiz} setGameState={setGameState} quizLength={quizLength}/> : null}
      {joined && gameState === gameStates[2] ? <ViewResult isHost={isHost} socket={socket} roomId={roomId} quiz={quiz} usersChoices={usersChoices} roommates={roommates} /> : null}
      
      {/* <button onClick={()=>{socket.emit('bark', {name: self?.name})}}>bark</button> */}
      <div className="roommates-counter"> connected players: { Object.keys(roommates).length } </div>
      <div className="hstack roommates">{ Object.keys(roommates).map((userId) => <div> {roommates[userId]?.name}</div>) }</div>
    </div>
}