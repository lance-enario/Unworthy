
package Enemies;

import Entity.Entity;
import Main.GamePanel;
import objects.obj_Coin;
import objects.obj_Potion;
import objects.obj_Projectile;

import java.awt.*;
import java.util.Random;

public class Slime extends Entity {
    GamePanel gp;


    public Slime(GamePanel gp){
        super(gp);

        this.gp = gp;

        type = type_monster; //monster type
        name = "Green Slime";
        defaultSpeed = 2;
        speed = defaultSpeed;
        maxLife = 5;
        life = maxLife;
        attack = 1;
        defense = 0;
        exp = 2;
        projectile = new obj_Projectile(gp);

        solidArea = new Rectangle(4, 15, 50, 47);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage(){
        up1 = setup("/monster/slime/walk/slime_walk_0");
        up2 = setup("/monster/slime/walk/slime_walk_1");
        down1 = setup("/monster/slime/walk/slime_walk_2");
        down2 = setup("/monster/slime/walk/slime_walk_3");
        left1 = setup("/monster/slime/walk/slime_walk_4");
        left2 = setup("/monster/slime/walk/slime_walk_5");
        right1 = setup("/monster/slime/walk/slime_walk_6");
        right2 = setup("/monster/slime/walk/slime_walk_7");
    }

    @Override
    public void setAction() {
        if(onPath){
//            int goalCol = 78;
//            int goalRow = 22;

            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
            searchPath(goalCol,goalRow);

            int i = new Random().nextInt(100)+1;
            if(i > 99 && !projectile.isAlive && shotAvailableCounter == 30){
                projectile.set(worldX, worldY, direction, true, this);
                //gp.projectileList.add(projectile);

                for(int ii = 0; ii < gp.projectile[1].length; ii++){
                    if(gp.projectile[gp.currentMap][ii] == null){
                        gp.projectile[gp.currentMap][ii] = projectile;
                        break;
                    }
                }
                shotAvailableCounter = 0;
            }
        } else {
            actionLockCounter++;
            if(actionLockCounter == 40){
                Random rand = new Random();
                int i = rand.nextInt(100) + 1;

                if(i<=25) {
                    direction = "up"; //up
                }
                if(i > 25 && i <=50){
                    direction = "down"; //down
                }
                if(i > 50 && i <= 75){
                    direction = "left"; // left
                }
                if(i > 75){
                    direction = "right"; // right
                }
                actionLockCounter = 0;
            }
        }

    }

    @Override
    public void update() {
        super.update();

        int xDistance = Math.abs(worldX-gp.player.worldX);
        int yDistance = Math.abs(worldY-gp.player.worldY);
        int tileDistance = (xDistance+yDistance)/gp.tileSize;

        if(!onPath && tileDistance < 5){
            int i = new Random().nextInt(100)+1;
            if(i > 50) onPath = true;
        }

        if(onPath && tileDistance > 20) onPath = false;
        setAction();
        collisionOn = false;
    }

    public void damageReaction(){
        actionLockCounter = 0;
        //direction = gp.player.direction;
        onPath = true;
    }

    public void checkDrop(){
        int i = new Random().nextInt(100)+1;
        if(i < 50){
            dropItem(new obj_Potion(gp));
        }
        if(i > 50){
            dropItem(new obj_Coin(gp));
        }
    }
}