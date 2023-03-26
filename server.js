const WebSocket = require('ws');

const PORT = 5555;

const server = new WebSocket.Server({port:5555});

let player1 = null;
let player2 = null;

server.on('connection', (ws,req)=>{
    console.log("connected user", req.connection.remoteAddress);

    if(player1===null){
        player1 = ws;
    }else{
        player2 = ws;

        player1.send("gameEstablished");
        player2.send("gameEstablished");
        
        ws.on('message', (message) => {
            console.log(`Received message: ${message}`);
        });
        
          // Handle client disconnects
        ws.on('close', () => {
            console.log('Client disconnected.');
        });
    

    }
});