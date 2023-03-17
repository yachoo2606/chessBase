import Phaser from "phaser"
import MenuScene from "./MenuScene";
import GameScene from "./GameScene";

const config={
    type: Phaser.CANVAS,
    width: window.innerWidth,
    heihgt: window.innerHeight,
    scaleMode:Phaser.Scale.ScaleModes.HEIGHT_CONTROLS_WIDTH,
    scene:[MenuScene,GameScene],
};

const game = new Phaser.Game(config);
