package Main;

import Entity.Entity;
import objects.CharSelect;
import objects.assets;
import objects.obj_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaBold;
    BufferedImage heart_full,heart_blank,heart_half;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen 1: the second screen
    public BufferedImage warrior, mage, ranger, title, charselect, back, warriorbutton, magebutton, rangerbutton;
    public int slotCol = 0;
    public int slotRow = 0;

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

        assets Asset = new assets(gp);
        title = Asset.image;
        charselect = Asset.image2;
        back = Asset.image3;
        warriorbutton = Asset.image4;
        magebutton = Asset.image5;
        rangerbutton = Asset.image6;
        ranger= Asset.image7;
        warrior = Asset.image8;
        mage = Asset.image9;

        //CREATE HUD OBJECT
        Entity heart = new obj_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }
    public void showMessage (String text){
        message.add(text);
        messageCounter.add(0);
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
            drawMessage();
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
            drawInventory();
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
    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));

        for(int i = 0; i < message.size(); i++){
            if(message.get(i) != null){
                g2.setColor(Color.black);
                g2.drawString(message.get(i),messageX+2,messageY);
                g2.setColor(Color.white);
                g2.drawString(message.get(i),messageX,messageY);

                int counter = messageCounter.get(i) + 1; //message counter increment
                messageCounter.set(i, counter); // set the counter to the array;
                messageY += 50;

                if(messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    public void drawTitleScreen(){

        if(titleScreenState == 0) {
            //BACKGROUND
            g2.drawImage(title,0, 0, gp.screenWidth, gp.screenHeight, null);

            //TITLE NAME
            int y = gp.tileSize * 3;

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

            String text = "NEW GAME";
            int x = getXforCenteredText(text);
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
            g2.drawImage(charselect, 0, 0, gp.screenWidth, gp.screenHeight, null);

            //CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Warrior";
            int x =getXforCenteredText(text) - 418;
            int y = gp.tileSize*10;
            drawSubWindow(x - 41, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(warrior, 172, 220, 800, 450, null);
            if(commandNum == 0){
                g2.drawImage(warriorbutton, x-49, 520, 200, 200, null);
            }

            text = "Mage";
            x = getXforCenteredText(text);
            y = gp.tileSize * 10;
            drawSubWindow(x - 53, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(mage, 590, 220, 800, 450, null);
            if(commandNum == 1){
                g2.drawImage(magebutton, x- 64, 520, 200, 200, null);
            }

            text = "Ranger";
            x =getXforCenteredText(text) + 418;
            y = gp.tileSize * 10;
            drawSubWindow(x - 48, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(ranger, gp.tileSize * 16, 220, 800, 450, null);
            if(commandNum == 2){
                g2.drawImage(rangerbutton, x-41, 520, 200, 200, null);
            }

            text = "         ";
            x = getXforCenteredText(text);
            y += gp.tileSize*2 - 10;
            g2.drawString(text,x,y);
            if(commandNum == 3){
                g2.drawImage(back, x - 100, 600, 300 , 300, null);
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
    public void drawInventory(){
        // FRAME
        int frameX = gp.tileSize*19;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*4;
        int frameHeight = gp.tileSize*8;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+3;

        //Draw player's Inventory
        for(int i = 0; i < gp.player.inventory.size(); i++){

            //Equip Cursor
            if(gp.player.inventory.get(i)  == gp.player.currentWeapon ||
                gp.player.inventory.get(i) == gp.player.currentShield){
                g2.setColor(new Color(240,190,90));
                g2.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }
            g2.drawImage(gp.player.inventory.get(i).down1,slotX,slotY,null);
            slotX += slotSize;

            if(i == 2 || i == 5 || i == 8){
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //draw Cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,10,10);

        // Description frame
        int dframeX = frameX;
        int dframeY = frameY + frameHeight;
        int dframeWidth = frameWidth;
        int dframeHeight = gp.tileSize*3;
        // Draw description text
        int textX = dframeX + 20;
        int textY = dframeY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));
        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()){
            drawSubWindow(dframeX,dframeY,dframeWidth,dframeHeight);
            for(String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line,textX,textY);
                textY += 32;
            }
        }
    }
    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow*3);
        return itemIndex;
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
