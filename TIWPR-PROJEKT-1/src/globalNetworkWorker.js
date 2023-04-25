const globalNetworkWorker = new Worker('./src/network.js');

globalNetworkWorker.onmessage = (event) =>{

    if (event.data.type === 'Connected') {
        console.log("Waiting for game start!");
    }

    if(event.data.type === "turn"){
        localStorage.setItem("turn", event.data.value)
        localStorage.setItem("playerNumber", event.data.value)
        localStorage.setItem("GAMEID", event.data.gameID)
    }

    if (event.data.type === 'gameEstablished') {
        if(event.data.value){
            localStorage.setItem("gameScene", true)
        }
    }

    if (event.data.type === 'checkShoot') {
        localStorage.setItem("checkShoot", JSON.stringify({x:event.data.x, y:event.data.y}))
    }
    if (event.data.type === "shootCheked"){
        localStorage.setItem("markEnemyBorad", event.data.value);
    }
}

export default globalNetworkWorker;