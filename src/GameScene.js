import Phaser from "phaser";
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

        this.myGrid = this.createGrid(window.innerWidth/20,innerHeight/6,'field')
        this.enemyGrid = this.createGrid(window.innerWidth/20*12,innerHeight/6,'field')
             

        this.exitButton = new TextButton(this,window.innerWidth/2,window.innerHeight,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('menuScene')
        })
        this.add.existing(this.exitButton);
    }

    update() {
        
    }

    createGrid(x,y,texture){
        const toAddGrid = this.add.group();
        for(var i=0;i<11;i++){
            for(var j=0;j<11;j++){
                const sprite = toAddGrid.create(x+(i*50), y+(j*50), texture); sprite.ship=false; sprite.shooted=undefined;
                if(Math.random()>0.5){
                    console.log("statek")
                    sprite.ship = true;
                }
                sprite.setScale(50/sprite.width);
                sprite.setInteractive();
            }
        }
        toAddGrid.children.iterate((sprite)=>{
            sprite.on('pointerdown',()=>{
                if(sprite.shooted==undefined){
                    if(sprite.ship == true){
                        this.sound.play('shootSoundShip',{volume:0.5})
                    }else{
                        this.sound.play('shootSoundWater',{volume:0.5})
                    }  
                }              
                console.log(`Clicket sprite:  x: ${Math.floor((sprite.x-x)/50)} y: ${Math.floor((sprite.y-y)/50)}`,);
            });
        })
        return toAddGrid;
    }
}