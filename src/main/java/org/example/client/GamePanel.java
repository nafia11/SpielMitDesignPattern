package org.example.client;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.entity.Player;
import org.example.game.KeyHandler;
import org.example.lobby.MainApp;
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
    private Player localPlayer;
    private Map<String, Player> players;
    private GraphicsContext gc;

    private long startTime;
    private final long gameDuration = 30000 ; // 1 minute
    private boolean gameEnded = false;

    public GamePanel(String localUsername) {
        gp = this;
        this.players = new HashMap<>();
        this.gc = this.getGraphicsContext2D();
        initialize();
        this.setWidth(screenWidth);
        this.setHeight(screenHeight);

        tileM = new TileManager(this);
        tileM.getMapData().printBlockLocations();
        tileM.getMapData().printChestLocations();

        this.keyHandler = new KeyHandler();

        this.setFocusTraversable(true);
        this.setOnKeyPressed(keyHandler::handleKeyPress);
        this.setOnKeyReleased(keyHandler::handleKeyRelease);

        this.localPlayer = new Player(localUsername, keyHandler);
        addPlayer(localPlayer);

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
        startTime = System.currentTimeMillis();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameEnded) {
                    updateTimer();
                    localPlayer.update(); // Update the local player
                    render();
                }
            }
        }.start();
    }

    private void updateTimer() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long timeLeft = gameDuration - elapsedTime;

        if (timeLeft <= 0) {
            endGame();
        }
    }

    private void drawTimer() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long timeLeft = gameDuration - elapsedTime;

        if (timeLeft > 0) {
            gc.setFill(Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font(20));

            gc.fillText("Timer: " + (timeLeft / 1000) + " sec", 500, 20);
        }
    }

    private void endGame() {
        if (!gameEnded) {
            gameEnded = true;
            // Determine the winner
            Player winner = players.values().stream()
                    .max((p1, p2) -> Integer.compare(p1.getInteractionCount(), p2.getInteractionCount()))
                    .orElse(null);

            String winnerMessage;
            if (winner != null) {
                winnerMessage = "Game Over! \nWinner: " + winner.getUsername() + " with " + winner.getInteractionCount() + " Chests";
            } else {
                winnerMessage = "Game Over! No players.";
            }

            // Show game over message
            MainApp.showGameOverMessage(winnerMessage);
        }
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
            player.setInteractionCount(interactionCount);
            System.out.println("gamepanel" + x + "" + y);
        } else {
            player = new Player(username, null);
            player.setPosition(x, y);
            player.setDirection(direction);
            player.setSpriteNum(spriteNum);
            player.setInteractionCount(interactionCount);
            players.put(username, player);
        }
        render();
    }



    private void render() {
        gc.clearRect(0, 0, screenWidth, screenHeight);

        tileM.draw(gc);
        for (Player player : players.values()) {
            player.draw(gc, localPlayer);
        }

        drawInteractionCounts();
        drawTimer();
    }

    private void drawInteractionCounts() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20.0)); // Set the font size and type

        int yOffset = 20;
        for (Player player : players.values()) {
            String text = player.getUsername() + ": " + player.getInteractionCount() + " points";
            gc.fillText(text, 20, yOffset);
            yOffset += 30;
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
