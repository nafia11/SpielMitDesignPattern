package org.example.tiles;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class AnimatedTile extends Tile {
    private Image[] images;
    private int currentFrameIndex;

    public AnimatedTile(TileType tileType, boolean isInteractable, Image[] images) {
        super(tileType, isInteractable);
        this.images = images;
        this.currentFrameIndex = 0;
    }

    public Image getCurrentImage() {
        if (images != null && images.length > 0) {
            return images[currentFrameIndex];
        }
        return null;
    }

    // Method to advance the animation frame
    public void nextFrame() {
        if (images != null && images.length > 0) {
            currentFrameIndex = (currentFrameIndex + 1) % images.length;
        }
    }

    // Trigger an interaction with the chest (animation)
    public void interact() {
        if (images != null && images.length > 0) {
            // Show the second frame immediately
            currentFrameIndex = 1;

            // Create a PauseTransition to reset to the original frame after 10 seconds
            PauseTransition revertDelay = new PauseTransition(Duration.seconds(10));
            revertDelay.setOnFinished(event -> resetToOriginalFrame());
            revertDelay.play();
        }
    }


    // Reset the tile to its original state
    private void resetToOriginalFrame() {
        currentFrameIndex = 0;
    }
}
