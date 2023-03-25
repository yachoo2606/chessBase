import Phaser, { Game } from "phaser";

export default class Ship extends Phaser.GameObjects.Image{

    constructor(scene,x,y,texture,number){
        super(scene,texture);
        this.setTexture(texture);
        this.setPosition(x, y);
        scene.add.existing(this);

        this.setScale(50/this.width,(50*texture.split("ship")[1])/this.height)
        this.setInteractive();
        scene.input.setDraggable(this)
        
        this.input.dragY = true;
        this.setName(texture+"."+number)
        this.setDepth(2);
    }
}