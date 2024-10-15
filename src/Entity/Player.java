package Entity;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public Player (GamePanel gp, KeyHandler keyH){

        this.gp = gp;       // setter for gp
        this.keyH = keyH;   //setter for keyH

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 3;
        direction = "default";
    }

    public void getPlayerImage(){

        try {
            walk0 = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_0.png"));
            walk1 = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_1.png"));
            walk2 = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_2.png"));
            walk3 = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_3.png"));
            walk4 = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_4.png"));
            walk5 = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_5.png"));
            idle0 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE0.png"));
            idle1 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE1.png"));
            idle2 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE2.png"));
            idle3 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE3.png"));
            idle4 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE4.png"));
            idle5 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE5.png"));
            bscAttack0 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack0.png"));
            bscAttack1 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack1.png"));
            bscAttack2 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack2.png"));
            bscAttack3 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack3.png"));
            bscAttack4 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack4.png"));
            bscAttack5 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack5.png"));
            bscAttack6 = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack6.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        } else if (keyH.downPressed) {
            direction = "down";
            y += speed;
        } else if (keyH.leftPressed) {
            direction = "left";
            maintain = direction;
            x -= speed;
        } else if (keyH.rightPressed) {
            direction = "right";
            maintain = direction;
            x += speed;
        } else if (keyH.bscAtkPressed) {
            direction = "bscAttack";
        } else {
            direction = "default";

        }

        spriteCounter++;

        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                spriteNum = 5;
            } else if (spriteNum == 5) {
                spriteNum = 6;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        spriteBasicAttack++;

        if (spriteBasicAttack > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                spriteNum = 5;
            } else if (spriteNum == 5) {
                spriteNum = 6;
            } else {
                spriteNum = 1;
            }
            spriteBasicAttack = 1;
        }

    }

    public void draw(Graphics2D g2){

        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction){
            case "up", "down", "left", "right":
                if (spriteNum == 1){
                    image = walk0;
                }
                if (spriteNum == 2){
                    image = walk1;
                }
                if (spriteNum == 3){
                    image = walk2;
                }
                if (spriteNum == 4){
                    image = walk3;
                }
                if (spriteNum == 5){
                    image = walk4;
                }
                if (spriteNum == 6){
                    image = walk5;
                }
                break;
            case "default" : // idle animation
                if (spriteNum == 1){
                    image = idle0;
                }
                if (spriteNum == 2){
                    image = idle1;
                }
                if (spriteNum == 3){
                    image = idle2;
                }
                if (spriteNum == 4){
                    image = idle3;
                }
                if (spriteNum == 5){
                    image = idle4;
                }
                if (spriteNum == 6){
                    image = idle5;
                }
                break;
            case "bscAttack":
                if (spriteNum == 1){
                    image = bscAttack3;
                }
                if (spriteNum == 2){
                    image = bscAttack3;
                }
                if (spriteNum == 3){
                    image = bscAttack4;
                }
                if (spriteNum == 4){
                    image = bscAttack5;
                }
                if (spriteNum == 5){
                    image = bscAttack6;
                }
                if (spriteNum == 6){
                    image = bscAttack0;
                }
                break;
        }

        if (direction.equals("left") || direction.equals("up") && maintain.equals("left") || direction.equals("down") && maintain.equals("left")){ // when player faces left, the sprite continues facing left when moving up or down
            g2.drawImage(image, x + gp.tileSize, y, -gp.tileSize, gp.tileSize, null);
        } else {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }


    }
}
