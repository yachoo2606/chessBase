import Phaser from "phaser";

export default class Grid extends Phaser.GameObjects.GameObject{

    constructor(scene,x,y,texture){
        super(scene)
        
        this.letters = ['A','B','C','D','E','F','G','H','I','J'];   
        this.grid = scene.add.group();
        this.graphics = scene.add.graphics();

        for(var i=0;i<11;i++){
            for(var j=0;j<11;j++){
                const sprite = this.grid.create(x+(i*50), y+(j*50), texture); sprite.ship=false; sprite.shooted=undefined;
                if(Math.random()>0.5){
                    sprite.ship = true;
                }
                sprite.setScale(50/sprite.width);
                sprite.setInteractive();
            }
        }
        this.grid.children.iterate((sprite)=>{
            if(Math.floor((Math.round(sprite.x-x))/50) == 0 || Math.floor((Math.round(sprite.y-y))/50)==0 ){
                if(Math.floor((Math.round(sprite.x-x))/50) == 0 && Math.floor((Math.round(sprite.y-y))/50)==0){
                    this.graphics.lineStyle(2, 0xffffff, 1);
                    this.graphics.lineBetween(sprite.x-22,sprite.y-22,sprite.x+22,sprite.y+22);
                }
                if(Math.floor((Math.round(sprite.x-x))/50) == 0 && Math.floor(((Math.round(sprite.y-y)))/50)!=0){
                    scene.add.text(sprite.x-5,sprite.y-5,Math.floor((Math.round(sprite.y-y))/50),{fontSize:25})
                }
                if(Math.floor((Math.round(sprite.x-x))/50) != 0 && Math.floor((Math.round(sprite.y-y))/50) == 0 ){
                    scene.add.text(sprite.x-5,sprite.y-5,this.letters[Math.floor((Math.round(sprite.x-x))/50)-1],{fontSize:25})
                }
            }else{
                sprite.on('pointerdown',()=>{
                    if(sprite.shooted==undefined){
                        if(sprite.ship == true){
                            scene.sound.play('shootSoundShip',{volume:0.5})
                            this.graphics.lineStyle(2, 0xff0000, 1);
                            this.graphics.lineBetween(sprite.x-22,sprite.y-22,sprite.x+22,sprite.y+22);
                            this.graphics.lineBetween(sprite.x+22,sprite.y-22,sprite.x-22,sprite.y+22);
                        }else{
                            scene.sound.play('shootSoundWater',{volume:0.5})
                            this.graphics.lineStyle(2, 0xffffff, 1);
                            this.graphics.strokeCircle(sprite.x,sprite.y,23)
                        }  
                    }
                    sprite.removeAllListeners()
                    console.log(`Clicket field:  x: ${Math.floor((Math.round(sprite.x-x))/50)} y: ${Math.floor((Math.round(sprite.y-y))/50)}`);
                });
            }
        })
    }
}