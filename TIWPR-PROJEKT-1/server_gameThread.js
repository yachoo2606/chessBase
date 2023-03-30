const { Socket } = require('dgram');
const { parentPort, workerData } = require('worker_threads');

const sockets = [];

parentPort.on('message', (message) => {
  if (message.type === 'socketFds') {
    console.log('Received sockets from main thread:', message.socketFds);

    // Add the socket to the array
    for (let i = 0; i < message.socketFds.length; i++) {
        const socket = new net.Socket({ fd: message.socketFds[i], readable: true, writable: true });
        sockets.push(socket);
    }

    // When both sockets are added to the array, start the game
    if (sockets.length === 2) {
        console.log('Both sockets have been received. Starting game.');
        
    }
  }
});


// player1.send("gameEstablished");
// player2.send("gameEstablished");

