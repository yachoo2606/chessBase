import Phaser from "phaser";
import { TextButton } from "./TextButton";

export default class GameScene extends Phaser.Scene {
    constructor() {
        super({ key: 'gameScene' });
    }

    preload() {
        // Load any assets needed for the game scene
    }

    create() {

        this.cameras.main.setBackgroundColor("#692137");

        this.exitButton = new TextButton(this,window.innerWidth/2,500,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('menuScene')
        })
        this.add.existing(this.exitButton);
    }

    update() {
        // Update the game world on each frame
    }
}