import Phaser from "phaser";
import Grid from "./Grid";
import Ship from "./Ship";
import { TextButton } from "./TextButton";

export default class GameScene extends Phaser.Scene {
    constructor() {
        super({ key: 'gameScene' });
    }

    preload() {
        this.load.image("field", "../assets/square.png");
        this.load.image("ship1", "../assets/ship1.png",{ crossOrigin: 'anonymous' });
        this.load.image("ship2", "../assets/ship2.png",{ crossOrigin: 'anonymous' });
        this.load.image("ship3", "../assets/ship3.png",{ crossOrigin: 'anonymous' });
        this.load.image("ship4", "../assets/ship4.png",{ crossOrigin: 'anonymous' });
        this.load.audio('shootSoundWater',"../assets/audio/splashing-water-fx.wav");
        this.load.audio('shootSoundShip',"../assets/audio/explosion_F_minor.wav");
    }

    create() {

        this.cameras.main.setBackgroundColor("#692137");

        this.myGrid = new Grid(this,window.innerWidth/20,innerHeight/6,'field',false)
        this.enemyGrid = new Grid(this,window.innerWidth/20*12,innerHeight/6,'field',true)
        
        this.ship11 = new Ship(this,400,400,'ship1');
        this.ship12 = new Ship(this,400,450,'ship1');
        this.ship13 = new Ship(this,400,500,'ship1');
        this.ship14 = new Ship(this,400,550,'ship1');

        this.ship21 = new Ship(this,500,400,'ship2')
        this.ship22 = new Ship(this,500,500,'ship2')
        this.ship23 = new Ship(this,500,600,'ship2')

        this.ship31 = new Ship(this,600,500,'ship3')
        this.ship32 = new Ship(this,600,650,'ship3')

        this.ship4 = new Ship(this,700,500,'ship4')
        

        this.exitButton = new TextButton(this,window.innerWidth/2,window.innerHeight,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('menuScene')
        })
        this.add.existing(this.exitButton);
    }

    update() {
    }

    
}