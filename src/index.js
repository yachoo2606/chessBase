import Phaser from "phaser"
import MenuScene from "./MenuScene";
import LoadingScene from "./LoadingScene";
import GameScene from "./GameScene";

const config={
    type: Phaser.CANVAS,
    width: window.innerWidth,
    heihgt: window.innerHeight,
    scale:{
        mode:Phaser.Scale.ENVELOP,
        autoCenter:Phaser.Scale.CENTER_BOTH
    },
    scene:[MenuScene,LoadingScene,GameScene],
};

const game = new Phaser.Game(config);
game.canvas.addEventListener('contextmenu', (evt)=>{
    evt.preventDefault();
    evt.stopPropagation();
})