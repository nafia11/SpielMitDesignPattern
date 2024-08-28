package org.example.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.client.GameClient;
import org.example.game.KeyHandler;

import java.util.Objects;

public class Player {
    private double x, y;
    private double speed;
    private String direction;
    private String username;
    private Image up1, up2, down1, down2, left1, left2, right1, right2;
    private KeyHandler keyHandler;

    private int spriteCounter = 0;
    private int spriteNum = 1;

    // Constructor
    public Player(String username, KeyHandler keyHandler) {
        this.username = username;
        this.speed = 4;
        this.direction = "down";
        this.keyHandler = keyHandler;

        // Load images for animations
        up1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_1.png")));
        up2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_2.png")));
        down1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_1.png")));
        down2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_2.png")));
        left1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_1.png")));
        left2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_2.png")));
        right1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_1.png")));
        right2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_2.png")));
    }

    // Set the player's position
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Get the player's X position
    public double getX() {
        return x;
    }

    // Get the player's Y position
    public double getY() {
        return y;
    }

    // Get the player's username
    public String getUsername() {
        return username;
    }

    // Set the player's X position
    public void setX(double x) {
        this.x = x;
    }

    // Set the player's Y position
    public void setY(double y) {
        this.y = y;
    }

    // Update player position based on key input
    public void update() {
        boolean isMoving = false;

        if (keyHandler != null) {  // Check if keyHandler is not null
            if (keyHandler.upPressed) {
                direction = "up";
                y -= speed;
                System.out.println("Keyup is being pressed");
                isMoving = true;
            } else if (keyHandler.downPressed) {
                direction = "down";
                y += speed;
                System.out.println("Keydown is being pressed");
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
                // Notify the server of the new position
                sendPositionUpdate(this.username, this.x, this.y);
            } else {
                // Set to idle sprite when not moving
                spriteNum = 1;
            }
        }
        else {
            System.out.println("Keyhandler is null");

        }
    }

    // Send position update to the server
    private void sendPositionUpdate(String username, double x, double y) {
        String message = "POSITION_UPDATE " + username + "," + x + "," + y;
        GameClient.getInstance().sendMessage(message);
        // Replace with actual sending mechanism
    }

    // Draw the player on the canvas
    public void draw(GraphicsContext gc) {
        Image image = switch (direction) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            default -> null;
        };

        if (image != null) {
            gc.drawImage(image, x, y, 48, 48);
            //System.out.println("Drawing player: " + username + " at position (" + x + ", " + y + ")");
        } else {
            System.out.println("Image is null for direction: " + direction);
        }
    }
}
