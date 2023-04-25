import Phaser from "phaser";
import globalNetworkWorker from "./globalNetworkWorker"


export default class LoadingScene extends Phaser.Scene{
    
    constructor(){
        super({key:'loadingScene'});
        this.connectionEstablished = false;
    }

    preload(){
    }

    create(){
        this.cameras.main.setBackgroundColor("#692137")
        
        this.waitingText = this.add.text(this.cameras.main.centerX, this.cameras.main.centerY,"Waiting for opponent....",{ fontSize: '64px', fill: '#fff' });
        this.waitingText.x = this.waitingText.x - this.waitingText.width/2;
        
        if(localStorage.getItem("GAMEID")){
            console.log({type:'Reconnect', gid: localStorage.getItem("GAMEID"),playerNumber: localStorage.getItem("playerNumber")})
            if(localStorage.getItem("playerNumber")=="true"){
                globalNetworkWorker.postMessage({type:'Reconnect', gid: localStorage.getItem("GAMEID"),playerNumber: 1})
            }else{
                globalNetworkWorker.postMessage({type:'Reconnect', gid: localStorage.getItem("GAMEID"),playerNumber: 0})
            }
            
        }else{
            console.log({type:'connect'})
            globalNetworkWorker.postMessage({type:'NewGame'})
        }
        // localStorage.clear();

    }

    update(){
        if (localStorage.getItem("ResumeGame")) {
            this.scene.start('gameScene');
            localStorage.removeItem("ResumeGame");
        }
        if (localStorage.getItem("gameScene")) {
            this.scene.start('gameScene');
            localStorage.removeItem("gameScene");
        }
    }

}