import Phaser from "phaser";

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

        const worker = new Worker("./src/network.js");
        worker.postMessage({type:'connect'})

        worker.onmessage = (event) =>{

            if (event.data === 'Connected') {
                console.log("connectedToServer");
            }
            if (event.data === 'gameStart') {
                this.connectionEstablished = true;
            }
        }
    }

    update(){
        if (this.connectionEstablished) {
            this.scene.start('gameScene');
          }
    }

}