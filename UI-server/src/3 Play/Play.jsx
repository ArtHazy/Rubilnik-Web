import { useLocation, useParams } from "react-router-dom"
import { getSelf, putSelf } from "../functions.mjs"
// import { io } from "socket.io-client";
import { SERVER_URL } from "../values.mjs";
import { ViewLobby } from "./ViewLobby";
import { ViewQuestion } from "./ViewQuestion";
import { ViewResult } from "./ViewResult";
import { useState } from "react";

export const Play = () => {
    const {state} = useLocation()
    const {roomId} = useParams()
    
    // const [socket, setSocket] = useState(null)
    const [socket, setSocket] = useState({connected:true}) //! FOR TEST
    // const [joined, setJoined] = useState(false)
    const [joined, setJoined] = useState(true) //! FOR TEST
    const [roommates, setRoommates] = useState({})

    const isHost = state? true: false
    const gameStates = ['lobby', 'live', 'finished']
    const [gameState, setGameState] = useState(gameStates[0])
    const [quizLength, setQuizLength] = useState(0)

    const [quiz] = useState(state?.quiz)
    
    const self = getSelf()
    // const isAuthorized = Boolean(self.password) //!

    // useEffect(() =>{
    //     let usersChoices = {}
    //     let socket = io(SERVER_URL)
    //     setSocket(socket)
    
    //     let userName = self.name
    //     let userId = self.id

    //     socket.emit(isHost? 'create' : 'join', {roomId, userName, userId} )

    //     socket.on('join', ({userName, userId, roommates})=>{setRoommates(roommates)});
    //     socket.on('leave', ({userName, userId, socketId, roommates})=>{setRoommates(roommates)});
    //     socket.on('bark', ({msg}) => { setTimeout(()=>{alert(msg)}, 1) } );
    //     socket.on('create',()=>{});
    //     socket.on('start',()=>{setGameState(gameStates[1])})
    //     socket.on('next',()=>{setGameState(gameStates[1])})
    //     socket.on('end',({})=>{setGameState(gameStates[2])})
    //     socket.on('joined',({roommates, guestId, quizLength})=>{
    //       self?.id? self.id = guestId : null
    //       putSelf(self), setRoommates(roommates), setQuizLength(quizLength), setJoined(true)
    //     })
    //     !isAuthorized? socket.on('disconnect',()=>{delete self.id, putSelf(self)}) : null
    
    //     // map player's choices for later evaluation
    //     if (isHost){
    //       socket.on('choice',({userId, questionInd, choices})=>{
    //         !usersChoices[userId]? usersChoices[userId] = [] : null
    //         usersChoices[userId][questionInd] = choices
    //         console.log('usersChoices:', usersChoices)
    //       })
    //     }

    //     return () => {socket.off(), socket.disconnect();};
    // }, []);

    if (!socket || !socket.connected)
      return <div>failed to connect<button onClick={()=>{window.location.reload()}}>retry</button></div>
    else 
      return <div className="Play">
        {!joined? <div> failed to join the room. Maybe it doesn't exist </div> : null}
        {joined && gameState === 'lobby' ? <ViewLobby isHost={isHost} socket={socket} roomId={roomId} /> : null}
        {joined && gameState === 'in progress' ? <ViewQuestion isHost={isHost} socket={socket} roomId={roomId} quiz={quiz} setGameState={setGameState} quizLength={quizLength}/> : null}
        {joined && gameState === 'finished' ? <ViewResult isHost={isHost} socket={socket} roomId={roomId} quiz={quiz} usersChoices={usersChoices} roommates={roommates} /> : null}
        
        <button onClick={()=>{socket.emit('bark', {userName: user?.name, guestName: guest?.name})}}>bark</button>
        <div className="roommates-counter"> connected players: { Object.keys(roommates).length } </div>
        <div className="hstack roommates">{ Object.keys(roommates).map((userId) => <div> {roommates[userId]?.name}</div>) }</div>
      </div>
}