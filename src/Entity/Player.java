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
    BufferedImage[] walkFrames = new BufferedImage[6];
    BufferedImage[] idleFrames = new BufferedImage[6];
    BufferedImage[] bscAttackFrames = new BufferedImage[7];

    public Player (GamePanel gp, KeyHandler keyH){

        this.gp = gp;       // setter for gp
        this.keyH = keyH;   //setter for keyH

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 100;
        y = 500;
        speed = 3;
        direction = "default";
        maintain = "right";
        isAttacking = false;
    }

    public void getPlayerImage(){

        try {
            for (int i = 0; i < 6; i++) {
                walkFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/walk/sprite_" + i + ".png"));
                idleFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/idle/sprite_IDLE" + i + ".png"));
            }
            for (int i = 0; i < 7; i++) {
                bscAttackFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack" + i + ".png"));
            }
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
            isAttacking = true;
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


    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        if (isAttacking) {
            isAttacking = false; // Reset attack state (could be handled differently based on animation needs)
            image = bscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
        } else {
            switch (direction) {
                case "left", "right", "up", "down":
                    image = walkFrames[(spriteNum - 1) % walkFrames.length]; // Walk animation frame
                    break;
                case "default":
                    image = idleFrames[(spriteNum - 1) % idleFrames.length]; // Idle animation frame
                    break;
                default:
                    image = idleFrames[0]; // Fallback to first idle frame if direction is unrecognized
                    break;
            }
        }

        boolean shouldFlip =
                direction.equals("left") ||
                (direction.equals("up") && maintain.equals("left")) ||
                (direction.equals("down") && maintain.equals("left")) ||
                (direction.equals("default") && maintain.equals("left"));

        if (shouldFlip) {
            g2.drawImage(image, (x + gp.tileSize), y, -gp.tileSize, gp.tileSize, null);
        } else {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }

    }
}

