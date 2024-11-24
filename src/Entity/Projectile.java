package Entity;

import Main.GamePanel;

public class Projectile extends Entity{

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set (int worldX, int worldY, String direction, boolean isAlive, Entity user){
        this.worldX = worldX + 15;
        this.worldY = worldY + 50;
        this.direction = direction;
        this.isAlive = isAlive;
        this.user = user;
        this.life = maxLife;
    }

    @Override
    public void update(){
        if (user == gp.player){
            int monsterIndex;
            monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999){
                gp.player.damageMonster(monsterIndex, this, attack, knockbackPower);
                generateParticle(user.projectile,gp.monster[gp.currentMap][monsterIndex]);
                isAlive = false;
            }
        }
        if(user != gp.player){
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if(!gp.player.isInvincible && contactPlayer){
                damagePlayer(attack);
                generateParticle(user.projectile,gp.player);
                isAlive = false;
            }
        }

        switch (direction){
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
            case "upleft": worldY -= speed; worldX -= speed; break;
            case "upright": worldY -= speed; worldX += speed; break;
            case "downleft": worldY += speed; worldX -= speed; break;
            case "downright": worldY += speed; worldX += speed; break;
            case "upleftleft": worldY -= speed; worldX -= speed/2; break;
            case "uprightright": worldY -= speed; worldX += speed/2; break;
            case "downleftleft": worldY += speed; worldX -= speed/2; break;
            case "downrightright": worldY += speed; worldX += speed/2; break;
            case "leftupleft": worldY -= speed/2; worldX -= speed; break;
            case "rightupright": worldY -= speed/2; worldX += speed; break;
            case "leftdownleft": worldY += speed/2; worldX -= speed; break;
            case "rightdownright": worldY += speed/2; worldX += speed; break;
        }

        life--;
        if (life <= 0){
            isAlive = false;
        }

        spriteCounter++;
        if (spriteCounter > 12){
            spriteCounter = 2;
        }
    }

}