import Phaser from "phaser";

export var TextButton = new Phaser.Class({

    Extends: Phaser.GameObjects.Text,

    initialize:

    function TextButton(scene, x, y, text, style, callback) {
        Phaser.GameObjects.Text.call(this, scene, x, y, text, style);
        // Set button properties
        this.setInteractive({ useHandCursor: true })
            .on('pointerdown', () => callback())
            .on('pointerover', () => this.setStyle({ fill: '#f00' }))
            .on('pointerout', () => this.setStyle({ fill: '#fff' }));
        this.setOrigin(0.5);
    }

});
