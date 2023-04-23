import Phaser, { Game } from "phaser";

export default class Ship extends Phaser.GameObjects.Image{

    constructor(scene,x,y,texture,number){
        super(scene,texture);

        this.originalCords = {x:x,y:y};
        this.placed = false;
        this.scaleVar = 45;
        this.setDepth(1);
        this.setTexture(texture);
        this.setPosition(x, y);
        scene.add.existing(this);

        this.setScale(this.scaleVar/this.width,(this.scaleVar*texture.split("ship")[1])/this.height)
        this.setInteractive();
        scene.input.setDraggable(this)
        
        this.input.dragY = true;
        this.setName(texture+"."+number)
        this.setDepth(2);

        this.fieldsOfShip = {
            name:this.name,
            coordinates:[]
        }
        this.sunk = false;
        this.lives = parseInt(texture.split("ship")[1])
    }
}