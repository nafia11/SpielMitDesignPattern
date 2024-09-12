package org.example.tiles;

import javafx.scene.image.Image;

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
}
