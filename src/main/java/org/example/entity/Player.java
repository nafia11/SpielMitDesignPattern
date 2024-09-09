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
    private Image idleUp, idleDown;
    private boolean prevUpPressed, prevDownPressed;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private boolean prevMoving = false;

    // Constructor
    public Player(String username, KeyHandler keyHandler) {
        this.username = username;
        this.speed = 4;
        this.direction = "idle_down";
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
        idleUp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_idle_up.png")));
        idleDown = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_idle_down.png")));

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

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }

    // Update player position based on key input
    public void update() {
        boolean isMoving = false;

        if (keyHandler != null) {  // Check if keyHandler is not null
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

            // Check if the player was moving up or down and the corresponding key was released
            if (!isMoving) {
                if (prevUpPressed && !keyHandler.upPressed) {
                    direction = "idle_up";
                } else if (prevDownPressed && !keyHandler.downPressed) {
                    direction = "idle_down";
                }
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
            if (isMoving || prevMoving) {
                sendPositionUpdate(this.username, this.x, this.y);
            }
            // Update previous key states and movement state
            prevUpPressed = keyHandler.upPressed;
            prevDownPressed = keyHandler.downPressed;
            prevMoving = isMoving;
        }
        else {
            System.out.println("Keyhandler is null");

        }
    }

    // Send position update to the server
    private void sendPositionUpdate(String username, double x, double y) {
        //String message = String.format("POSITION_UPDATE %s,%f,%f,%s,%d", username, x, y, direction, spriteNum);
        String message = "POSITION_UPDATE " + username + "," + x + "," + y + ","+direction +","+ spriteNum;
        System.out.println(message);
        GameClient.getInstance().sendMessage(message);
    }


    // Draw the player on the canvas
    public void draw(GraphicsContext gc) {
        Image image = switch (direction) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            case "idle_up" -> idleUp;  // New idle state image
            case "idle_down" -> idleDown;
            default -> null;
        };

        if (image != null) {
                gc.drawImage(image, x, y,   70, 70);
            //System.out.println("Drawing player: " + username + " at position (" + x + ", " + y + ")");
        } else {
            System.out.println("Image is null for direction: " + direction);
        }
    }

}
