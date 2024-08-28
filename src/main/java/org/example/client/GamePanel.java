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
    private Player player1, player2;
    private KeyHandler keyHandler1;
    private KeyHandler keyHandler2;

    public GamePanel() {
        this.setPrefSize(screenWidth, screenHeight);
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        keyHandler1 = new KeyHandler();
        player1 = new Player("ww", keyHandler1);
        player2 = new Player("22", keyHandler2);

        // Set up key event handlers
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> keyHandler1.handleKeyPress(event));
        canvas.setOnKeyReleased(event -> keyHandler1.handleKeyRelease(event));

        // Request focus on the canvas to capture key events
        canvas.requestFocus();

        // Start the game loop
        startGameLoop();

        // Test positions
        testPlayerPositions();
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
        player1.update();
        player2.update();
    }

    private void render() {
        gc.clearRect(0, 0, screenWidth, screenHeight); // Clear the canvas
        player1.draw(gc);
        player2.draw(gc);
    }

    private void testPlayerPositions() {
        // Set player positions for testing
        player1.setPosition(50, 50);
        player2.setPosition(100, 100);

        // Optionally set swinging behavior, I will add it later
        /*player1.setSwingPosition(200, 200);
        player2.setSwingPosition(300, 300);*/
    }

}