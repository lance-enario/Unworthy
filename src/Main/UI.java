package Main;

import Entity.Entity;
import objects.assets;
import objects.obj_Coin;
import objects.obj_Heart;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.Timer;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaBold;
    BufferedImage heart_full,heart_blank,heart_half,coin;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;

    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen 1: the second screen
    public BufferedImage warrior,heroInv, mage, ranger, title, charselect, back, warriorbutton, magebutton, rangerbutton,shop,  dialogueBanner, outskirtsBanner,villageBanner, castleBanner, dungeonBanner, options;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int counter = 0;
    int negCounter = 255;
    int subState = 0;

    // for cutscenes
    public BufferedImage[] cutscenes = new BufferedImage[14];
    public URL[] narrations = new URL[12];
    boolean[] soundPlayed = new boolean[12];
    boolean bg = false;
    public int cutsceneNum = 0;
    String[] caption = new String[12];
    String displayedDialogue = "";
    Clip curr, titleBg;
    float opacity = 0.0f;
    public boolean fadingOut = false;
    public boolean transitioning = false; // True during a transition
    private long lastUpdateTime = System.currentTimeMillis(); // Timer to control fade speed
    public Entity npc; // easy access for inventory

    public String currentDialogue = "";
    public String[] signDialogue = new String[20];
    private int charIndex = 0; // Current character index being displayed
    private Timer dialogueTimer; // Timer to control the typing effect


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
        dialogueBanner = Asset.banner;
        outskirtsBanner = Asset.outskirts;
        villageBanner = Asset.village;
        castleBanner = Asset.castle;
        dungeonBanner = Asset.dungeon;
        shop = Asset.shopbox;
        options = Asset.optionsbox;
        heroInv = Asset.herobox;



        //CREATE HUD OBJECT
        Entity heart = new obj_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity goldCoin = new obj_Coin(gp);
        coin = goldCoin.up1;

        setSigns();
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
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        // OPTION STATE
        if(gp.gameState == gp.optionState){
            drawOptionScreen();
        }

        // TRANSITION STATE
        if(gp.gameState == gp.transitionState){
            drawTransition();
            drawMapRegion();
        }
        // GAME OVER STATE
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        // BUY STATE
        if(gp.gameState == gp.buyState){
            drawBuyScreen();
        }

    }

    public void drawBuyScreen(){
        switch (subState){
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
        }
        gp.keyH.enterPressed = false;
    }

    public void trade_select(){
        drawDialogueScreen();

        // DRAW WINDOW
        int x = gp.tileSize * 15;
        int y = gp.tileSize + 20;
        int width = gp.tileSize * 3;
        int height = (int)(gp.tileSize * 3.5);

        //  DRAW TEXTS
        x += gp.tileSize;
        y += gp.tileSize+15;
        g2.drawString("Buy", x, y);
        if(commandNum == 0){
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed == true){
                subState = 1;
            }
        }
        y += gp.tileSize+15;

        g2.drawString("Leave", x, y);
        if(commandNum == 1){
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed == true){
                commandNum = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "Thank you! Come again!";
            }
        }
    }

    public void trade_buy(){
        // DRAW PLAYER INVENTORY
        drawInventory(gp.player, false);
        // DRAW NPC INVENTORY
        drawInventory(npc, true);

        // DRAW HINT WINDOW
        int x = gp.tileSize*17;
        int y = gp.tileSize*9;
        g2.drawString("Your coin: " + gp.player.coins, x+70,y-20);

        //DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol,npcSlotRow);
        if(itemIndex < npc.inventory.size()){
            x = 336;
            y = 542;
            g2.drawImage(coin, x-15, y-23,80,80,null);

            int price = npc.inventory.get(itemIndex).price;
            String text = "" + price;
            g2.drawString(text,x+44,y+27);

            //BUY AN ITEM
            if(gp.keyH.enterPressed){
                if(npc.inventory.get(itemIndex).price > gp.player.coins){
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You don't have sufficient coin to buy this item!";
                    drawDialogueScreen();
                }
                else{
                    if(gp.player.canObtainItem(npc.inventory.get(itemIndex))){
                        gp.player.coins -= npc.inventory.get(itemIndex).price;
                    }
                    else{
                        subState = 0;
                        gp.gameState = gp.dialogueState;
                        currentDialogue = "Your inventory is full, you cannot carry any more items";
                    }
                }
            }
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
        if(cutsceneNum == 1)drawIntro();
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
       // startTypewriterEffect(caption[cutsceneNum]);


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
                        cutsceneNum++;
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

       // startTypewriterEffect(caption[cutsceneNum]);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        int y = 130, x = 200;
        if(cutsceneNum == 0){
            drawDialogueScreen();
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
                Alaric and Queen Seraphina. The royal family was adored by the people, who often spoke of their \s
                kindness and wisdom. However,beneath this idyllic surface lay the complexities of royal lineage\s
                sand ambition.""";
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

    public void setSigns(){
        signDialogue[0] = "YAAWAAA";
        signDialogue[1] = "hi";
        signDialogue[2] = "hello";
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
        }else if(titleScreenState == 2) {
            stopCurr();
            //BACKGROUND
            g2.drawImage(charselect, 0, 0, gp.screenWidth, gp.screenHeight, null);

            //CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Warrior";
            int x = getXforCenteredText(text) - 418;
            g2.drawImage(warrior, 172, 220, 800, 450, null);
            if (commandNum == 0) {
                g2.drawImage(warriorbutton, x - 49, 520, 200, 200, null);
            }

            //Mage
            x = getXforCenteredText(" ");
            g2.drawImage(mage, x - 167, 220, 800, 450, null);
            if (commandNum == 1) {
                g2.drawImage(magebutton, x - 93, 520, 200, 200, null);
            }

            //Ranger
            x = getXforCenteredText(text) + 418;
            g2.drawImage(ranger, gp.tileSize * 16, 220, 800, 450, null);
            if (commandNum == 2) {
                g2.drawImage(rangerbutton, x - 36, 520, 200, 200, null);
            }

            //Back
            x = getXforCenteredText(text);
            if (commandNum == 3) {
                g2.drawImage(back, x - 100, 600, 300, 300, null);
            }

        }
    }

    public void drawMapRegion(){
        if(gp.currentMap == 0){
            g2.drawImage(villageBanner,gp.tileSize *3,40, 1200, 600, null);
        }else if(gp.currentMap == 1){
            g2.drawImage(outskirtsBanner,gp.tileSize *3,40, 1200, 600, null);
        }else if(gp.currentMap == 2){
            g2.drawImage(dungeonBanner,gp.tileSize *3,40, 1200, 600, null);
        }else if(gp.currentMap == 3){
            g2.drawImage(castleBanner,gp.tileSize *3,40, 1200, 600, null);
        }
    }

    public void drawDialogueScreen(){
            int x = gp.tileSize*3  , y = gp.tileSize, width = gp.screenWidth - (gp.tileSize*6), height = gp.tileSize*4;
            g2.drawImage(dialogueBanner, 0, 0, gp.screenWidth, 400, null);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
            x += gp.tileSize;
            y+= gp.tileSize;

            for(String line :currentDialogue.split("\n")){
                g2.drawString(line, x, y + 15);
                y += 40;
            }
    }

    public void startTypewriterEffect(String dialogue) {
        currentDialogue = dialogue;

        charIndex = 0;

        if (dialogueTimer != null) {
            dialogueTimer.stop(); // Stop any previous timer
        }

        // Timer to reveal characters one by one
        dialogueTimer = new Timer(50, new ActionListener() { // Adjust delay (e.g., 50ms)
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex < currentDialogue.length()) {
                    displayedDialogue += currentDialogue.charAt(charIndex); // Add the next character
                    charIndex++;
                    gp.repaint(); // Redraw the screen
                } else {
                    dialogueTimer.stop(); // Stop when the dialogue is fully displayed
                }
            }
        });
        dialogueTimer.start();
    }


    public void drawCharacterScreen(){
        //CREATE A FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
       // drawSubWindow(frameX, frameY, frameWidth,frameHeight);
        g2.drawImage(options, 0, 0, options.getWidth() - 450, options.getHeight() - 400, null);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 40;
        int textY = frameY + 120;
        final int lineHeight = 45;

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

        //VALUES
        int tailX = (frameX + frameWidth) - 30;

        //RESET textY
        textY = frameY + 120;
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

    public void drawInventory(Entity entity, boolean cursor){

        int frameX,frameY,frameWidth,frameHeight,slotCol,slotRow;

        if(entity == gp.player){
            frameX = gp.tileSize*17;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
            g2.drawImage(shop, 1050, 0, shop.getWidth() - 400, shop.getHeight() - 400, null);
        }else{
            frameX = gp.tileSize;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
            g2.drawImage(shop, 0, 0, shop.getWidth() - 280, shop.getHeight() - 400, null);
        }

        //SLOT
        final int slotXstart = frameX + 80;
        final int slotYstart = frameY + 80;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+4;

        //Draw player's Inventory
        for(int i = 0; i < entity.inventory.size(); i++){

            //Equip Cursor
            if(entity.inventory.get(i)  == entity.currentWeapon ||
                entity.inventory.get(i) == entity.currentShield){
                g2.setColor(new Color(240,190,90));
                g2.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }
            g2.drawImage(entity.inventory.get(i).down1,slotX,slotY,null);

            // DISPLAY AMOUNT
            if(entity.inventory.get(i).amount > 1){

                g2.setFont(g2.getFont().deriveFont(32f));
                int amountX;
                int amountY;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXforAlignnToRightText(s, slotX+44);
                amountY = slotY + gp.tileSize;

                // SHADOW
                g2.setColor(new Color(60,60,60));
                g2.drawString(s,amountX,amountY);
                //NUMBER
                g2.setColor(Color.white);
                g2.drawString(s,amountX-3,amountY-3);

            }

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14 || i == 19){
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        if(cursor){
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize ;
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
            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if(itemIndex < entity.inventory.size()){
                for(String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line,textX +  50,textY + 120);
                    textY += 32;
                }
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

        g2.drawImage(options, 450, 0, options.getWidth() - 280, options.getHeight() - 400, null);

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
            textY = frameY + 120;
            g2.drawString(text, textX, textY);

            // FULL SCREEN ON/OFF
            textX = frameX + gp.tileSize;
            textY = gp.tileSize*4;
            g2.drawString("Full Screen", textX +50, textY);
                if(commandNum == 0){
                    g2.drawString(">", textX + 15,textY);
                    if(gp.keyH.enterPressed) {
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
            g2.drawString("Music",textX+50,textY);
                if(commandNum == 1){
                    g2.drawString(">", textX+15,textY);
                }

            // SE
            textY += gp.tileSize;
            g2.drawString("SE",textX + 50,textY);
                if(commandNum == 2){
                    g2.drawString(">", textX + 15,textY);
                }

            // CONTROL
            textY += gp.tileSize;
            g2.drawString("Control",textX+ 50,textY);
                if(commandNum == 3){
                    g2.drawString(">", textX+15,textY);
                    if(gp.keyH.enterPressed){
                        subState = 2;
                        commandNum = 0;
                    }
                }

            // END GAME
            textY += gp.tileSize;
            g2.drawString("End Game",textX+ 50 ,textY);
                if(commandNum == 4){
                    g2.drawString(">", textX +15,textY);
                    if(gp.keyH.enterPressed){
                        subState = 3;
                        commandNum = 0;
                    }
                }

            // BACK
            textY += gp.tileSize*2;
            g2.drawString("Back",textX+ 50,textY);
                if(commandNum == 5) {
                    g2.drawString(">", textX + 15, textY);
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
            g2.drawRect(textX , textY, 120, 24);
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
        int textX = frameX + 125;
        int textY = frameY + 215;

        currentDialogue = "Please restart to game\nto apply the changes.";

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, textX,textY);
            textY += 40;
        }

        //BACK
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back",getXforCenteredText("") - 25 ,textY);
                if(commandNum == 0){
                g2.drawString(">", getXforCenteredText("") - 40,textY);
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
        textY = frameY + gp.tileSize + 33;
        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(30F));
        textX = frameX + 75;
        textY = 215;
        g2.drawString("Move",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Confirm/Attack",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Shoot/Cast",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Character Screen",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Pause",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Options",textX,textY); textY += gp.tileSize+15;

        textX = frameX + 330;
        textY = 215;
        g2.drawString("W A S D",textX,textY); textY += gp.tileSize+15;
        g2.drawString("ENTER",textX,textY); textY += gp.tileSize+15;
        g2.drawString("U",textX,textY); textY += gp.tileSize+15;
        g2.drawString("C",textX,textY); textY += gp.tileSize+15;
        g2.drawString("P",textX,textY); textY += gp.tileSize+15;
        g2.drawString("Escape",textX,textY); textY += gp.tileSize+15;

        //BACK
        textX = getXforCenteredText("");
        textY = frameY + 620 ;
        g2.drawString("Back",textX - 25,textY);
            if(commandNum == 0){
                g2.drawString(">", textX-50,textY);
                if(gp.keyH.enterPressed){
                    subState = 0;
                    commandNum = 3;
                }
            }
    }
    public void optionEndGame(int frameX, int frameY){
        int textX = frameX + 85;
        int textY = frameY + 215;

        currentDialogue = "Quit the game and \nreturn to the title screen?";

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line,textX + 15,textY);
            textY += 40;
        }
        // OPTION YES
        String text ="Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize*2;
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

    public int getItemIndexOnSlot(int slotCol, int slotRow){
        return slotCol + (slotRow*3);
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(101,38,3, 255);
        g2.setColor(c);
//        g2.fillRoundRect(x, y, width, height, 25, 25);
        //dialogueBanner.createGraphics();
//        TexturePaint tp = new TexturePaint(dialogueBanner, new Rectangle(0, 0, 128,128 ));
//        g2.setPaint (tp);
        g2.fillRoundRect (x, y,width,height, 25, 25);


        c = new Color(50,19,1);
        g2.setColor(c);
        g2.setStroke( new BasicStroke(3) );
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

    public void drawIntro(){
        if (negCounter > 0) {
            negCounter--;
        }
        int alpha = Math.max(0, negCounter);
        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }


    public void drawTransition(){
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);


        if (counter >= 50) {
                while(counter > 0) {
                    counter--;
                    g2.setColor(new Color(0,0,0,counter*5));

                }
               // counter = 0;
            if(counter == 0) {
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

}
