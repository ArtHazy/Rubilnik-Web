import { Server } from 'socket.io';

export function initSocket(server){
    console.log('initSocket');
    const io = new Server(server, {cors: {origin: "*"}});
    const roomsData = new Map();

    io.on('connection', (socket) => {
        console.log(`socket connect recieved: ${socket.id}`);
        logRooms()
        socket.on('create', ({roomId, userName, userId, quizLength}) => {
            console.log('create ','quizLength', quizLength );
            const roomData = {host: socket, quizLength: quizLength} 
            roomsData.set(roomId, roomData)

            console.log(`create received for room "${roomId}"`);
            socket.data = {userId, userName};
            socket.join(roomId);
            let roommates = getRoommates(roomId)
            io.to(roomId).emit('create',{})
            io.to(roomId).emit('joined',{roommates, quizLength})
            logRooms()
        })

        socket.on('bark',({name})=>{
            socket.rooms.forEach((roomId)=>{
                io.to(roomId).emit('bark',{msg:name+' barked'})
            })
        })

        socket.on('join', ({roomId, userName, userId}) => {
            // parse arrays from mobile JSON ))))))))))))))
                if (Array.isArray(roomId)){roomId = roomId[0];}
                if (Array.isArray(userName)){userName = userName[0];}
                if (Array.isArray(userId)){userId = userId[0];}
                
            console.log(roomId, userName, userId);

            logRooms()
            userId? null : (userId = socket.id, console.log('no userId!'))
            console.log(`join received from user ${userId} ${userName} to room: "${roomId}"`);
            
            socket.data = {userId, userName};

            
            console.log(io.sockets.adapter.rooms.has(roomId));
            console.log(io.sockets.adapter.rooms);
        
            if (io.sockets.adapter.rooms.has(roomId)){
                socket.join(roomId)
                let roommates = getRoommates(roomId)

                console.log('roommates', roommates );
                console.log(socket.rooms);
            
                io.to(roomId).emit('join',{userName, userId, roommates})
                socket.emit('joined',{roommates, guestId: userId, quizLength: roomsData.get(roomId)?.quizLength })
            } else {
                let e = `${userName} failed to join. Room "${roomId}" does not exist`
                console.log(e);
                socket.data = {userId, userName};
                socket.emit('joined', {e})
            }
            logRooms()
        })
      
        socket.on('start', ({roomId}) => {
            console.log(`start received for room: "${roomId}"`)
            io.to(roomId).emit('start',{})
        })
      
        socket.on('choice', ({roomId, userId, userName, questionInd, choices}) => {
            !userId? userId = socket.id : null
            console.log(`choice received for room: "${roomId}" from user ${userId} on question ${questionInd} with choice: ${choices}`)
            io.to(roomId).emit('choice',{userId, userName, questionInd, choices})
        })
      
        socket.on('next', ({roomId, questionInd, question}) => {
            console.log(`next received for room: "${roomId}" with question (# ${questionInd}): ${question.text}  with choices: ${question.choices}`)
            io.to(roomId).emit('next',{question, questionInd})
        })
      
        socket.on('end', ({roomId}) => {
            console.log(`end received for room: "${roomId}"`);
            io.to(roomId).emit('end',{})
        })
      
        socket.on('scores', ({roomId, usersScores}) =>{
            console.log(`result received for room: "${roomId}"`);
            io.to(roomId).emit('scores',{usersScores})
        })
      
        socket.on('reveal', ({roomId, correctChoicesInd}) => {
            console.log(`reveal received for room: "${roomId}" choiceInd: ${correctChoicesInd}`); 
            io.to(roomId).emit('reveal',{correctChoicesInd})
        })
      
        socket.on('disconnecting', () => {
            console.log(`socket disconnect recieved: ${socket.id}`)
            console.log('socket rooms', socket.rooms)
      
            socket.rooms.forEach((roomId)=>{
                console.log('emit leave to', roomId)
                let roommates = getRoommates(roomId)
                
                roommates[socket.id] = undefined
                io.to(roomId).emit('leave', {userId: socket.data.userId, userName: socket.data.userName, socketId: socket.id, roommates})
            })
      
            socket.on('disconnect', () =>{
                logRooms()
            })
      
        });
    });

    function logRooms(){
        io.sockets.adapter.rooms.forEach((clients, roomId)=>{
            console.log('Room', roomId, ":", clients);
        })
    }
    /**
     * @param {string} roomId 
     * @returns {{}} roommates {userId?:string : {name?:string}, ...}
     */
    function getRoommates(roomId){
        let roommates = {}
        let room = io.sockets.adapter.rooms.get(roomId);
        room.forEach((socketId)=>{
            let socket = io.sockets.sockets.get(socketId);
            let id = socket.data.userId;
            let name = socket.data.userName;
            roommates[id] = {name}
        })
        return roommates
    }
    return io
}