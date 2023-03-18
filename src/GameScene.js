import Phaser from "phaser";
import Grid from "./Grid";
import { TextButton } from "./TextButton";

export default class GameScene extends Phaser.Scene {
    constructor() {
        super({ key: 'gameScene' });
    }

    preload() {
        this.load.image("field", "../assets/square.png");
        this.load.audio('shootSoundWater',"../assets/audio/splashing-water-fx.wav");
        this.load.audio('shootSoundShip',"../assets/audio/explosion_F_minor.wav");
    }

    create() {

        this.cameras.main.setBackgroundColor("#692137");

        this.myGrid = new Grid(this,window.innerWidth/20,innerHeight/6,'field')
        this.enemyGrid = new Grid(this,window.innerWidth/20*12,innerHeight/6,'field')
             

        this.exitButton = new TextButton(this,window.innerWidth/2,window.innerHeight,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('menuScene')
        })
        this.add.existing(this.exitButton);
    }

    update() {
    }

    
}