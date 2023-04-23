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

        globalNetworkWorker.postMessage({type:'connect'})

        localStorage.clear();

    }

    update(){
        if (localStorage.getItem("gameScene")) {
            this.scene.start('gameScene');
            localStorage.removeItem("gameScene");
        }
    }

}