
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
        getAttackImage();
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
    public void getAttackImage(){
        attackUp2 = setup("/monster/slime/walk/attack/slime_attack_0");
        attackUp1 = setup("/monster/slime/walk/attack/slime_attack_1");
        attackDown1 = setup("/monster/slime/walk/attack/slime_attack_2");
        attackDown2 = setup("/monster/slime/walk/attack/slime_attack_3");
        attackLeft1 = setup("/monster/slime/walk/attack/slime_attack_4");
        attackLeft2 = setup("/monster/slime/walk/attack/slime_attack_5");
        attackRight1 = setup("/monster/slime/walk/attack/slime_attack_6");
        attackRight2 = setup("/monster/slime/walk/attack/slime_attack_7");
    }

    @Override
    public void setAction() {

        if(onPath) {
            //check if it stops chasing
            checkStopChasingOrNot(gp.player,15,100);

            //search direction to go
            searchPath(getGoalCol(gp.player),getGoalRow(gp.player));

            //check if it shoots projectile
            checkShootOrNot(200,30);
        } else {
            //check if it starts chasing
            checkStartChasingOrNot(gp.player,5,100);

            //get a random direction
            getRandomDirection();

        }
        if(!isAttacking){
            checkAttackOrNot(30,gp.tileSize*4,gp.tileSize);
        }
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