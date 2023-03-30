let socket;


self.onmessage = (event)=>{
    if(event.data.type === 'connect'){
        socket = new WebSocket('ws://127.0.0.1:5555');
        
        socket.addEventListener('open', ()=>{
            // self.postMessage('connnected');
            self.postMessage('Connected');
        })
        
        socket.addEventListener("message", (event) => {
            console.log("Message from server ", event.data);
            if(event.data === 'gameEstablished'){
                self.postMessage("gameStart")
            }else{
                console.log("waiting For game");
            }
        });
        
        socket.addEventListener("error", (event)=>{
            console.log("WebSocket error: ", event);
        })
        
    }
}