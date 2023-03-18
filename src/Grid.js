import Phaser from "phaser";

export default class Grid extends Phaser.GameObjects.GameObject{

    constructor(scene,x,y,texture){
        super(scene)
        
        this.letters = ['A','B','C','D','E','F','G','H','I','J'];   
        this.grid = scene.add.group();

        for(var i=0;i<11;i++){
            for(var j=0;j<11;j++){
                const sprite = this.grid.create(x+(i*50), y+(j*50), texture); sprite.ship=false; sprite.shooted=undefined;
                if(Math.random()>0.5){
                    console.log("statek")
                    sprite.ship = true;
                }
                sprite.setScale(50/sprite.width);
                sprite.setInteractive();
            }
        }
        this.grid.children.iterate((sprite)=>{
            if(Math.floor((Math.round(sprite.x-x))/50) == 0 || Math.floor((Math.round(sprite.y-y))/50)==0 ){
                if(Math.floor((Math.round(sprite.x-x))/50) == 0 && Math.floor((Math.round(sprite.y-y))/50)==0){
                    var graphics = scene.add.graphics();
                    graphics.lineStyle(2, 0xffffff, 1);
                    graphics.lineBetween(sprite.x-22,sprite.y-22,sprite.x+22,sprite.y+22);
                }
                if(Math.floor((Math.round(sprite.x-x))/50) == 0 && Math.floor(((Math.round(sprite.y-y)))/50)!=0){
                    var text = scene.add.text(sprite.x-5,sprite.y-5,Math.floor((Math.round(sprite.y-y))/50),{fontSize:25})
                }
                if(Math.floor((Math.round(sprite.x-x))/50) != 0 && Math.floor((Math.round(sprite.y-y))/50) == 0 ){
                    var text = scene.add.text(sprite.x-5,sprite.y-5,this.letters[Math.floor((Math.round(sprite.x-x))/50)-1],{fontSize:25})
                }
            }else{
                sprite.on('pointerdown',()=>{
                    if(sprite.shooted==undefined){
                        if(sprite.ship == true){
                            scene.sound.play('shootSoundShip',{volume:0.5})
                        }else{
                            scene.sound.play('shootSoundWater',{volume:0.5})
                        }  
                    }              
                    console.log(`Clicket sprite:  x: ${Math.floor((Math.round(sprite.x-x))/50)} y: ${Math.floor((Math.round(sprite.y-y))/50)}`);
                });
            }
        })
        // return this.toAddGrid;

    }
}


// createGrid(x,y,texture){

    
    

    
// }