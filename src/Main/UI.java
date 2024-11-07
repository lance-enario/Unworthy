package Main;

import Entity.Entity;
import objects.CharSelect;
import objects.obj_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaBold;
    BufferedImage heart_full,heart_blank,heart_half;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen 1: the second screen
    public BufferedImage warrior, mage, ranger;

    public UI(GamePanel gp){

        this.gp = gp;
        try{
            InputStream is = getClass().getResourceAsStream("/Font/x12y16pxMaruMonica.ttf");
            assert is != null;
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/Font/Purisa Bold.ttf");
            assert is != null;
            purisaBold = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch(FontFormatException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        Entity CharSelect = new CharSelect(gp);
        ranger = CharSelect.image;
        warrior = CharSelect.image2;
        mage = CharSelect.image3;

        //CREATE HUD OBJECT
        Entity heart = new obj_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage (String text){
        message = text;
        messageOn = true;
    }

    public void draw (Graphics2D g2){
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(Color.white);

        //title state
        if(gp.gameState == gp.titleState){
            drawTitleScreen();

        }
        //play state
        if (gp.gameState == gp.playState){
            drawPlayerLife();
        }
        //pause state
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            Color c = new Color(32,32,32, 127);
            g2.setColor(c);
            g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
            g2.setColor(Color.white);
            drawPauseScreen();
        }
        //dialogue
        if(gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
        }

    }
    public void drawPlayerLife(){
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // DRAW MAX LIFE
        while(i < gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x += gp.tileSize;
        }
        //RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //DRAW CURRENT LIFE
        while(i < gp.player.life){
            g2.drawImage(heart_half,x,y,null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }
    public void drawTitleScreen(){
        //characterSelectImage();
//        BufferedImage image1 = ranger[2];
        if(titleScreenState == 0) {
            //BACKGROUND
            g2.setColor(new Color(70, 120, 80));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Unworthy"; // Unworthy
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;

            //SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text, x + 5, y + 5);

            //MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //CHARACTER IMAGE
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 2;
            g2.drawImage(gp.player.idleFrames[3], x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize * 4;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                //if drawing we can use the drawImage function
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                //if drawing we can use the drawImage function
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "EXIT GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                //if drawing we can use the drawImage function
                g2.drawString(">", x - gp.tileSize, y);
            }
        }
        else if(titleScreenState == 1){
            //BACKGROUND
            g2.setColor(new Color(90, 90, 80));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select your class!";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*2;
            drawSubWindow(605, 83, gp.tileSize * 5, gp.tileSize);
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            text = "Warrior";
            x =getXforCenteredText(text) - 418;
            y = gp.tileSize*10;
            drawSubWindow(x - 41, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(warrior, gp.tileSize * 3, gp.tileSize *4 + 16, gp.tileSize * 5, gp.tileSize * 5, null);
            if(commandNum == 0){
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "Mage";
            x = getXforCenteredText(text);
            y = gp.tileSize * 10;
            drawSubWindow(x - 53, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(mage, gp.tileSize * 7 + 12, gp.tileSize - 60, gp.tileSize * 9, gp.tileSize * 9, null);
            if(commandNum == 1){
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "Ranger";
            x =getXforCenteredText(text) + 418;
            y = gp.tileSize * 10;
            drawSubWindow(x - 48, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(ranger, gp.tileSize * 16, gp.tileSize * 4, gp.tileSize * 5, gp.tileSize * 5, null);
            if(commandNum == 2){
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "Back";
            x = getXforCenteredText(text);
            y += gp.tileSize*2;
            g2.drawString(text,x,y);
            if(commandNum == 3){
                g2.drawString(">",x-gp.tileSize,y);
            }
        }
    }
    public void drawDialogueScreen(){
            int x = gp.tileSize*2  , y = gp.tileSize, width = gp.screenWidth - (gp.tileSize*4), height = gp.tileSize*4;
            drawSubWindow(x, y, width, height);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
            x += gp.tileSize;
            y+= gp.tileSize;

            for(String line :currentDialogue.split("\n")){
                g2.drawString(currentDialogue, x, y);
                y += 40;
            }

    }

    public void drawCharacterScreen(){
        //CREATE A FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth,frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        //NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 20;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) - 30;

        //RESET textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coins);
        textX = getXforAlignnToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 15, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 15, null);
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 25, 25);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke( new BasicStroke(5) );
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }


    public void drawPauseScreen(){

        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }


    public int getXforCenteredText(String text){
        int length  = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
    public int getXforAlignnToRightText(String text, int tailX){
        int length  = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = tailX - length;
        return x;
    }

}
