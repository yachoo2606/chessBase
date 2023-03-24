import Phaser, { Game } from "phaser";

export default class Ship extends Phaser.GameObjects.Image{

    constructor(scene,x,y,texture){
        super(scene,texture);
        this.setTexture(texture);
        this.setPosition(x, y);
        scene.add.existing(this);

        this.setScale(50/this.width,(50*texture.split("ship")[1])/this.height)
        this.setInteractive();
        scene.input.setDraggable(this)
        this.input.dragY = true;

        this.on("dragstart",(pointer, gameObject)=>{
            console.log("dragstart", pointer,gameObject);
            if(pointer.rightButtonDown()){
                if(this.angle == 90){
                    this.setAngle(0);
                }else{
                    this.setAngle(90);
                }
            }
        })
        this.on('drag', (pointer, dragX, dragY) => {
            this.x = dragX;
            this.y = dragY;
        });
        this.on('dragend', (pointer, gameObject) => {
            if (pointer.leftButtonReleased()) {
                console.log("dragEnd")
            }
        });
        
    }
}