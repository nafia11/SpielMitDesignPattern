package org.example.tiles;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnimationFactory {
    private static final Map<Character, AnimatedTile> animationCache = new HashMap<>();

    public static AnimatedTile createAnimatedTile(char tileChar) {
        // Check if the tile is already cached
        if (animationCache.containsKey(tileChar)) {
            return animationCache.get(tileChar);
        }

        AnimatedTile animatedTile;
        switch (tileChar) {
            case 'C':
                animatedTile = new AnimatedTile(TileType.CHEST, true,
                        new Image[]{
                                new Image(Objects.requireNonNull(AnimationFactory.class.getResourceAsStream("/tiles/Box1.png"))),
                                new Image(Objects.requireNonNull(AnimationFactory.class.getResourceAsStream("/tiles/Box2.png"))),
                                new Image(Objects.requireNonNull(AnimationFactory.class.getResourceAsStream("/tiles/Box3.png")))
                        });
                break;
            // Add more cases for other animated tiles as needed
            default:
                throw new IllegalArgumentException("Invalid tile character for animation: " + tileChar);
        }

        animationCache.put(tileChar, animatedTile);
        return animatedTile;
    }
}
