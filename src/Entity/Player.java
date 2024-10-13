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
        speed = 2;
        direction = "down";
    }

    public void getPlayerImage(){

        try {
            walk0 = ImageIO.read(getClass().getResourceAsStream("/player/sprite_WALK0.png"));
            walk1 = ImageIO.read(getClass().getResourceAsStream("/player/sprite_WALK1.png"));
            walk2 = ImageIO.read(getClass().getResourceAsStream("/player/sprite_WALK2.png"));
            walk3 = ImageIO.read(getClass().getResourceAsStream("/player/sprite_WALK3.png"));
            walk4 = ImageIO.read(getClass().getResourceAsStream("/player/sprite_WALK4.png"));
            walk5 = ImageIO.read(getClass().getResourceAsStream("/player/sprite_WALK5.png"));
            idle0 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE0.png"));
            idle1 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE1.png"));
            idle2 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE2.png"));
            idle3 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE3.png"));
            idle4 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE4.png"));
            idle5 = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){

        if (keyH.upPressed == true){
            direction = "up";
            y -= speed;
        } else if (keyH.downPressed == true){
            direction = "down";
            y += speed;
        } else if (keyH.leftPressed == true){
            direction = "left";
            maintain = direction;
            x -= speed;
        } else if (keyH.rightPressed == true) {
            direction = "right";
            maintain = direction;
            x += speed;
        } else {
            direction = "default";
            maintain = direction;;
        }

        spriteCounter++;

        if (spriteCounter > 10){
            if(spriteNum == 1) {
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

    }

    public void draw(Graphics2D g2){

        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction){
            case "up":
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
            case "down":
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
            case "left":
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
            case "right":
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
            case "default" :
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
        }

        if (direction == "left" || direction == "up" && maintain == "left" || direction == "down" && maintain == "left"){ // when player faces left, the sprite continues facing left when moving up or down
            g2.drawImage(image, x + gp.tileSize, y, -gp.tileSize, gp.tileSize, null);
        } else {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }


    }
}
