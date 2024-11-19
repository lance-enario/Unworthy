package Main;

import Entity.Entity;
import objects.assets;
import objects.obj_Heart;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
    int counter = 0;
    int subState = 0;
    // for cutscenes
    public BufferedImage[] cutscenes = new BufferedImage[14];
    public URL[] narrations = new URL[12];
    boolean[] soundPlayed = new boolean[12];
    boolean bg = false;
    public int cutsceneNum = 0;
    String[] caption = new String[12];
    Clip curr, titleBg;
    float opacity = 0.0f;
    public boolean fadingOut = false;
    public boolean transitioning = false; // True during a transition
    private long lastUpdateTime = System.currentTimeMillis(); // Timer to control fade speed


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

        //For Cutscenes
        System.arraycopy(Asset.cue, 0, cutscenes, 0, cutscenes.length);
        Sound narrate = new Sound();
        System.arraycopy(narrate.soundURL, 0, narrations, 0, narrations.length);
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
        // OPTION STATE
        if(gp.gameState == gp.optionState){
            drawOptionScreen();
        }

        // TRANSITION STATE
        if(gp.gameState == gp.transitionState){
            drawTransition();
            drawDialogueScreen();
        }
        // GAME OVER STATE
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
    }

    public void rollCutScene() {
        if (cutsceneNum == 12) {
            stopCurr();
            titleScreenState = 2;
            return;
        }
        bg = false;
        if (!bg) {
            if (titleBg == null) {
                titleBg = gp.playCutScenes(17); // Play background sound
            }
            bg = true;
        }
//
//            if (cutsceneNum == 0 || cutsceneNum >= 4){
//                handleFadeEffect();
//            }

        g2.drawImage(cutscenes[cutsceneNum], 0, 0, gp.screenWidth, gp.screenHeight, null);
        //Draw the cutscene image with applied opacity
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        if (!soundPlayed[cutsceneNum]) {
            fadingOut = true;
            playNarrationClip(cutsceneNum + 3); // Play narration for the current cutscene
            soundPlayed[cutsceneNum] = true;   // Mark as played to prevent repeats
        }
        setCaptions();
        drawCaptions(cutsceneNum);


    }

    public void playNarrationClip(int clipNumber) {
        stopCurr(); // Stop any existing narration

        curr = gp.playCutScenes(clipNumber);
        if (curr != null) {

            curr.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        curr.close(); // Release resources
                        curr.removeLineListener(this); // Remove the listener
                        //cutsceneNum++;
                        if(!fadingOut){
                            cutsceneNum++;
                        }
                    }
                }
            });
        }

    }

    // Method to stop the current narration safely
    public void stopCurr() {
        if (curr != null) {
            if (curr.isRunning()) {
                curr.stop();
            }
            curr.close();
            curr = null; // Avoid stale references
        }
    }

    public  void drawCaptions(int i){
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        int y = 105, x = 125;
        if(cutsceneNum == 0){
            drawSubWindow(100, 50, 1350, 200);
            for(String line :caption[0].split("\n")){
                g2.setColor(Color.darkGray);
                g2.drawString(line, x, y);
                g2.setColor(Color.white);
                g2.drawString(line, x, y+3);
                y+=50;
            }
        }else if (i >= 1 && i <= 3 || i == 6){
            g2.setColor(Color.darkGray);
            g2.drawString(caption[i], getXforCenteredText(caption[i]), (gp.screenWidth / 2) - 3);
            g2.setColor(Color.WHITE);
            g2.drawString(caption[i], getXforCenteredText(caption[i]), gp.screenWidth / 2);
        }
         else {
             if(caption[i] != null) {
                 for(String line :caption[i].split("\n")){
                     g2.setColor(Color.darkGray);
                     g2.drawString(line, x, y);
                     g2.setColor(Color.white);
                     g2.drawString(line, x, y+3);
                     y+=50;
                 }
             }

         }
    }

    public void setCaptions(){
        caption[0] = """
                In the heart of a lush, fertile valley, the Kingdom of Eldoria thrived under the benevolent rule of King\s
                Alaric and Queen Seraphina. The royal family was adored by the people, who often spoke of their kindness
                 and wisdom. However,beneath this idyllic surface lay the complexities of royal lineage and ambition.""";
        caption[1] = "King Alaric had two sons: ";
        caption[2] = "Prince Sirius, the pureblood heir, son of Queen Seraphina, ";
        caption[3] = "and Prince Lucian, the concubine prince, born just a day later after Prince Sirius's birth.";
        caption[4] = "Sirius embodied the traditional virtues of a prince—stoic, disciplined, and dutiful—but he struggled to \n" +
                     "command the same affection from the people that Lucian effortlessly garnered.";
        caption[5] = """
                Lucian became a favorite among the subjects as he had a charismatic personality and was gifted in both\s
                diplomacy and combat. His charm and talent made him beloved, while Sirius often felt like a shadow in the\s
                brilliance of his half-brother. \s""";
        caption[6] = "This growing jealousy Sirius had fueled a rift between the brothers, leading to tension within the palace walls. ";
        caption[7] = """
                King Alaric, who loved both sons dearly but understood the importance of lineage, often found himself caught\s
                in the middle. He hoped that in time, Sirius would grow into his role as king without feeling threatened by
                 \
                Lucian’s popularity. \s""";
        caption[8] = """
                All was well in Eldoria until tragedy struck when King Alaric fell gravely ill. On his deathbed, he summoned\s
                both princes to impart his final wishes. He urged them to unite for the good of the kingdom and warned them
                against letting jealousy or ambition drive them apart. However, with his passing came chaos. As Sirius\s
                ascended the thronehe was instantly consumed by his fear and insecurities, and in his moment of desperation\s
                fueled by paranoia, Sirius banished Lucian from Eldoria, claiming that his half-brother posed a threat\s
                to his reign.""";
        caption[9] = """
                Lucian was cast out into the wilderness, forced to flee from his home, and hid in the dense forests bordering
                 Eldoria. The pain of betrayal cut deep; he mourned for his lost home along with the bond he once shared\s
                with Sirius.\s""";
        caption[10] = """
                Two years into his exile, Lucian developed his leadership and fighting abilities among people who had also
                suffered injustice at the hands of King Sirius. It is here that Lucian also hears of King Sirius's harsh
                 reign. The kingdom that had flourished under King Alaric's reign now languished under tyranny. Because\s
                of this, Lucian decided to go back to Elodria—disenfranchised nobles and commoners alike—who shared tales
                of their suffering under King Sirius's tyrannical reign.\s""";
        caption[11] = """
                Thus began Lucian's journey back to Eldoria—a tale woven with threads of betrayal, ambition, and redemption.
                As he prepared to face his brother once more, he knew that this confrontation would determine both their\s
                fates and that of an entire kingdom yearning for freedom from tyranny.""";
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
            if(!bg){
               curr = gp.playCutScenes(0);
               bg = true;
            }

            //BACKGROUND
            g2.drawImage(title,0, 0, gp.screenWidth, gp.screenHeight, null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

            String text = "NEW GAME";
            int x = getXforCenteredText(text);
            int y = 448;
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
        else if(titleScreenState == 1) {
            rollCutScene();
        }else if(titleScreenState == 2){
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
            drawSubWindow(x - 53, y - 45, gp.tileSize * 3, gp.tileSize);
            g2.drawString(text,x,y);
            g2.drawImage(mage, 590, 220, 800, 450, null);
            if(commandNum == 1){
                g2.drawImage(magebutton, x- 64, 520, 200, 200, null);
            }

            text = "Ranger";
            x =getXforCenteredText(text) + 418;
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
                g2.drawString(line, x, y);
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
        int dframeY = frameY + frameHeight;
        int dframeHeight = gp.tileSize*3;
        // Draw description text
        int textX = frameX + 20;
        int textY = dframeY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));
        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()){
            drawSubWindow(frameX,dframeY, frameWidth,dframeHeight);
            for(String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line,textX,textY);
                textY += 32;
            }
        }
    }
    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));

        text = "Game Over";
        // Shadow
        g2.setColor(Color.BLACK);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;

        // Main
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        //RETRY
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text,x,y);
        if(commandNum == 0){
            g2.drawString(">",x-40,y);
        }

        //Back to the title Screen
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text,x,y);
        if(commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }
    }
    public void drawOptionScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        //SUB WINDOW
        int frameX = gp.tileSize*8;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = 670;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        switch(subState){
            case 0: optionsTop(frameX,frameY); break;
            case 1: optionFullScreenNotification(frameX, frameY); break;
            case 2: optionControl(frameX,frameY); break;
            case 3: optionEndGame(frameX,frameY); break;
        }

        gp.keyH.enterPressed = false;
    }
    public void optionsTop(int frameX, int frameY){
            int textX;
            int textY;

            //TITLE
            String text = "Options";
            textX = getXforCenteredText(text);
            textY = frameY + gp.tileSize;
            g2.drawString(text, textX, textY);

            // FULL SCREEN ON/OFF
            textX = frameX + gp.tileSize;
            textY += gp.tileSize*2;
            g2.drawString("Full Screen", textX, textY);
                if(commandNum == 0){
                    g2.drawString(">", textX-25,textY);
                    if(gp.keyH.enterPressed == true) {
                        if (!gp.fullScreenOn) {
                            gp.fullScreenOn = true;
                        } else if (gp.fullScreenOn) {
                            gp.fullScreenOn = false;
                        }
                        subState = 1;
                    }
                }

            // MUSIC
            textY += gp.tileSize;
            g2.drawString("Music",textX,textY);
                if(commandNum == 1){
                    g2.drawString(">", textX-25,textY);
                }

            // SE
            textY += gp.tileSize;
            g2.drawString("SE",textX,textY);
                if(commandNum == 2){
                    g2.drawString(">", textX-25,textY);
                }

            // CONTROL
            textY += gp.tileSize;
            g2.drawString("Control",textX,textY);
                if(commandNum == 3){
                    g2.drawString(">", textX-25,textY);
                    if(gp.keyH.enterPressed){
                        subState = 2;
                        commandNum = 0;
                    }
                }

            // END GAME
            textY += gp.tileSize;
            g2.drawString("End Game",textX,textY);
                if(commandNum == 4){
                    g2.drawString(">", textX-25,textY);
                    if(gp.keyH.enterPressed){
                        subState = 3;
                        commandNum = 0;
                    }
                }

            // BACK
            textY += gp.tileSize*2;
            g2.drawString("Back",textX,textY);
                if(commandNum == 5) {
                    g2.drawString(">", textX - 25, textY);
                    if(gp.keyH.enterPressed){
                        gp.gameState = gp.playState;
                        commandNum = 0;
                    }
                }

            // FULL SCREEN CHECK BOX
            textX = frameX + (int)(gp.tileSize*4.5);
            textY = frameY + gp.tileSize*2 + 40;
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(textX,textY,24,24);
                if(gp.fullScreenOn){
                    g2.fillRect(textX,textY,24,24);;
                }

            //MUSIC VOLUME
            textY += gp.tileSize;
            g2.drawRect(textX, textY, 120, 24);
            int volumeWidth = 24 * gp.sound.volumeScale;
            g2.fillRect(textX,textY,volumeWidth, 24);

            //SOUND EFFECT VOLUME
            textY += gp.tileSize;
            g2.drawRect(textX, textY, 120, 24);
            volumeWidth = 24 * gp.SE.volumeScale;
            g2.fillRect(textX,textY,volumeWidth, 24);

            gp.config.saveConfig();
    }
    public void optionFullScreenNotification(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "Please restart to game\nto apply the changes.";

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, textX,textY);
            textY += 40;
        }

        //BACK
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back",textX,textY);
            if(commandNum == 0){
                g2.drawString(">", textX-25,textY);
                if(gp.keyH.enterPressed){
                    subState = 0;
                }
            }

    }
    public void optionControl(int frameX, int frameY){
        int textX;
        int textY;

        //TEXT
        String text = "Control";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY = gp.tileSize*3;
        g2.drawString("Move",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Confirm/Attack",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Shoot/Cast",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Character Screen",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Pause",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Options",textX,textY); textY += gp.tileSize+15;

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("W A S D",textX,textY); textY += gp.tileSize+15;
        g2.drawString("ENTER",textX,textY); textY += gp.tileSize+15;
        g2.drawString("U",textX,textY); textY += gp.tileSize+15;
        g2.drawString("C",textX,textY); textY += gp.tileSize+15;
        g2.drawString("P",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Escape",textX,textY); textY += gp.tileSize+15;

        //BACK
        textX = frameX + gp.tileSize;
        textY = frameY + 620;
        g2.drawString("Back",textX,textY);
            if(commandNum == 0){
                g2.drawString(">", textX-25,textY);
                if(gp.keyH.enterPressed){
                    subState = 0;
                    commandNum = 3;
                }
            }
    }
    public void optionEndGame(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "Quit the game and \nreturn to the title screen?";

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line,textX,textY);
            textY += 40;
        }
        // OPTION YES
        String text ="Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize*3;
        g2.drawString(text,textX,textY);
        if(commandNum == 0){
            g2.drawString(">", textX-25,textY);
            if(gp.keyH.enterPressed){
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }

        text ="No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text,textX,textY);
        if(commandNum == 1){
            g2.drawString(">", textX-25,textY);
            if(gp.keyH.enterPressed){
                subState = 0;
                commandNum = 4;
            }
        }
    }
    public int getItemIndexOnSlot(){
        return slotCol + (slotRow*3);
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
        return gp.screenWidth/2 - length/2;
    }

    public int getXforAlignnToRightText(String text, int tailX){
        int length  = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return tailX - length;
    }


    public void handleFadeEffect() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastUpdateTime;

        if (elapsed > 2) { // ~60 FPS fade speed
            lastUpdateTime = currentTime;

            if (fadingOut) {
                opacity += 0.02f; // Fade to black
                if (opacity >= 1.0f) { // Fully faded out
                    opacity = 1.0f;
                    fadingOut = false; // Start fading in
                    transitioning = true; // Still in transition
                    System.out.println("fade in");
                    cutsceneNum++;
                }
            } else if (transitioning) {
                opacity -= 0.02f; // Fade back to the scene
                if (opacity <= 0.0f) { // Fully faded in
                    opacity = 0.0f;
                    transitioning = false; // Transition complete
                    System.out.println("fade out");
                }
            }
        }
    }

    public void fadein(){

    }

    public void drawTransition(){
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);


        if (counter >= 50) {
                counter = 0;
                gp.gameState = gp.playState;

                // Teleport the player to the new position
                gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
                gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;

                gp.currentMap = gp.eHandler.tempMap;  // Set the map to the temporary map

                gp.eHandler.previousEventX = gp.player.worldX;
                gp.eHandler.previousEventY = gp.player.worldY;

        }

    }

}
