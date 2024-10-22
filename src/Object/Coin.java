package Object;

import Entity.Entity;
import Main.GamePanel;

public class Coin extends Entity {

    GamePanel gp;

    public Coin(GamePanel gp){
        super(gp);
        this.gp = gp;


    }
}
