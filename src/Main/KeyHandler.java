package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

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
            }
            else if(gp.ui.titleScreenState == 1){

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
        //PLAY STATE
        if(gp.gameState == gp.playState) {
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
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }

            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.pauseState;
            }
        //PAUSE STATE
        } else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_ESCAPE){
                gp.gameState = gp.playState;
            }
        } else if(gp.gameState == gp.dialogueState){
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
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
