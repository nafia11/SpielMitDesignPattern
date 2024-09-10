package org.example.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.client.GameClient;
import org.example.client.GamePanel;
import org.example.game.CollisionDetection;
import org.example.game.KeyHandler;
import org.example.tiles.TileManager;

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
    private GamePanel gp;
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


    /*public void update() {
        boolean isMoving = false;

        if (keyHandler != null) {
            double newX = x;
            double newY = y;

            // Handle movement based on key presses
            if (keyHandler.upPressed) {
                direction = "up";
                newY -= speed;
                isMoving = true;
            } else if (keyHandler.downPressed) {
                direction = "down";
                newY += speed;
                isMoving = true;
            } else if (keyHandler.leftPressed) {
                direction = "left";
                newX -= speed;
                isMoving = true;
            } else if (keyHandler.rightPressed) {
                direction = "right";
                newX += speed;
                isMoving = true;
            }

            // Handle idle state transitions if not moving
            if (!isMoving) {
                if (direction.equals("up")) {
                    direction = "idle_up";
                } else if (direction.equals("down")) {
                    direction = "idle_down";
                }
            }


            TileManager tileManager = null;
            if (GamePanel.gp != null) {
                // Safe to call gp methods here
                tileManager = GamePanel.gp.getTileManager();
            } else {
                // Handle the case where gp is null (which should not happen if gp is set correctly)
                System.err.println("Warning: GamePanel instance is not initialized yet.");
            }

            // Check for collisions if the tileManager is available
            if (tileManager != null) {
                CollisionDetection collisionDetection = new CollisionDetection(GamePanel.gp, tileManager);
                if (!collisionDetection.isCollisionDetected(this, newX, newY)) {
                    // No collision, update position
                    x = newX;
                    y = newY;
                }
            }

            // Handle sprite animation for movement
            if (isMoving) {
                spriteCounter++;
                if (spriteCounter > 12) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
            } else {
                spriteNum = 1; // Reset to the first sprite when idle
            }

            prevMoving = isMoving;
        } else {
            System.out.println("KeyHandler is null");
        }
    }*/
    public void update() {
        boolean isMoving = false;

        if (keyHandler != null) {
            double newX = x;
            double newY = y;

            // Handle movement based on key presses
            if (keyHandler.upPressed) {
                direction = "up";
                newY -= speed;
                isMoving = true;
            } else if (keyHandler.downPressed) {
                direction = "down";
                newY += speed;
                isMoving = true;
            } else if (keyHandler.leftPressed) {
                direction = "left";
                newX -= speed;
                isMoving = true;
            } else if (keyHandler.rightPressed) {
                direction = "right";
                newX += speed;
                isMoving = true;
            }

            // Handle idle state transitions if not moving
            if (!isMoving) {
                if (direction.equals("up")) {
                    direction = "idle_up";
                } else if (direction.equals("down")) {
                    direction = "idle_down";
                }
            }

            TileManager tileManager = null;
            if (GamePanel.gp != null) {
                // Safe to call gp methods here
                tileManager = GamePanel.gp.getTileManager();
            } else {
                System.err.println("Warning: GamePanel instance is not initialized yet.");
            }

            // Check for collisions first
            boolean collisionDetected = false;
            if (tileManager != null) {
                CollisionDetection collisionDetection = new CollisionDetection(GamePanel.gp, tileManager);
                collisionDetected = collisionDetection.isCollisionDetected(this, newX, newY);
            }

            // If no collision, check boundaries and update position
            if (!collisionDetected) {
                // Define the boundaries
                double maxX = 2048;  // Maximum X coordinate for the player
                double maxY = 2148;  // Maximum Y coordinate for the player
                double minX = 400;     // Minimum X coordinate (left boundary)
                double minY = 300;     // Minimum Y coordinate (top boundary)

                // Ensure the player stays within bounds
                if (newX >= minX && newX <= maxX) {
                    x = newX;
                }
                if (newY >= minY && newY <= maxY) {
                    y = newY;
                }
            }

            // Handle sprite animation for movement
            if (isMoving) {
                spriteCounter++;
                if (spriteCounter > 12) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
                sendPositionUpdate(this.username, this.x, this.y);
            } else {
                spriteNum = 1; // Reset to the first sprite when idle
            }

            // Always notify the server if the player is moving or has just stopped moving
            if (isMoving || prevMoving) {
                sendPositionUpdate(this.username, this.x, this.y);
            }

            prevMoving = isMoving;
        } else {
            System.out.println("KeyHandler is null");
        }
    }


    // Send position update to the server
    private void sendPositionUpdate(String username, double x, double y) {
        //String message = String.format("POSITION_UPDATE %s,%f,%f,%s,%d", username, x, y, direction, spriteNum);
        String message = "POSITION_UPDATE " + username + "," + x + "," + y + ","+direction +","+ spriteNum;
        System.out.println(message);
        GameClient.getInstance().sendMessage(message);
    }

    // Inside Player class or movement handling logic

    // Draw the player on the canvas
    public void draw(GraphicsContext gc , Player localPlayer) {
        Image image = switch (direction) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            case "idle_up" -> idleUp;
            case "idle_down" -> idleDown;
            default -> null;
        };

        if (image != null) {

            double screenX = (x - localPlayer.getX()) + (GamePanel.gp.getScreenWidth() / 2) - 35; // Offset based on sprite width
            double screenY = (y - localPlayer.getY()) + (GamePanel.gp.getScreenHeight() / 2) - 35; // Offset based on sprite height

            gc.drawImage(image, screenX, screenY, 100, 100);
        } else {
            System.out.println("Image is null for direction: " + direction);
        }


    }

}

