package Enemies;

import Entity.Entity;
import Main.GamePanel;
import objects.obj_Coin;
import objects.obj_Potion;
import objects.obj_arrow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bandit2 extends Entity {
    GamePanel gp;


    public Bandit2(GamePanel gp){
        super(gp);

        this.gp = gp;

        defaultSpeed = 2;
        speed = defaultSpeed;
        type = type_monster; //monster type
        name = "Bandit2";
        speed = 2;
        maxLife = 5;
        life = maxLife;
        attack = 1;
        defense = 0;
        exp = 2;
        projectile = new obj_arrow(gp);

        solidArea = new Rectangle(10, 40, 40, 53);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        getAttackImage();
    }



    public void getImage() {
        up1 = setup("/Enemies/Bandit2/bandit2_walk_0");
        up2 = setup("/Enemies/Bandit2/bandit2_walk_1");
        down1 = setup("/Enemies/Bandit2/bandit2_walk_2");
        down2 = setup("/Enemies/Bandit2/bandit2_walk_3");
        left1 = setup("/Enemies/Bandit2/bandit2_walk_4");
        left2 = setup("/Enemies/Bandit2/bandit2_walk_5");
        right1 = setup("/Enemies/Bandit2/bandit2_walk_6");
        right2 = setup("/Enemies/Bandit2/bandit2_walk_7");
    }

    public void getAttackImage() {
        attackUp1 = setup("/Enemies/Bandit2/attack/bandit2_attack_0");
        attackUp2 = setup("/Enemies/Bandit2/attack/bandit2_attack_1");
        attackDown1 = setup("/Enemies/Bandit2/attack/bandit2_attack_2");
        attackDown2 = setup("/Enemies/Bandit2/attack/bandit2_attack_3");
        attackLeft1 = setup("/Enemies/Bandit2/attack/bandit2_attack_4");
        attackLeft2 = setup("/Enemies/Bandit2/attack/bandit2_attack_5");
        attackRight1 = setup("/Enemies/Bandit2/attack/bandit2_attack_6");
        attackRight2 = setup("/Enemies/Bandit2/attack/bandit2_attack_7");
    }

    @Override
    public void setAction() {
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int totalDistance = Math.max(xDistance, yDistance);

        // Define the desired range (3–4 tiles)
        int minDistance = gp.tileSize * 3;
        int maxDistance = gp.tileSize * 4;

        if (onPath) {
            // Stop chasing if too far
            checkStopChasingOrNot(gp.player, 15, 100);

            if (totalDistance < minDistance) {
                // Move away from the player
                moveAwayFromPlayer();
            } else if (totalDistance > maxDistance) {
                // Move closer to the player
                searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
            }

            checkShootOrNot(200, 30);
        } else {
            // Start chasing if within a specific range
            checkStartChasingOrNot(gp.player, 5, 100);

            // If not on a path, choose random directions and occasionally shoot
            getRandomDirection();
            checkShootOrNot(200, 30);
        }

        if (!isAttacking) {
            checkAttackOrNot(30, gp.tileSize * 7, gp.tileSize);
        }
    }


    private void moveAwayFromPlayer() {
        int xDirection = 0;
        int yDirection = 0;

        if (!collisionOn) {
            if (worldX < gp.player.worldX) xDirection = -1;
            else if (worldX > gp.player.worldX) xDirection = 1;

            if (worldY < gp.player.worldY) yDirection = -1;
            else if (worldY > gp.player.worldY) yDirection = 1;
        }

        worldX += xDirection * 1;
        worldY += yDirection * 1;

        if (xDirection == -1) direction = "left";
        if (xDirection == 1) direction = "right";
        if (yDirection == -1) direction = "up";
        if (yDirection == 1) direction = "down";
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }

    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;
        if (i < 50) dropItem(new obj_Potion(gp));
        else dropItem(new obj_Coin(gp));
    }
    @Override
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right", "upleft", "upright", "downleft", "downright":
                    if (spriteNum == 1) {
                        if (isAttacking) {
                            image = attackUp1;
                        } if(!isAttacking) {
                            image = up1;
                        }
                    }
                    if (spriteNum == 2) {
                        if (isAttacking) {
                            image = attackUp2;
                        } if(!isAttacking) {
                            image = up2;
                        }
                    }
                    if (spriteNum == 3) {
                        if (isAttacking) {
                            image = attackDown1;
                        } if(!isAttacking) {
                            image = down1;
                        }
                    }
                    if(spriteNum == 4) {
                        if (isAttacking) {
                            image = attackDown2;
                        } if(!isAttacking) {
                            image = down2;
                        }
                    }
                    if(spriteNum == 5) {
                        if (isAttacking) {
                            image = attackLeft1;
                        } if(!isAttacking) {
                            image = left1;
                        }
                    }
                    if(spriteNum == 6) {
                        if (isAttacking) {
                            image = attackLeft2;
                        } if(!isAttacking) {
                            image = left2;
                        }
                    }
                    if(spriteNum == 7) {
                        if (isAttacking) {
                            image = attackRight1;
                        } if(!isAttacking) {
                            image = right1;
                        }
                    }
                    if(spriteNum == 8) {
                        if (isAttacking) {
                            image = attackRight2;
                        } if(!isAttacking) {
                            image = right2;
                        }
                    }
                    break;
            }

            //Enemies HP BAR
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

            if (type == 11) { //projectile size
                if (shouldFlip) {
                    g2.drawImage(image, (screenX + 32), screenY, -(gp.tileSize-32), gp.tileSize-32, null);
                } else {
                    g2.drawImage(image, screenX, screenY, gp.tileSize-32, gp.tileSize-32, null);
                }
            } else if(type == 10){
                g2.drawImage(image, screenX, screenY - 22, gp.tileSize, gp.tileSize + 20, null);
            } else {
                if (shouldFlip) {
                    g2.drawImage(image, screenX  + gp.tileSize + 5, screenY, -gp.tileSize - 40, gp.tileSize+40, null);
                } else {
                    g2.drawImage(image, screenX - 29,  screenY, gp.tileSize+40, gp.tileSize+40, null);
                }
            }

            changeAlpha(g2,1.0f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }



}
