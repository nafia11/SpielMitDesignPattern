package org.example.client;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import org.example.entity.Player;
import org.example.game.KeyHandler;

public class GamePanel extends Pane {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    private GraphicsContext gc;
    private Player player;
    private KeyHandler keyHandler;

    public GamePanel() {
        this.setPrefSize(screenWidth, screenHeight);
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        keyHandler = new KeyHandler();
        player = new Player(keyHandler);

        // Set up key event handlers
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> keyHandler.handleKeyPress(event));
        canvas.setOnKeyReleased(event -> keyHandler.handleKeyRelease(event));

        // Request focus on the canvas to capture key events
        canvas.requestFocus();

        // Start the game loop
        startGameLoop();
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();
    }

    private void update() {
        player.update();
    }

    private void render() {
        gc.clearRect(0, 0, screenWidth, screenHeight); // Clear the canvas
        player.draw(gc);
    }
}
