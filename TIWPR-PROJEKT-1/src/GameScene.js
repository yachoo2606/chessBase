import Phaser from "phaser";
import Grid from "./Grid";
import Ship from "./Ship";
import { TextButton } from "./TextButton";
import globalNetworkWorker from "./globalNetworkWorker";

export default class GameScene extends Phaser.Scene {
    constructor() {
        super({ key: 'gameScene' });
        this.allShipPlaced = false;
        this.myLifes = 20;
        this.oponentsLifes = 20;
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
                        console.log("kolizja", gameObject.coordinates ,sprite.coordinates, sprite.canBePlaced)
                        if(sprite.canBePlaced){
                            ship.fieldsOfShip.coordinates.push({original: {x:sprite.x,y:sprite.y}, logical:sprite.coordinates})
                        }else{
                            canBePlaced = false;
                        }
                    }
                })
                if(canBePlaced && (ship.lives == ship.fieldsOfShip.coordinates.length) ){
                    // console.log("ship: "+ship.name+" placed", ship.fieldsOfShip,"lives:"+ship.lives);
                    ship.placed = true;
                    this.myGrid.grid.children.iterate(sprite=>{
                        ship.fieldsOfShip.coordinates.forEach(coords =>{
                            if(sprite.coordinates == coords){
                                sprite.canBePlaced = false;
                                ship.removeAllListeners();
                                ship.input.draggable = false;
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
        if(localStorage.getItem('savedSceneObjects')){
            const savedSceneData = JSON.parse(localStorage.getItem('savedSceneObjects'));
            this.myLifes = savedSceneData.myLifes
            this.oponentsLifes = savedSceneData.oponentsLifes
            this.allShipPlaced = true;
            savedSceneData.ships.forEach((ship,index) =>{
                this.ships[index].x = ship.x;
                this.ships[index].y = ship.y;
                this.ships[index].setAngle(ship.angle);
                this.ships[index].fieldsOfShip = ship.fieldsOfShip;
            })
            localStorage.setItem("turn", savedSceneData.turn)
            savedSceneData.myGridGeoms.forEach(geom=>{
                if(geom.type=="line"){
                    this.myGrid.setLine(geom.x,geom.y);
                }else{
                    this.myGrid.setCircle(geom.x,geom.y,geom.r);
                }
            })
            savedSceneData.enemyGridGeoms.forEach(geom=>{
                if(geom.type=="line"){
                    this.enemyGrid.setLine(geom.x,geom.y);
                }else{
                    this.enemyGrid.setCircle(geom.x,geom.y,geom.r);
                }
            })

        }
    }

    saveSceneData(){
        let gameDataObj={
            ships:[],
            myGridGeoms:this.myGrid.geoms,
            enemyGridGeoms:this.enemyGrid.geoms,
            graphics:this.graphics,
            myLifes:this.myLifes,
            oponentsLifes:this.oponentsLifes,
            turn:localStorage.getItem("turn")
        }
        this.ships.forEach((ship)=>{
            gameDataObj.ships.push({
                x:ship.x,
                y:ship.y,
                angle:ship.angle,
                fieldsOfShip:ship.fieldsOfShip
            })
        })

        localStorage.setItem('savedSceneObjects',JSON.stringify(gameDataObj));

    }

    checkShoot(x,y){

        for(let ship of this.ships){
            for(let field of ship.fieldsOfShip.coordinates){
                if(field.logical.x === x && field.logical.y === y){
                    this.myGrid.setCrossOrCircle(true,x, y)
                    globalNetworkWorker.postMessage({type:"shootChecked",ship:true})
                    this.myLifes -=1;
                    localStorage.setItem("turn","true");
                    this.saveSceneData()
                    return;
                }
            }
        }
        this.myGrid.setCrossOrCircle(false,x, y)
        localStorage.setItem("turn","true");
        this.saveSceneData()
        globalNetworkWorker.postMessage({type:"shootChecked",ship:false})
    }

    checkIfAllPlaced(){
        for(const ship of this.ships){
            if(ship.fieldsOfShip.coordinates.length === 0){
                localStorage.setItem("allShipPlaced","false");
                return false
            }
        }
        localStorage.setItem("allShipPlaced","true");
        return true;
    }

    update() {

        if(JSON.parse(localStorage.getItem("checkShoot"))){
            console.log("shoot to check: ", JSON.parse(localStorage.getItem("checkShoot")).x, JSON.parse(localStorage.getItem("checkShoot")).y)
            this.checkShoot(JSON.parse(localStorage.getItem("checkShoot")).x,JSON.parse(localStorage.getItem("checkShoot")).y);
            localStorage.removeItem("checkShoot");
        }

        if(!this.allShipPlaced){
            document.title = "Place all ships"
            this.allShipPlaced = this.checkIfAllPlaced();
        }

        if(localStorage.getItem("markEnemyBorad")==="true"){
            this.enemyGrid.setCrossOrCircle(
                true,
                JSON.parse(localStorage.getItem("enemyGridToShot")).x,
                JSON.parse(localStorage.getItem("enemyGridToShot")).y
            )
            this.oponentsLifes -= 1;
            this.saveSceneData()
            localStorage.removeItem("markEnemyBorad")
        }else if(localStorage.getItem("markEnemyBorad")==="false"){
            this.enemyGrid.setCrossOrCircle(
                false,
                JSON.parse(localStorage.getItem("enemyGridToShot")).x,
                JSON.parse(localStorage.getItem("enemyGridToShot")).y
            )
            localStorage.removeItem("markEnemyBorad")
            this.saveSceneData()
        }
        
        if(localStorage.getItem("turn")==="true" && localStorage.getItem("allShipPlaced") === "true"){
            document.title = "My Turn, lifes:" + this.myLifes;
        }else if(localStorage.getItem("turn")==="false" && localStorage.getItem("allShipPlaced") === "true"){
            document.title = "Oponent Turn, lifes:" + this.myLifes;
        }

        if(this.myLifes === 0){
            this.scene.start('endingScene',{winner:false});
        }

        if(this.oponentsLifes === 0){
            this.scene.start('endingScene',{winner:true});
        }

    }

    
}