package Entity;

import java.awt.image.BufferedImage;

public class Entity{
    public int x, y;
    public int speed;

    public BufferedImage walk0, walk1, walk2, walk3, walk4, walk5;
    public BufferedImage idle0, idle1, idle2, idle3, idle4, idle5;
    public String direction;
    public String maintain;

    public int spriteCounter = 0;
    public int spriteNum = 1;
}


