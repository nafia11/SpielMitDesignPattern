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

        Tile tile;
        switch (tileChar) {
            case 'F':
                tile = new Tile(TileType.FLOOR, false);
                tile.setImage(new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Floor2.png"))));
                break;
            case 'f':
                tile = new Tile(TileType.FLOOR, false);
                tile.setImage(new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Floor2.png"))));
                break;
            case 'E':
                tile = new Tile(TileType.END, false);
                tile.setImage(new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/End.png"))));
                break;
            case 'B':
                tile = new Tile(TileType.BLOCK, true);
                tile.setImage(new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Block.png"))));
                tile.setCollision(true);
                break;
            case 'b':
                tile = new Tile(TileType.BLOCK, true);
                tile.setImage(new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Block3.png"))));
                tile.setCollision(true);
                break;
            case 'w':
                tile = new Tile(TileType.BLOCK, true);
                tile.setImage(new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Block2.png"))));
                tile.setCollision(true);
                break;
            case 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T':
                tile = new AnimatedTile(TileType.CHEST, true,
                        new Image[]{
                                new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Box1.png"))),
                                new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Box3.png"))),
                                //new Image(Objects.requireNonNull(TileFactory.class.getResourceAsStream("/tiles/Box2.png")))
                        });
                break;

            default:
                // Log the invalid tile character and use a default or placeholder tile
                System.err.println("Warning: Invalid tile character: " + tileChar);
                tile = new Tile(TileType.FLOOR, false); // Default to FLOOR tile
                break;
        }


            tileCache.put(tileChar, tile);

        return tile;
    }

}
