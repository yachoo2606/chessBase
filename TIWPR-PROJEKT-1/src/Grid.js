import Phaser from "phaser";
import globalNetworkWorker from "./globalNetworkWorker";

export default class Grid extends Phaser.GameObjects.GameObject{

    constructor(scene,x,y,texture, listeners){
        super(scene)
        this.scene = scene;
        let scale = 55
        this.letters = ['A','B','C','D','E','F','G','H','I','J'];   
        this.grid = scene.add.group();
        this.graphics = scene.add.graphics();
        this.graphics.setDepth(2);
        this.geoms=[]
        for(var i=0;i<11;i++){
            for(var j=0;j<11;j++){
                const sprite = this.grid.create(x+(i*scale), y+(j*scale), texture); 
                sprite.coordinates = {x: i, y: j};
                sprite.setDepth(0)
                sprite.shooted=undefined;
                sprite.canBePlaced = true;
                sprite.setScale(scale/sprite.width);
                sprite.setInteractive();
            }
        }
        this.grid.children.iterate((sprite)=>{
            if(Math.floor((Math.round(sprite.x-x))/scale) == 0 || Math.floor((Math.round(sprite.y-y))/scale)==0 ){
                if(Math.floor((Math.round(sprite.x-x))/scale) == 0 && Math.floor((Math.round(sprite.y-y))/scale)==0){
                    this.graphics.lineStyle(2, 0xffffff, 1);
                    this.graphics.lineBetween(sprite.x-22,sprite.y-22,sprite.x+22,sprite.y+22);
                }
                if(Math.floor((Math.round(sprite.x-x))/scale) == 0 && Math.floor(((Math.round(sprite.y-y)))/scale)!=0){
                    scene.add.text(sprite.x-7,sprite.y-5,Math.floor((Math.round(sprite.y-y))/scale),{fontSize:25})
                }
                if(Math.floor((Math.round(sprite.x-x))/scale) != 0 && Math.floor((Math.round(sprite.y-y))/scale) == 0 ){
                    scene.add.text(sprite.x-5,sprite.y-5,this.letters[Math.floor((Math.round(sprite.x-x))/scale)-1],{fontSize:25})
                }
            }else{
                sprite.input.dropZone = true;
                if(listeners){
                    sprite.on('pointerdown',()=>{
                        console.log(localStorage.getItem("turn"));
                        if(localStorage.getItem("turn")==="true" && localStorage.getItem("allShipPlaced")==="true"){
                            localStorage.setItem("turn","false");
                            sprite.removeAllListeners();
                            console.log(`Clicket field:  x: ${Math.floor((Math.round(sprite.x-x))/scale)} y: ${Math.floor((Math.round(sprite.y-y))/scale)}`);
                            localStorage.setItem("enemyGridToShot",JSON.stringify({x:Math.round(sprite.x-x)/scale, y:Math.floor((Math.round(sprite.y-y))/scale)}))
                            globalNetworkWorker.postMessage({type:'shoot', coords: {x: Math.round(sprite.x-x)/scale, y: Math.floor((Math.round(sprite.y-y))/scale) }})
                        }
                    });
                }
            }
        })
    }

    setLine(x,y){
        this.graphics.lineStyle(2, 0xff0000, 1);
        this.graphics.lineBetween(x-22,y-22,x+22,y+22);
        this.graphics.lineBetween(x+22,y-22,x-22,y+22);
    }

    setCircle(x,y,r=23){
        this.graphics.lineStyle(2, 0xffffff, 1);
        this.graphics.strokeCircle(x,y,r)
    }

    setCrossOrCircle(mode, x, y){
        this.grid.children.iterate((sprite)=>{
            if(sprite.coordinates.x === x && sprite.coordinates.y ===y){
                console.log("shooted field",sprite);
                if(mode){
                    this.scene.sound.play('shootSoundShip',{volume:0.5})
                    this.graphics.lineStyle(2, 0xff0000, 1);
                    this.graphics.lineBetween(sprite.x-22,sprite.y-22,sprite.x+22,sprite.y+22);
                    this.graphics.lineBetween(sprite.x+22,sprite.y-22,sprite.x-22,sprite.y+22);
                    this.geoms.push({
                        type:"line",
                        x:sprite.x,
                        y:sprite.y,
                        r:undefined
                    })
                }else{
                    this.scene.sound.play('shootSoundWater',{volume:0.5})
                    this.graphics.lineStyle(2, 0xffffff, 1);
                    this.graphics.strokeCircle(sprite.x,sprite.y,23)
                    this.geoms.push({
                        type:"circle",
                        x:sprite.x,
                        y:sprite.y,
                        r:23
                    })
                }
            }
        })
        
    }

}