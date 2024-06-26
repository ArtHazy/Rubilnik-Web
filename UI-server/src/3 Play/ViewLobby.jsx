export const ViewLobby = ({isHost, roomId, socket}) => {
  console.log('socket', socket);

  if (socket && socket.connected) 
    return <div className="ViewLobby">
      <div id="qr-container"><img src={"https://api.qrserver.com/v1/create-qr-code/?size=128x128&data="+roomId}/></div>
      <div className="id">{roomId}</div>
      <div className="text">connection code</div>
      {isHost? <button className="start big" onClick={() => {socket.emit('start', {roomId})}}>START</button> : null} 
    </div>
  else 
    return <div>Failed to connect socket</div>
}