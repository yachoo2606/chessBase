const WebSocket = require('ws');
const {Worker} = require('worker_threads');

const PORT = 5555;

const server = new WebSocket.Server({port:PORT});

server.on('connection',(ws,req)=>{
    console.log("connected user", req.connection.remoteAddress);
})

server.on('request', req => {
    const player = req.accept(null, req.origin);
    console.log("open user", req.connection.remoteAddress);

    // if(player1===null){
    //     player1 = ws;
    // }else{
    //     player2 = ws;

    //     player1.send("gameEstablished");
    //     player2.send("gameEstablished");
        
    //     ws.on('message', (message) => {
    //         console.log(`Received message: ${message}`);
    //     });
    // }
});