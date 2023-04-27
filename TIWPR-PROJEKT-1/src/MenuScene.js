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
        this.titleText = this.add.text(window.innerWidth/2, window.innerHeight/5, "Battleship",{ fontSize: '120px', fill: '#fff' })
        this.titleText.setOrigin(0.5);

        // Add buttons

        this.playButton = new TextButton(this,window.innerWidth/2,window.innerHeight/2,"Play",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('loadingScene');
        })
        this.add.existing(this.playButton)

        this.clearButton = new TextButton(this,window.innerWidth/2,1.8*window.innerHeight/3,"Clear storage",{ fontSize: '64px', fill: '#fff' }, ()=>{
            localStorage.clear();
        })
        this.add.existing(this.clearButton);

        this.exitButton = new TextButton(this,window.innerWidth/2,window.innerHeight/2+200,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            window.close();
        })
        this.add.existing(this.exitButton);
        
        
    }
    update(){
    }
}