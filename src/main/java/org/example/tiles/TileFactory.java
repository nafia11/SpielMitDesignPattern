package org.example.tiles;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TileFactory {
    private static final Map<Character, Tile> tileCache = new HashMap<>();

    public static Tile createTile(char tileChar) {
        // Check if the tile is already cached
        if (tileCache.containsKey(tileChar)) {
            return tileCache.get(tileChar);
        }

        Tile tile = new Tile();
        switch (tileChar) {
            case 'F' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Floor1.png")));
            case 'f' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Floor2.png")));
            case 'B' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Block.png")));
            case 'b' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Block3.png")));
            case 'w' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Block2.png")));
            case 'E' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/End.png")));
            case 'C' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Box1.png")));

        }

        tileCache.put(tileChar, tile);
        return tile;
    }
}

