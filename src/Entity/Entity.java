package Entity;

import java.awt.*;
import Main.GamePanel;

public class Entity{
    public int worldX, worldY;
    public String direction;
    public String maintain;
    public boolean isAttacking;

    public int audioCounter = 0;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public boolean CollisionOn = false;
    public int spriteBasicAttack;


    // ATTRIBUTES
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int attack;
    public Projectile projectile;
    public boolean alive;

    GamePanel gp;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
}


