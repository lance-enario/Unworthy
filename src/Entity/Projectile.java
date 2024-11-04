package Entity;

import Main.GamePanel;

public class Projectile extends Entity{

    Entity user;

    public Projectile(GamePanel gp) {super(gp);}

    public void set (int worldX, int worldY, String direction, boolean isAlive, Entity user){

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.isAlive = isAlive;
        this.user = user;
        this.life = maxLife;
    }

//    public void update(){
//
//        switch (direction){
//            case "up": worldY -= speed; break;
//            case "down": worldY -= speed; break;
//            case "left": worldY -= speed; break;
//            case "right": worldY -= speed; break;
//        }
//
//        life--;
//        if (life <= 0){
//            alive = false;
//        }
//
//        spriteCounter++;
//        if (spriteCounter > 12){
//
//            spriteCounter = 2;
//        }
//    }

}
