package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;

public class Entity {
    public int worldX, worldY;
    public String direction = "up";
    public String maintain = "right";
    public boolean isAlive = true;
    public boolean isDying = false;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage[] idleFrames;
    public int audioCounter = 0;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    public boolean hpBarOn = false;

    //placeholder area lines for collision & dialogue check
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public Rectangle solidArea = new Rectangle(32,56, 64, 64);
    public Rectangle DialogueArea = new Rectangle(32,56, 64, 64);

    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean CollisionOn = false;

    //entity invincibility
    public boolean isInvincible = false;
    //entity attack
    public boolean isAttacking = false;

    //COUNTER
    public int invincibleCounter = 0;
    public int actionLockCounter = 0;
    public int hpBarCounter = 0;
    public int attackCounter = 0;
    public int shotAvailableCounter = 0;
    public int mageSkill1Counter = 0;
    int dyingCounter = 0;


    String[] dialogue = new String[20];
    int dialogueIndex = 0;
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;

    // CHARACTER ATTRIBUTES
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int attack;
    public Projectile projectile;
    public int strength;
    public int dexterity;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coins;
    public Entity currentWeapon;
    public Entity currentShield;

    // ITEM ATTRIBUTES
    public int attackValue;
    public int defenseValue;
    public String description = "";

    //TYPE
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_shield = 4;
    public final int type_wand = 5;
    public final int type_bow = 6;
    public final int type_armor = 7;
    public final int type_consumable = 8;
    public final int type_projectile = 9;

    GamePanel gp;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction(){}
    public void damageReaction(){}
    public void speak(){
        if(dialogue[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction){
            case "up":  direction = "down"; break;
            case "left": direction = "right"; break;
            case "down": direction = "up"; break;
            case "right": direction = "left"; break;
        }
    }
    public void use(Entity entity){}
    public void update() {
        setAction();
        CollisionOn = false;

        gp.cChecker.checkTile(this);
        gp.cChecker.checkOBJ(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == type_monster && contactPlayer){
            if (!gp.player.isInvincible){

                int damage  = attack - gp.player.defense;
                if(damage < 0){
                    damage = 0;
                }

                gp.player.life -= damage;
                gp.player.isInvincible = true;
            }
        }

        if (!CollisionOn) {
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

        spriteCounter++;

        if (spriteCounter > 8) {
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

        if (isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 30){
                isInvincible = false;
                invincibleCounter = 0;
            }
        }

    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right", "upright", "upleft", "downright", "downleft":
                   if(spriteNum == 1) image = up1;
                   if(spriteNum == 2) image = up2;
                   if(spriteNum == 3) image = down1;
                   if(spriteNum == 4) image = down2;
                   if(spriteNum == 5) image = left1;
                   if(spriteNum == 6) image = left2;
                   if(spriteNum == 7) image = right1;
                   if(spriteNum == 8) image = right2;
                   break;
            }

            //MONSTER HP BAR
            if(type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1,screenY-16,gp.tileSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if(hpBarCounter > 300) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if(isInvincible){
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2,0.4f);
            }

            if(isDying){
                dyingAnimation(g2);
            }

            boolean shouldFlip = direction.equals("left") ||
                    (direction.equals("up") && maintain.equals("left")) ||
                    (direction.equals("down") && maintain.equals("left"));

            switch(type){
                case 9:     //regular mage projectile
                    if (shouldFlip) {
                        g2.drawImage(image, (screenX + 32), screenY, -(gp.tileSize-32), gp.tileSize-32, null);
                    } else {
                        g2.drawImage(image, screenX, screenY, gp.tileSize-32, gp.tileSize-32, null);
                    }
                    break;
                case 10:    //skill1 mage Projectile
                    if (shouldFlip) {
                        g2.drawImage(image, (screenX + 64), screenY, -(gp.tileSize), gp.tileSize, null);
                    } else {
                        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                    break;
                default:
                    if (shouldFlip) {
                        g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize, gp.tileSize, null);
                    } else {
                        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                    break;
            }

//            if (type == 9) { //projectile size
//                if (shouldFlip) {
//                    g2.drawImage(image, (screenX + 32), screenY, -(gp.tileSize-32), gp.tileSize-32, null);
//                } else {
//                    g2.drawImage(image, screenX, screenY, gp.tileSize-32, gp.tileSize-32, null);
//                }
//            } else {
//                if (shouldFlip) {
//                    g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize, gp.tileSize, null);
//                } else {
//                    g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
//                }
//            }

            changeAlpha(g2,1.0f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;
        int i = 5;
             if(dyingCounter <= i){changeAlpha(g2,0f);}
             if(dyingCounter > i && dyingCounter <= i*2){changeAlpha(g2,1f);}
             if(dyingCounter > i*2 && dyingCounter <= i*3){changeAlpha(g2,0f);}
             if(dyingCounter > i*3 && dyingCounter <= i*4){changeAlpha(g2,1f);}
             if(dyingCounter > i*4 && dyingCounter <= i*5){changeAlpha(g2,0f);}
             if(dyingCounter > i*5 && dyingCounter <= i*6){changeAlpha(g2,1f);}
             if(dyingCounter > i*6 && dyingCounter <= i*7){changeAlpha(g2,0f);}
             if(dyingCounter > i*7 && dyingCounter <= i*8){changeAlpha(g2,1f);}
            if(dyingCounter > i*8){
            isDying = false;
            isAlive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
    }

}


