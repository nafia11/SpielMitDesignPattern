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
            // Show animation by cycling through images
            nextFrame();

            // Use a transition to move through frames and revert back after a delay
            PauseTransition delay = new PauseTransition(Duration.seconds(40));
            delay.setOnFinished(event -> nextFrame());  // Advance to the next frame after 1 second
            delay.play();

            // Revert back to the original state after a longer delay
            PauseTransition revertDelay = new PauseTransition(Duration.seconds(60)); // 1 minute delay
            revertDelay.setOnFinished(event -> resetToOriginalFrame());
            revertDelay.play();
        }
    }

    // Reset the tile to its original state
    private void resetToOriginalFrame() {
        currentFrameIndex = 0;
    }
}
