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
            case 'D' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Road1.png")));
            case 'G' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Road2.png")));
            case 'R' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/RoadH.png")));
            case 'V' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/RoadV.png")));
            case 'S' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Start.png")));
            case 'H' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/House1.png")));
            case 'T' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Tree1.png")));
            case 't' -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Tree2.png")));
            default -> tile.image = new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Road1.png")));
        }

        tileCache.put(tileChar, tile);
        return tile;
    }
}

