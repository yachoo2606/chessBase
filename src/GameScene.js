import Phaser from "phaser";
import Grid from "./Grid";
import Ship from "./Ship";
import { TextButton } from "./TextButton";

export default class GameScene extends Phaser.Scene {
    constructor() {
        super({ key: 'gameScene' });
    }

    preload() {

        this.load.scenePlugin({
            key: 'ArcadePhysics',
            sceneKey: 'physics',
            url: Phaser.Physics.Arcade.ArcadePhysics
        });

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
        
        this.ships = []
        
        this.ships.push(new Ship(this,700,200,'ship1',1))
        this.ships.push(new Ship(this,700,250,'ship1',2))
        this.ships.push(new Ship(this,700,300,'ship1',3))
        this.ships.push(new Ship(this,700,350,'ship1',4))

        this.ships.push(new Ship(this,750,200,'ship2',1))
        this.ships.push(new Ship(this,750,300,'ship2',2))
        this.ships.push(new Ship(this,750,400,'ship2',3))

        this.ships.push(new Ship(this,800,200,'ship3',1))
        this.ships.push(new Ship(this,800,350,'ship3',2))

        this.ships.push(new Ship(this,850,200,'ship4',1))

        this.ships.forEach(ship => {
            ship.on("dragstart",(pointer, gameObject)=>{
                console.log("dragstart",ship.name, pointer,gameObject);
                if(pointer.rightButtonDown()){
                    if(ship.angle == 90){
                        ship.setAngle(0);
                    }else{
                        ship.setAngle(90);
                    }
                }
            })
            ship.on('drag', (pointer, dragX, dragY) => {
                ship.x = dragX;
                ship.y = dragY;
            });
            ship.on('drop', (pointer, gameObject,target) => {     
                let canBePlaced = true;           
                this.myGrid.grid.children.iterate(sprite =>{
                    if(Phaser.Geom.Intersects.RectangleToRectangle(ship.getBounds(),sprite.getBounds())){
                        // console.log("kolizja", gameObject.coordinates ,sprite.coordinates, sprite.canBePlaced)
                        if(sprite.canBePlaced){
                            ship.fieldsOfShip.coordinates.push(sprite.coordinates)
                        }else{
                            canBePlaced = false;
                        }
                    }
                })
                if(canBePlaced && (ship.lives == ship.fieldsOfShip.coordinates.length) ){
                    // console.log("ship: "+ship.name+" placed", ship.fieldsOfShip,"lives:"+ship.lives);
                    this.myGrid.grid.children.iterate(sprite=>{
                        ship.fieldsOfShip.coordinates.forEach(coords =>{
                            if(sprite.coordinates == coords){
                                sprite.canBePlaced = false;
                                ship.removeAllListeners();
                                ship.setDepth(0);
                                // console.log(sprite.coordinates, coords)
                            }
                            
                            if(sprite.coordinates.y == coords.y-1 && 
                                (sprite.coordinates.x == coords.x-1 ||
                                 sprite.coordinates.x == coords.x ||
                                 sprite.coordinates.x == coords.x+1)){
                                    sprite.canBePlaced = false;
                                }
                            if(sprite.coordinates.y == coords.y && 
                                (sprite.coordinates.x == coords.x-1 ||
                                 sprite.coordinates.x == coords.x+1)){
                                        sprite.canBePlaced = false;
                                    } 
                            if(sprite.coordinates.y == coords.y+1 && 
                                (sprite.coordinates.x == coords.x-1 ||
                                 sprite.coordinates.x == coords.x ||
                                 sprite.coordinates.x == coords.x+1)){
                                    sprite.canBePlaced = false;
                            } 

                        })
                    })
                }else{
                    ship.x = ship.originalCords.x;
                    ship.y = ship.originalCords.y;
                    ship.fieldsOfShip = {
                        name:ship.name,
                        coordinates:[]
                    }
                }
            });
        })

        this.exitButton = new TextButton(this,window.innerWidth/2,window.innerHeight,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('menuScene')
        })
        this.add.existing(this.exitButton);
    }

    update() {
    }

    
}