let socket;

this.turn;

const ADDRESS = "127.0.0.1";
const PORT = 5555;

self.onmessage = (event)=>{
    if(event.data.type === 'connect'){
        socket = new WebSocket(`ws://${ADDRESS}:${PORT}`);
        
        socket.addEventListener('open', ()=>{
            console.log('connected to Server')
            self.postMessage({type:"Connected", value:true});
        })
        socket.addEventListener("message", (event) => {
            console.log("Message from server ", event.data);
            const datas = event.data.split("|")
            
            if(datas[0]=="GameStart"){
                
                console.log(datas)
                if(datas[2]==="1") self.postMessage({type:"turn", value:"true", gameID: datas[1]})
                if(datas[2]==="0") self.postMessage({type:"turn", value:"false", gameID: datas[1]})
                
                self.postMessage({type:"gameEstablished", value:true})
                
            }else if(datas[0] == "shoot"){
                console.log("got cords to check: ",JSON.parse(datas[1]))
                self.postMessage({type:"checkShoot", x:JSON.parse(datas[1]).x,y:JSON.parse(datas[1]).y})
            }else if(datas[0]=="shootCheked"){
                console.log("got cords to check: ",JSON.parse(datas[1]))
                self.postMessage({type:"shootCheked",value:datas[1]})
            }
        });
        socket.addEventListener("error", (event)=>{
            console.log("WebSocket error: ", event);
        })
        
    }else if(event.data.type == "shoot"){
        console.log("sending coords to opponent:", event.data.coords)
        socket.send("shoot|"+JSON.stringify(event.data.coords))
    }
    else if(event.data.type == "shootChecked"){
        socket.send("shootCheked|"+event.data.ship)
    }
    // else if(event.data.type == "allPlaced"){
    //     socket.send(JSON.stringify(""))
    // }
}