package org.example.game;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler {
    public boolean upPressed, downPressed, leftPressed, rightPressed, ePressed;

    public void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.W) {
            upPressed = true;
        }
        if (code == KeyCode.S) {
            downPressed = true;
        }
        if (code == KeyCode.A) {
            leftPressed = true;
        }
        if (code == KeyCode.D) {
            rightPressed = true;
        }
        if (code == KeyCode.E) {
            ePressed = true;
        }
    }

    public void handleKeyRelease(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.W) {
            upPressed = false;
        }
        if (code == KeyCode.S) {
            downPressed = false;
        }
        if (code == KeyCode.A) {
            leftPressed = false;
        }
        if (code == KeyCode.D) {
            rightPressed = false;
        }
        if (code == KeyCode.E) {
            ePressed = false;
        }
    }
}
