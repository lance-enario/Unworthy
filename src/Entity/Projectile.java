package Entity;

import Main.GamePanel;

public class Projectile extends Entity{

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set (int worldX, int worldY, String direction, boolean isAlive, Entity user){

        this.worldX = worldX + 30;
        this.worldY = worldY + 30;
        this.direction = direction;
        this.isAlive = isAlive;
        this.user = user;
        this.life = maxLife;
    }

    public void update(){

        if (user == gp.player){
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999){
                gp.player.damageMonster(monsterIndex, attack);
                isAlive = false;
            }
        }

        switch (direction){
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
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
