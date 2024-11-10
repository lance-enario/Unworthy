package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.Sound;
import Object.OBJ_MageAttack;
import objects.obj_Book;
import objects.obj_Key;
import objects.obj_Wand;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {

    KeyHandler keyH;
    Sound sound;
    public final int screenX;
    public final int screenY;
    public BufferedImage[] walkFrames = new BufferedImage[6];
    public BufferedImage[] idleFrames = new BufferedImage[6];
    BufferedImage[] bscAttackFrames = new BufferedImage[7];
    int hasKey =0 ;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp); // setter for gp
        this.keyH = keyH;   //setter for keyH

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // (30, 75, 45, 40)
        DialogueArea = new Rectangle(13, 40, 100, 100);
        solidArea = new Rectangle(45,95, 33, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 40;
        attackArea.height = 24;

        setDefaultValues();
        getPlayerImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 16; //spawn point
        worldY = gp.tileSize * 16;
        speed = 7; // 3 default but increased just for testing

        // PLAYER STATUS
        maxLife = 10;
        life = maxLife;
        level = 1;
        strength = 1;   // the more strength he has, the more damage he gives
        dexterity = 1;  // the more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 5;
        coins = 0;
        currentWeapon = new obj_Wand(gp);
        currentShield = new obj_Book(gp);
        attack = getAttack();   // total attack value is decided by strength and weapon.
        defense = getDefense(); // total defense value is decided by dexterity and shield.

        direction = "default";
        maintain = "right";
        isAttacking = false;
        projectile = new OBJ_MageAttack(gp);
    }

    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new obj_Key(gp));
        inventory.add(new obj_Key(gp));
    }

    public int getAttack(){
        return attack = strength * currentWeapon.attack;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defense;
    }

    public void getPlayerImage() {

        try {
            for (int i = 0; i < 6; i++) {
                walkFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/walk/" + (i + 1) + ".png")));
                idleFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/idle/Idle" + (i + 1) + ".png")));
            }
            for (int i = 0; i < 7; i++) {
                bscAttackFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/bscAttack/" + (i + 1) + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //@Override
    public void update() {

        if (isAttacking || keyH.bscAtkPressed){
            isAttacking = true;
            attacking();

            attackCounter++;
            if (attackCounter > 15){

                keyH.bscAtkPressed = false;
                isAttacking = false;
                attackCounter = 0;
            }

        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
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

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

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

            int npcDialogue = gp.cChecker.checkDialogue(this, gp.npc);
            interactNPC(npcDialogue);

            gp.keyH.enterPressed = false;
        }
            spriteCounter++;

            if (isAttacking){
                spriteNum = 5;
            }

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

        // this code snippet handles invincibility
        if (isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 60){
                isInvincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void attacking(){
        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        switch(direction){
            case "up": worldY -= attackArea.height; break;
            case "down": worldY += attackArea.height; break;
            case "left": worldX -= attackArea.width; break;
            case "right": worldX += attackArea.width; break;
        }

        solidArea.width = attackArea.width;
        //solidArea.height = attackArea.height;

        //check monster collision on hit
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        damageMonster(monsterIndex);

        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
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
                           gp.obj[objIDX] = null;
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

        public void contactMonster(int i){
            if(i!=999){
                if (!isInvincible){

                    int damage = gp.monster[i].attack - defense;
                    if(damage < 0){
                        damage = 0;
                    }

                    life -= damage;
                    isInvincible = true;
                }
            }
        }

        public void damageMonster(int i){
            if (i != 999){
                if(!gp.monster[i].isInvincible){

                    int damage = attack - gp.monster[i].defense;
                    if(damage < 0){
                        damage = 0;
                    }
                    gp.monster[i].life -= damage;
                    gp.ui.showMessage(damage + "damage!");
                    gp.monster[i].isInvincible = true;
                    gp.monster[i].damageReaction();

                    if(gp.monster[i].life <= 0){
                        gp.monster[i].isDying = true;
                        gp.ui.showMessage("Killed the " + gp.monster[i].name + "!");
                        gp.ui.showMessage("Exp + " + gp.monster[i].exp);
                        exp += gp.monster[i].exp;
                        checkLevelUp();
                    }
                }
            }
        }
    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp*2; // Up to us to decide whats the exp for nextlevelup
            maxLife += 2;
            strength++;
            dexterity++;
            attack++;
            attack = getAttack();
            defense = getDefense();

            //need sound effect;
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You leveled up to " + level + "!\n" + "You are now stronger than you are before";
        }
    }
    public void draw (Graphics2D g2) {
        BufferedImage image = idleFrames[0];

        if (isAttacking) {
            image = bscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
        } else {
            image = switch (direction) {
                case "left", "right", "up", "down" ->
                        walkFrames[(spriteNum - 1) % walkFrames.length]; // Walk animation frame
                case "default" -> idleFrames[(spriteNum - 1) % idleFrames.length];// Idle animation frame
                default -> idleFrames[0]; // Fallback to first idle frame if direction is unrecognized
            };
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

        if (shouldFlip) {
            g2.drawImage(image, (screenX + gp.playerSize+30), screenY, -gp.playerSize-30, gp.playerSize+30, null);
        } else {
            g2.drawImage(image, screenX, screenY, gp.playerSize+30, gp.playerSize+30, null);
        }

        // reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        //visible collision checker, just cross out if not needed
        g2.setColor(Color.green);
        g2.drawRect(screenX + DialogueArea.x,screenY + DialogueArea.y, DialogueArea.width, DialogueArea.height);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

}


