// ESSENTIALS
import express from 'express'; 
import path from 'path';
import { fileURLToPath } from 'url';
import cors from 'cors';
import http from 'http';

const app = express();
const server = http.createServer(app);
const __dirname = path.dirname(fileURLToPath(import.meta.url));

app.use(express.json());
app.use(cors());

server.listen(3000, () => {
  console.log('Server listening on http://localhost:3000');
});

// USEFULL
import {
  doesJsonHave, handleMissingProperties, reqNotifier,
} from "./tools.mjs"

app.get('/documentation', (req, res) => {
  res.sendFile(path.join(__dirname,'documentation.html'))
})
app.get('/banner', (req,res)=>{
  reqNotifier(req)
  res.sendFile(path.join(__dirname, 'banner/banner.html'));
})
app.get('/Screenshot_7-4-2024_225737_192.168.0.138.jpeg', (req,res)=>{
  reqNotifier(req)
  res.sendFile(path.join(__dirname, 'banner/Screenshot_7-4-2024_225737_192.168.0.138.jpeg'));
})
app.get('/2024-Rubilnik-Banner_unfinished__9.glb', (req,res)=>{
  reqNotifier(req)
  res.sendFile(path.join(__dirname, 'banner/2024-Rubilnik-Banner_unfinished__9.glb'));
})


// Prisma DB functions
import {
  deleteAllUsers,createUser,deleteUserById,getUser, putUser
} from "./prisma/_userFunctions.mjs";
import { 
  updateUsersQuizzes, 
} from './prisma/_quizFunctions.mjs';


import {
  IdTree
} from "./idTree.mjs";
import { initSocket } from './socket.mjs';

async function clearDB(){ await deleteAllUsers() }

// At Start
clearDB()
var userIds = new IdTree(4)
let io = initSocket(server,app)


app.post('/user/verify',(req,res)=>{
  reqNotifier(req);
  if (doesJsonHave(req.body, handleMissingProperties, 'email', 'password')){
    try {
      getUser(req.body.email).then(result=>{
        if (result){
          if (result.password==req.body.password) res.status(200).json(result)
          else res.status(400).json({msg: 'Wrong password'})
        } else res.status(400).json({msg:'Failed to get'})
      })
    } catch (error) { res.status(500).json({msg:"Server error"}) }
  }
})
app.post('/user',(req,res)=>{
  reqNotifier(req);
  if (doesJsonHave(req.body, handleMissingProperties, 'name','email','password')){
    try {
      var userId = userIds.getFreeId();
      if (userId) {
        try {
          createUser(userId,{...req.body},[]).then(result=>{
            if (result) res.status(200).json(result) 
            else res.status(400).json({msg:'Failed to create'})
          })
        } catch (e) {console.log(e);}
      }else{
        res.status(500).json({msg:"Failed to create: ran out of id's"})
      }
    } catch (error) { res.status(500).json({msg:"Server error"}) }
  }
})
app.put('/user',(req,res)=>{
  reqNotifier(req);
  if (doesJsonHave(req.body, handleMissingProperties, 'email')){
    try{
      putUser(req.body.email, req.body).then(result=>{
        if (result) res.status(200).json(result)
        else res.status(400).json({msg:'Failed to put'})
      })
    } catch (e) { res.status(500).json({msg:"Server error"}) }
  }
})
// TODO
app.delete('/user',(req,res)=>{
  reqNotifier(req);
  if (doesJsonHave(req.body,handleMissingProperties,'id')){
    deleteUserById(req.body.id).then(result=>{
      if (result) {
        userIds.deleteId(req.body.id)
        res.status(200).json({msg: 'Success'})
      } else res.status(400).json({msg:'Failed to delete'})
    }).catch((e)=>{ res.status(500).json({msg: 'Internal error'})})
  }
})
app.post('/user/quizzes',(req,res)=>{
  reqNotifier(req);
  if (doesJsonHave(req.body,handleMissingProperties,'userId','quizzes')){
    updateUsersQuizzes(req.body.userId,req.body.quizzes).then(result=>{
      if (result) res.status(200).json({msg:'Success'})
      else res.status(400).json({msg: "Failed to update"})
    })
  }
})
app.post('/user/verify',(req,res)=>{
  reqNotifier(req)
  try{
    if (!(req.body.email && req.body.password)) throw new Error();
    getUser(req.body.email).then((user)=>{
      if (user && user.password == req.body.password) res.status(200).send()
      else throw new Error();
    })
  } catch(e) { return res.status(404).json({msg: e.message}) }  
})
app.post('/checkRoomAvailability',(req,res)=>{
  reqNotifier(req)
  let roomId = req.body.roomId
  let room = io.sockets.adapter.rooms.get(roomId)
  if (room) {res.status(200).send()} 
  else {res.status(404).send()}
})