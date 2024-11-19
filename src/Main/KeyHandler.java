package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    boolean showDebugText = false;
    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, bscAtkPressed, skill1Pressed, skill2Pressed, skill3Pressed, enterPressed;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        //TITLE STATE
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        //PLAY STATE
        if(gp.gameState == gp.playState) {
            playState(code);

            //PAUSE STATE
        } else if (gp.gameState == gp.pauseState) {
            pauseState(code);
            //DIALOGUE STATE
        } else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        //OPTION STATE
        } else if(gp.gameState == gp.optionState){
            optionState(code);
        }
        //CHARACTER STATE
        else if(gp.gameState == gp.characterState){
            characterState(code);
        }
        //GAME OVER STATE
        else if(gp.gameState == gp.gameOverState){
            gameOverState(code);
        }

        //debug text
        if (code == KeyEvent.VK_T){
            if(!showDebugText){
                showDebugText = true;
            } else if(showDebugText){
                showDebugText = false;
            }
        }
        //refresh map
        if (code == KeyEvent.VK_R){
            switch(gp.currentMap){
                case 0: gp.tileM.loadMap("/Maps/Stage1.txt",0); break;
                case 1: gp.tileM.loadMap("/Maps/Stage2.txt",1); break;
                case 2: gp.tileM.loadMap("/Maps/Stage2.txt",2); break;
            }
        }
    }

    public void titleState(int code){
        if(gp.ui.titleScreenState == 0){

            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                    //gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) {
                    //add later
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }else if(gp.ui.titleScreenState == 1){
            if(code == KeyEvent.VK_ENTER){
                gp.ui.cutsceneNum++;
                //gp.ui.fadingOut = true;
            }
            if(code == KeyEvent.VK_D){
                gp.ui.titleScreenState = 2;
            }
        }
        else if(gp.ui.titleScreenState == 2){

            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 3;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 3){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    System.out.println("Do some warrior specific stuff!");
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNum == 1) {
                    System.out.println("Do some mage specific stuff!");
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNum == 2) {
                    System.out.println("Do some ranger specific stuff!");
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNum == 3) {
                    gp.ui.titleScreenState = 0;
                }
            }
        }
    }

    public void playState(int code){
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }

        if (code == KeyEvent.VK_U) {
            bscAtkPressed = true;
        }

        if (code == KeyEvent.VK_I) {
            skill1Pressed = true;
        }

        if (code == KeyEvent.VK_O) {
            skill2Pressed = true;
        }

        if (code == KeyEvent.VK_P) {
            skill3Pressed = true;
        }
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }

        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.optionState;
        }
    }

    public void pauseState(int code){
        if (code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code){
        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code){

        // Need soundeffect
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
            }
        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 6) {
                gp.ui.slotRow++;
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 2) {
                gp.ui.slotCol++;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }
    public void optionState(int code){
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        int maxCommandNum = 0;
        switch(gp.ui.subState){
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1; break;
        }

        if (code == KeyEvent.VK_W) {
            // NEED SE
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if (code == KeyEvent.VK_S) {
            // NEED SE
            gp.ui.commandNum++;
            if (gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_A) {
            if (gp.ui.subState == 0) {
                if (gp.ui.commandNum == 1 && gp.sound.volumeScale > 0) {
                    gp.sound.volumeScale--;
                    gp.sound.checkVolume();
                    //NEED SE
                }
                if (gp.ui.commandNum == 2 && gp.SE.volumeScale > 0) {
                    gp.SE.volumeScale--;
                    //NEED SE
                }
            }
        }
        if (code == KeyEvent.VK_D) {
            if (gp.ui.subState == 0) {
                if (gp.ui.commandNum == 1 && gp.sound.volumeScale < 5) {
                    gp.sound.volumeScale++;
                    gp.sound.checkVolume();
                    //NEED SE
                }
                if (gp.ui.commandNum == 2 && gp.SE.volumeScale < 5) {
                    gp.SE.volumeScale++;
                    //NEED SE
                }
            }
        }
    }
    public void gameOverState(int code){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
                //NEED SE
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1){
                    gp.ui.commandNum = 0;
                }
                //NEED SE
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                    gp.restart();
                } else if(gp.ui.commandNum == 1) {
                    gp.ui.titleScreenState = 0;
                    gp.gameState = gp.titleState;
                    gp.restart();
                }
            }
        }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }

        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }

        if(code == KeyEvent.VK_S){
            downPressed = false;
        }

        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_U){
            bscAtkPressed = false;
        }

        if(code == KeyEvent.VK_I){
            skill1Pressed = false;
        }

        if(code == KeyEvent.VK_O){
            skill2Pressed = false;
        }

        if(code == KeyEvent.VK_P){
            skill3Pressed = false;
        }

        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
    }
}
