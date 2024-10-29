package Main;
import Entity.NPC_Traveller;
import objects.obj_Chest;
import objects.obj_Door;
import objects.obj_Key;

public class AssetSetter {
    GamePanel gp ;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObj(){
        gp.obj[0] = new obj_Key();
        gp.obj[0].worldX = 13 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;

        gp.obj[3] = new obj_Chest();
        gp.obj[3].worldX = 13 * gp.tileSize;
        gp.obj[3].worldY = 10 * gp.tileSize;

        gp.obj[2] = new obj_Door();
        gp.obj[2].worldX = 13 * gp.tileSize;
        gp.obj[2].worldY = 9 * gp.tileSize;
    }

    public void setNPC(){
        gp.npc[0] = new NPC_Traveller(gp);
        gp.npc[0].worldX = 13 * gp.tileSize;
        gp.npc[0].worldY = 16 * gp.tileSize;
    }
}
