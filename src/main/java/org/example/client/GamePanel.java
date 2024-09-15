package org.example.client;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.entity.Player;
import org.example.game.KeyHandler;
import org.example.tiles.TileManager;

import java.util.HashMap;
import java.util.Map;

public class GamePanel extends Canvas {
    public static GamePanel gp;
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    private TileManager tileM;
    //TileManager tileM = new TileManager(this);



    private KeyHandler keyHandler;
    private Player localPlayer; // Store the local player instance
    private Map<String, Player> players;
    private GraphicsContext gc;

    public GamePanel(String localUsername) {
        gp = this;
        //super();
        this.players = new HashMap<>();
        this.gc = this.getGraphicsContext2D();
        initialize();
        this.setWidth(screenWidth);
        this.setHeight(screenHeight);

        // Initialize TileManager
        tileM = new TileManager(this);
        tileM.getMapData().printBlockLocations();
        tileM.getMapData().printChestLocations();

        // Initialize KeyHandler
        this.keyHandler = new KeyHandler();

        // Ensure the canvas can receive key events
        this.setFocusTraversable(true);
        this.setOnKeyPressed(keyHandler::handleKeyPress);
        this.setOnKeyReleased(keyHandler::handleKeyRelease);

        // Create the local player and assign the key handler to it
        this.localPlayer = new Player(localUsername, keyHandler);
        addPlayer(localPlayer);

        // Request focus to capture key inputs
        this.requestFocus();

        startGameLoop();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private void startGameLoop() {
        // Set up an AnimationTimer to check key states and update the local player's position
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (localPlayer != null) {
                    localPlayer.update(); // Only update the local player
                }
                render();
            }
        }.start();
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    private void initialize() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, screenWidth, screenHeight);
    }

    public void setInitialPositions(Map<String, double[]> initialPositions) {
        for (Map.Entry<String, double[]> entry : initialPositions.entrySet()) {
            String username = entry.getKey();
            double[] pos = entry.getValue();
            Player player = players.get(username);
            if (player != null) {
                player.setPosition(pos[0], pos[1]);
            } else {
                player = new Player(username, null);
                player.setPosition(pos[0], pos[1]);
                players.put(username, player);
            }
        }
        render();
    }

    public void updatePlayerPosition(String username, double x, double y, String direction, int spriteNum, int interactionCount) {
        Player player = players.get(username);
        if (player != null) {
            player.setPosition(x, y);
            player.setDirection(direction);
            player.setSpriteNum(spriteNum);
            player.setInteractionCount(interactionCount); // Update the interaction count
            System.out.println("gamepanel" + x + "" + y);
        } else {
            player = new Player(username, null);
            player.setPosition(x, y);
            player.setDirection(direction);
            player.setSpriteNum(spriteNum);
            player.setInteractionCount(interactionCount); // Set the interaction count for a new player
            players.put(username, player);
        }
        render();
    }



    private void render() {
        gc.clearRect(0, 0, screenWidth, screenHeight);

        // Draw the world (tiles) relative to the player's position
        tileM.draw(gc);

        // Draw all players relative to the local player
        for (Player player : players.values()) {
            player.draw(gc, localPlayer);
        }

        // Draw interaction counts
        drawInteractionCounts();
    }

    private void drawInteractionCounts() {
        gc.setFill(Color.BLACK); // Set the text color
        gc.setFont(new Font("Arial", 20.0)); // Set the font size and type

        int yOffset = 20; // Start position for the text on the y-axis
        for (Player player : players.values()) {
            String text = player.getUsername() + ": " + player.getInteractionCount() + " points";
            gc.fillText(text, 20, yOffset); // Draw the text on the canvas at a fixed position
            yOffset += 30; // Move down for the next player's interaction count
        }
    }




    public Map<String, Player> getPlayers() {
        return players;
    }
    public TileManager getTileManager() {
        return tileM;
    }
    public int getTileSize() {
        return tileSize;
    }

    public void addPlayer(Player player) {
        players.put(player.getUsername(), player);
    }

    public Player getPlayer(String username) {
        return players.get(username);
    }
}