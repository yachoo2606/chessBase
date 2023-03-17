import Phaser from "phaser";
import { TextButton } from "./TextButton";

export default class MenuScene extends Phaser.Scene{
    constructor(){
        super({key:'menuScene'})
    }
    preload(){
    }
    create(){       
        //TITLE
        this.cameras.main.setBackgroundColor("#213769");
        this.titleText = this.add.text(window.innerWidth/2, window.innerHeight/10, "WarShips",{ fontSize: '120px', fill: '#fff' })
        this.titleText.setOrigin(0.5);

        // Add buttons

        this.playButton = new TextButton(this,window.innerWidth/2,300,"Play",{ fontSize: '64px', fill: '#fff' },()=>{
            // alert("Play");
            this.scene.start('gameScene');
        })
        this.add.existing(this.playButton)

        this.exitButton = new TextButton(this,window.innerWidth/2,500,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            window.close();
        })
        this.add.existing(this.exitButton);
        
        
    }
    update(){
    }
}