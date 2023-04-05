let socket;

const ADDRESS = "127.0.0.1";
const PORT = 5555;

self.onmessage = (event)=>{
    if(event.data.type === 'connect'){
        socket = new WebSocket(`ws://${ADDRESS}:${PORT}`);
        
        socket.addEventListener('open', ()=>{
            console.log('connected to Server')
            self.postMessage('Connected');
        })
        
        socket.addEventListener("message", (event) => {
            console.log("Message from server ", event.data);
            if(event.data === 'gameEstablished'){
                self.postMessage("gameStart")
            }
        });
        
        socket.addEventListener("error", (event)=>{
            console.log("WebSocket error: ", event);
        })
        
    }
}