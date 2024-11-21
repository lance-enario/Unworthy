package Entity;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SIGN extends Entity {
    String yes;

    public SIGN(GamePanel gp, String signage) {
        super(gp);
        type = type_sign;
        direction = "default";
        speed = 0;
        getImage();
        yes = signage;
        setDialogue();
    }
    public void getImage() {

        try{
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign3.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign4.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign5.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign6.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign7.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/sign/sign8.png")));

        }catch (Exception e){
            e.printStackTrace();
        }



//        up1 = setup("/objects/sign/sign1");
//        up2 = setup("/objects/sign/sign2");
//        down1 = setup("/objects/sign/sign3");
//        down2 = setup("/objects/sign/sign4");
//        left1 = setup("/objects/sign/sign5");
//        left2 = setup("/objects/sign/sign6");
//        right1 = setup("/objects/sign/sign7");
    }
    //@Override
    public void setDialogue(){
        dialogue[0] = yes;
    }

    public void speak(){
        super.speak();
    }

    @Override
    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, 70);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
    }
}
