package org.example.tiles;

import javafx.scene.image.Image;

public class Tile {
    private Image image;
    private TileType tileType;
    private boolean isInteractable;
    private boolean collision;

    // Constructor
    public Tile(TileType tileType, boolean isInteractable) {
        this.tileType = tileType;
        this.isInteractable = isInteractable;
        this.collision = false; // Default to no collision unless specified
    }

    // Getter and setter for image
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // Getter and setter for collision
    public boolean hasCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    // Other getter methods if needed
    public TileType getTileType() {
        return tileType;
    }

    public boolean isInteractable() {
        return isInteractable;
    }
}
