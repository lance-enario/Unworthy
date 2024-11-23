package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import objects.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Warrior extends Player{

    //graphic images for player
    BufferedImage[] walkFrames = new BufferedImage[7];
    BufferedImage[] idleFrames = new BufferedImage[4];
    BufferedImage[] bscAttackFrames = new BufferedImage[16];

    //player skill counters
    public int skill1Counter = 180;
    public int skill2Counter = 600;
    public int frameCounter = 1;
    boolean skill2Pressed = false;
    boolean skill3Pressed = false;

    //variables for selecting attack animation
    private boolean attackSwitch = false;

    public Warrior(GamePanel gp, KeyHandler keyH) {

        super(gp, keyH);          // setter for gp

        DialogueArea = new Rectangle(13, 40, 100, 100);
        solidArea = new Rectangle(25,55, 33, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 64;
        attackArea.height = 128;
        currentWeapon = new obj_Sword(gp);
        currentShield = new obj_ArmorNoob(gp);
        setDefaultValues();
        getPlayerImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 19; //spawn point    Stage 1 = 19,41     Stage2 = 14,47     Stage 3 = 49,47
        worldY = gp.tileSize * 41;
        speed = 15; // 3 default but increased just for testing

        // PLAYER STATUS
        maxLife = 20;
        life = maxLife;
        level = 1;
        strength = 1;   // the more strength he has, the more damage he gives
        dexterity = 1;  // the more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 30;
        coins = 0;
        currentWeapon = new obj_Sword(gp);
        currentShield = new obj_ArmorNoob(gp);
        attack = getAttack();   // total attack value is decided by strength and weapon.
        defense = getDefense(); // total defense value is decided by dexterity and shield.

        direction = "left";
        maintain = "right";
        isAttacking = false;
    }

    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {
        try {
            for (int i = 0; i < 4; i++){
                idleFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Warrior/idle/" + "warrior_idle_" + i + ".png")));
            }

            for (int i = 0; i < 7; i++) {
                walkFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Warrior/walk/" +  "warrior_walk_" + i + ".png")));
            }

            for (int i = 0; i < 16; i++) {
                bscAttackFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Warrior/bscAttack/" + "warrior_basic_" + i + ".png")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {

        if (keyH.bscAtkPressed && attackCounter == 30) {
            isAttacking = true;

            if (attackSwitch){
                spriteNum = 1;
                frameCounter = 1;
                attackSwitch = false;
            } else {
                spriteNum = 8;
                frameCounter = 8;
                attackSwitch = true;
            }

            attacking();
            attackCounter = 0;
        }  else if ((keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) && attackCounter == 30) {

            //SKILLS
            if (keyH.skill1Pressed && skill1Counter == 180) { //skill1Counter is the cooldown. If it is 180/3 seconds, skill is available
                skill1();
                skill1Counter = 0;
            } else if (keyH.skill2Pressed && skill2Counter == 600){
                skill2();
                skill2Counter = 0;
            } else if (keyH.skill3Pressed){
                skill3();
            }

            //MOVEMENT
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
                maintain = direction;
            } else if (keyH.rightPressed) {
                direction = "right";
                maintain = direction;
            }

            //collision checker
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //obj checker
            int objIDX = gp.cChecker.checkOBJ(this, true);
            pickUpOBJ(objIDX);

            //check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //check event
            gp.eHandler.checkEvent();

            //if collision != true, player can move
            if (!collisionOn && !keyH.enterPressed) {
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
                }
            }

            if(audioCounter < 30){
                audioCounter++;
            } else {
                gp.playSE(1);
                audioCounter = 0;
            }

            gp.keyH.enterPressed = false;

        } else if (keyH.skill1Pressed || keyH.skill2Pressed || keyH.skill3Pressed){
            if (keyH.skill1Pressed && skill1Counter == 180) {
                skill1();
                skill1Counter = 0;
            } else if (keyH.skill2Pressed && skill2Counter == 600){
                skill2();
                skill2Counter = 0;
            } else {
                skill3();
            }

        } else {

            int npcDialogue = gp.cChecker.checkDialogue(this, gp.npc[gp.currentMap]);
            interactNPC(npcDialogue);

            gp.keyH.enterPressed = false;
        }

        //HANDLES SPRITE FRAME BY FRAME CHANGES FOR PLAYER
        spriteCounter++;

        if (isAttacking) {
            if (spriteCounter > 3){
                if (spriteNum == frameCounter) {
                    spriteNum++;
                    frameCounter++;
                } else {
                    spriteNum = 1;
                    frameCounter = 1;
                    isAttacking = false;
                }
                spriteCounter = 0;

            }
        } else {
            if (spriteCounter > 6){
                if (spriteNum == frameCounter) {
                    spriteNum++;
                    frameCounter++;
                } else {
                    spriteNum = 1;
                    frameCounter = 1;
                }
                spriteCounter = 0;
            }
        }

        if(isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 60) {
                isInvincible = false;
                invincibleCounter = 0;
            }
        }

        if (attackCounter < 30) {
            attackCounter++;
        }

        if (attackCounter == 30){
            isAttacking = false;
        }
    }

    public void skill1() {
        String[] directions = {"up", "down", "left", "right", "upleft", "upright", "downleft", "downright"};
        for (int i = 0; i < directions.length; i++) {
            Projectile proj = new obj_MageSkill1(gp);
            proj.set(worldX, worldY, directions[i], true, this);
            for(int j = 0; j < gp.projectile[1].length; j++){
                if(gp.projectile[gp.currentMap][j] == null){
                    gp.projectile[gp.currentMap][j] = projectile;
                    break;
                }
            }
        }
        gp.playSE(19);
    }

    public void skill2() {
        //isInvincible = true;
        skill2Pressed = true;
    }

    public void skill3(){
        skill3Pressed = true;
    }

    public void attacking(){
        gp.playSE(21);
        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        switch(direction){
            case "up":
                worldY -= attackArea.height;
                break;
            case "down":
                worldY += attackArea.height;
                break;
            case "left":
                worldX -= attackArea.width;
                break;
            case "right":
                worldX += attackArea.width;
                break;
        }

        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        //check monster collision on hit
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        damageMonster(monsterIndex, attack, currentWeapon.knockbackPower);

        int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
        damageProjectile(projectileIndex);

        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    public void draw (Graphics2D g2) {
        BufferedImage image = idleFrames[0];

        if (isAttacking) {
            image = bscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            image = switch (direction) {
                case "left", "right", "up", "down" ->
                        walkFrames[(spriteNum - 1) % walkFrames.length]; // Walk animation frame
                default -> idleFrames[0]; // Fallback to first idle frame if direction is unrecognized
            };
        } else {
            image = idleFrames[(spriteNum - 1) % idleFrames.length];// Idle animation frame
        }

        // visual confirmation of invincible state
        if (isInvincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        }

        boolean shouldFlip = false;

        if (direction.equals("left") ||
                (direction.equals("up") && maintain.equals("left")) ||
                (direction.equals("down") && maintain.equals("left")) ||
                (direction.equals("default") && maintain.equals("left")))
        {
            shouldFlip = true;
        }

        if (image == idleFrames[(spriteNum - 1) % idleFrames.length]) {
            if (shouldFlip)
                g2.drawImage(image, (screenX + gp.playerSize - 10), screenY, -(gp.playerSize), gp.playerSize, null);
            else
                g2.drawImage(image, screenX - 10, screenY, gp.playerSize, gp.playerSize, null);
        } else if (image == walkFrames[(spriteNum - 1) % walkFrames.length]){
            if (shouldFlip)
                g2.drawImage(image, (screenX + gp.playerSize - 20), screenY, -(gp.playerSize), gp.playerSize, null);
            else
                g2.drawImage(image, screenX, screenY, gp.playerSize, gp.playerSize, null);
        } else {
            if (shouldFlip)
                g2.drawImage(image, (screenX + gp.playerSize - 10), screenY, -(gp.playerSize), gp.playerSize, null);
            else
                g2.drawImage(image, screenX - 10, screenY, gp.playerSize, gp.playerSize, null);
        }

        // reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        //visible collision checker, just cross out if not needed
//        g2.setColor(Color.green);
//        g2.drawRect(screenX + DialogueArea.x,screenY + DialogueArea.y, DialogueArea.width, DialogueArea.height);
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}


