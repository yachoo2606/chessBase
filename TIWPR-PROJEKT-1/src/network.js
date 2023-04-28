let socket;

const ADDRESS = "127.0.0.1";
const PORT = 5555;

function toHex(bytes,offset){
    return Array.prototype.map.call(new Uint8Array(bytes.slice(offset)), x => ('00' + x.toString(16)).slice(-2)).join('');
}

function hexToBytes(hex) {
    let bytes = [];
    for (let c = 0; c < hex.length; c += 2)
        bytes.push(parseInt(hex.substr(c, 2), 16));
    return bytes;
}

function connect(mode){
    socket = new WebSocket(`ws://${ADDRESS}:${PORT}`);
    
        socket.addEventListener('open', ()=>{
            console.log('connected to Server')
            self.postMessage({type:"Connected", value:true});
            socket.send(mode)
        })
        socket.addEventListener("message", (event) => {
            console.log("Message from server ", event.data);
            let datas;            
            new Response(event.data).arrayBuffer()
            .then((res =>{
                console.log("arrayBuffer", res);
                const byteView = new Uint8Array(res);
                console.log(byteView)
                datas = new DataView(res);
                // if(datas[0]=="GameStart"){
                if(datas.getUint16(0,false)===0){
                    
                    console.log("datas: ")
                    console.log(datas)
                    console.log(datas.getUint16(0,false))
                    console.log(datas.getUint16(2,false))
                    console.log(datas.buffer.slice(4))

                    const GUID = toHex(datas.buffer,4)
                    if(datas.getUint16(2,false)===1) self.postMessage({type:"turn", value:"true", gameID: GUID})
                    if(datas.getUint16(2,false)===0) self.postMessage({type:"turn", value:"false", gameID: GUID})
                    self.postMessage({type:"gameEstablished", value:true})
                    
                }else if(datas.getUint8(0,false) == 2){
                    console.log("checkShoot: ",{x:datas.getUint8(1,false),y:datas.getUint8(2,false)})
                    self.postMessage({type:"checkShoot", x:datas.getUint8(1,false),y:datas.getUint8(2,false)})
                }else if(datas.getUint8(0,false) == 3){
                    console.log("shootCheked: ",datas.getUint8(1,false))
                    self.postMessage({type:"shootCheked",value:datas.getUint8(1,false)})
                }else if(datas.getUint16(0,false) == 4){
                    console.log("PLAYER DISCONNECTED")
                    self.postMessage({type:"DISCONNECTED"})
                }
            })
            );
        });
        socket.addEventListener("error", (event)=>{
            console.log("WebSocket error: ", event);
        })
}

self.onmessage = (event)=>{
    if(event.data.type === 'NewGame'){

        const buffer = new ArrayBuffer(8);
        const view = new DataView(buffer);
        view.setInt8(0,0);
        connect(buffer);
    }else if(event.data.type == "Reconnect"){
        console.log("ATEMPTING RECONNECTING",1,event.data.playerNumber,event.data.gid)
        console.log("ATEMPTING RECONNECTING",1,event.data.playerNumber,hexToBytes(event.data.gid))
        const buffer = new ArrayBuffer(18);
        const view = new DataView(buffer);
        view.setInt8(0,1);
        view.setInt8(1,event.data.playerNumber)
        const bytes = new Uint8Array(event.data.gid.match(/.{1,2}/g).map(byte => parseInt(byte, 16)));
        console.log("bytes: ", bytes, bytes.byteLength)

        for (let i = 0; i < bytes.length; i++) {
            view.setUint8(i+2, bytes[i]);
        }

        console.log("RECONNECTING", view)
        connect(buffer);

    }else if(event.data.type == "shoot"){
        console.log("sending coords to opponent:", event.data.coords)
        const buffer = new ArrayBuffer(3);
        const view = new DataView(buffer);
        view.setInt8(0,2);
        view.setInt8(1,event.data.coords.x);
        view.setInt8(2,event.data.coords.y);
        socket.send(view)
    }
    else if(event.data.type == "shootChecked"){
        const buffer = new ArrayBuffer(3);
        const view = new DataView(buffer);
        view.setInt8(0,3);
        if(event.data.ship){
            view.setInt8(1,1);
        }else{
            view.setInt8(1,0);
        }
        socket.send(view)
    }
    else if(event.data.type == "allPlaced"){
        socket.send(JSON.stringify("allplaced"))
    }
    
}
/*
    0 - NEW GAME
    1 - RECONNECT (1,)
    2 - shoot (2,x,y)
    3 - shootcheked (3,t/f)
    4 - GAME END
*/