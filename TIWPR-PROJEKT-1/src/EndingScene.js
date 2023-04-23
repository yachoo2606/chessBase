import Phaser from "phaser";

export default class EndingScene extends Phaser.Scene {
    constructor() {
        super({ key: 'endingScene' });
    }

    preload() {

    }

    create() {

        localStorage.clear();
        console.log("Type: ",this.scene.settings.data.winner);
        if(this.scene.settings.data.winner){
            this.cameras.main.setBackgroundColor("#FFD700");

            const text = this.add.text(this.cameras.main.centerX, this.cameras.main.centerY, 'YOU WIN!!!!', { fontFamily: 'Arial', fontSize: 48, color: '#FF0000' });
            text.setOrigin(0.5);
            text.setPosition(this.cameras.main.centerX, this.cameras.main.centerY);


        }else{
            this.cameras.main.setBackgroundColor("#808080");

            const text = this.add.text(this.cameras.main.centerX, this.cameras.main.centerY, 'YOU LOST!!!!', { fontFamily: 'Arial', fontSize: 48, color: '#FF0000' });
            text.setOrigin(0.5);
            text.setPosition(this.cameras.main.centerX, this.cameras.main.centerY);

        }

        this.exitButton = new TextButton(this,window.innerWidth/2,window.innerHeight,"Exit",{ fontSize: '64px', fill: '#fff' },()=>{
            this.scene.start('menuScene')
        })
        this.add.existing(this.exitButton);
        

    }
    
    update() {



    }

    
}