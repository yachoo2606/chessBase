const WebSocket = require('ws');

const PORT = 5555;

function waitForWebSocketConnection(wss) {
  return new Promise((resolve, reject) => {
    wss.on('connection', (ws) => {
      resolve(ws);
    });
  });
}

async function handleWebSocketConnection() {
  const wss = new WebSocket.Server({ port: PORT });
  console.log(`WebSocket server started on port ${PORT}`);

  while(true){
    const client1 = await waitForWebSocketConnection(wss);
    const client2 = await waitForWebSocketConnection(wss);

    console.log('clietsConnected');
    client1.send('gameEstablished')
    client2.send('gameEstablished')
    console.log("message sended to clients");
    // Do something with the client
  }
}

handleWebSocketConnection();

