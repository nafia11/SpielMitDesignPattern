package org.example.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.game.KeyHandler;

public class Player {
    private double x, y;
    private double speed;
    private String direction;
    private Image up1, up2, down1, down2, left1, left2, right1, right2;
    private KeyHandler keyHandler;

    private int spriteCounter = 0;
    private int spriteNum = 1;

    public Player(KeyHandler keyHandler) {
        this.x = 100;
        this.y = 100;
        this.speed = 4;
        this.direction = "down";
        this.keyHandler = keyHandler;

        // Load images for animations
        up1 = new Image(getClass().getResourceAsStream("/player/boy_up_1.png"));
        up2 = new Image(getClass().getResourceAsStream("/player/boy_up_2.png"));
        down1 = new Image(getClass().getResourceAsStream("/player/boy_down_1.png"));
        down2 = new Image(getClass().getResourceAsStream("/player/boy_down_2.png"));
        left1 = new Image(getClass().getResourceAsStream("/player/boy_left_1.png"));
        left2 = new Image(getClass().getResourceAsStream("/player/boy_left_2.png"));
        right1 = new Image(getClass().getResourceAsStream("/player/boy_right_1.png"));
        right2 = new Image(getClass().getResourceAsStream("/player/boy_right_2.png"));
    }

    public void update() {
        boolean isMoving = false;

        if (keyHandler.upPressed) {
            direction = "up";
            y -= speed;
            isMoving = true;
        } else if (keyHandler.downPressed) {
            direction = "down";
            y += speed;
            isMoving = true;
        } else if (keyHandler.leftPressed) {
            direction = "left";
            x -= speed;
            isMoving = true;
        } else if (keyHandler.rightPressed) {
            direction = "right";
            x += speed;
            isMoving = true;
        }

        if (isMoving) {
            // Animation logic
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {
            // Set to idle sprite when not moving
            spriteNum = 1;
        }
    }

    public void draw(GraphicsContext gc) {
        Image image = switch (direction) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            default -> null;
        };

        if (image != null) {
            gc.drawImage(image, x, y, 48, 48); // Adjust size as needed
        }
    }
}
