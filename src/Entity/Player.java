package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.Sound;
import Object.OBJ_MageAttack;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler keyH;
    Sound sound;
    public final int screenX;
    public final int screenY;
    public BufferedImage[] walkFrames = new BufferedImage[6];
    public BufferedImage[] idleFrames = new BufferedImage[6];
    BufferedImage[] bscAttackFrames = new BufferedImage[7];
    int hasKey =0 ;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp); // setter for gp
        this.keyH = keyH;   //setter for keyH

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(37,79, 33, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 3; //spawn point
        worldY = gp.tileSize * 16;
        speed = 7; // 3 default but increased just for testing

        // PLAYER STATUS
        maxLife = 10;
        life = maxLife;

        direction = "default";
        maintain = "right";
        isAttacking = false;
        projectile = new OBJ_MageAttack(gp);
    }

    public void getPlayerImage() {

        try {
            for (int i = 0; i < 6; i++) {
                walkFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/walk/" + (i+1) + ".png"));
                idleFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/idle/Idle" + (i+1) + ".png"));
            }
            for (int i = 0; i < 7; i++) {
                bscAttackFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/bscAttack/sprite_bscAttack" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {
        if (keyH.enterPressed || keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
                System.out.println("up");
            } else if (keyH.downPressed) {
                direction = "down";
                System.out.println("down");
            } else if (keyH.leftPressed) {
                direction = "left";
                maintain = direction;
                System.out.println("left");
            } else if (keyH.rightPressed) {
                direction = "right";
                maintain = direction;
                System.out.println("right");
            } else if (keyH.enterPressed){
                direction = "default";
                System.out.println("idle dialogue trigger");
            }

            //collision checker
            CollisionOn = false;
            gp.cChecker.checkTile(this);

            //obj checker
            int objIDX = gp.cChecker.checkOBJ(this, true);
            pickUpOBJ(objIDX);

            //check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //check event
            gp.eHandler.checkEvent();

            //if collision != true, player can move
            if (!CollisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                    case "default":
                        break;
                }
            }

            gp.keyH.enterPressed = false;

        } else {
            direction = "default";
            gp.cChecker.checkEntity(this, gp.npc);
        }
            spriteCounter++;

            if (spriteCounter > 6) {
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

        public void pickUpOBJ(int objIDX) {
            if(objIDX != 999){
               String objName = gp.obj[objIDX].name;

               switch (objName){
                   case "Key":
                       hasKey++;
                       gp.obj[objIDX] = null;
                       break;
                   case "Door":
                       if(hasKey > 0){
                           gp.obj[objIDX] = null;
                           hasKey--;
                       }
                       break;
                   case "Chest":
                       break;
               }
            }
        }

        public void interactNPC(int i) {
             if(i!=999){
                 if(gp.keyH.enterPressed){
                 gp.gameState = gp.dialogueState;
                 gp.npc[i].speak();
               }
             }
            gp.keyH.enterPressed = false;
        }

        public void draw (Graphics2D g2) {

            BufferedImage image;

            if (isAttacking) {
                isAttacking = false; // Reset attack state (could be handled differently based on animation needs)
                keyH.downPressed = false;
                keyH.leftPressed = false;
                keyH.upPressed = false;
                keyH.rightPressed = false;
                image = bscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
            } else {
                switch (direction) {
                    case "left", "right", "up", "down": image = walkFrames[(spriteNum - 1) % walkFrames.length];  break; // Walk animation frame
                    case "default": image = idleFrames[(spriteNum - 1) % idleFrames.length]; break;// Idle animation frame
                    default: image = idleFrames[0]; break; // Fallback to first idle frame if direction is unrecognized
                }
            }
            boolean shouldFlip =
                    direction.equals("left") ||
                    (direction.equals("up") && maintain.equals("left")) ||
                    (direction.equals("down") && maintain.equals("left")) ||
                    (direction.equals("default") && maintain.equals("left"));

            if (shouldFlip) {
                g2.drawImage(image, (screenX + gp.playerSize+15), screenY, -gp.playerSize-15, gp.playerSize+15, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.playerSize+15, gp.playerSize+15, null);
            }

            //visible collision checker, just cross out if not needed
            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

}


