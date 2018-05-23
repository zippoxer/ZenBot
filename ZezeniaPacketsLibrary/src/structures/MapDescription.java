/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public class MapDescription extends Structure {

    public static class Tile {

        public List<Item> items = new ArrayList<>();
        public List<Creature> creatures = new ArrayList<>();
        public Loc location;

        public Tile() {
        }

        public Tile(Loc location, TileDescription td) {
            items = td.items;
            creatures = td.creatures;
            this.location = location;
        }

        public boolean isWalkable() {
            return isWalkable(false);
        }
        
        public boolean isWalkable(boolean ignoreCreatures) {
            if(!ignoreCreatures && creatures.size() > 0) {
                return false;
            }
            for (Item item : items) {
                if (!Item.isWalkable(item.id)) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean isPathblocker() {
            return isPathblocker(false);
        }
        
        public boolean isPathblocker(boolean ignoreCreatures) {
            if(!ignoreCreatures && creatures.size() > 0) {
                return true;
            }
            for (Item item : items) {
                if (Item.isPathblocker(item.id)) {
                    return true;
                }
            }
            return false;
        }
    }
    public Loc view;
    public short width, height;
    public List<MapDescription.Tile> tiles = new ArrayList<>();

    public MapDescription() {
    }

    public MapDescription(Loc start, byte width, byte height, List<MapDescription.Tile> tiles) {
        this.view = start;
        this.width = width;
        this.height = height;
        this.tiles = tiles;
    }

    @Override
    public MapDescription read(NetworkMessage m) throws IOException {
        width = m.readByte();
        height = m.readByte();
        view = new Loc().read(m);
        tiles = new ArrayList<>();
        byte startz, endz, stepz;
        if (view.z > 7) {
            startz = (byte) (view.z - 2);
            endz = (byte) (view.z + 2);
            stepz = 1;
        } else {
            startz = 7;
            endz = 0;
            stepz = (byte) 255; // -1
        }
        int skip = 0;
        for (byte z = startz; z != endz + stepz; z += stepz) {
            for (short x = 0; x < width; x++) {
                for (short y = 0; y < height; y++) {
                    if (skip > 0) {
                        skip--;
                        continue;
                    }
                    TileDescription td = new TileDescription().read(m);
                    skip = td.skip;

                    //int offset = view.z - z;
                    int offset = 0;
                    Loc l = new Loc((short) (view.x + x + offset), (short) (view.y + y + offset), z);
                    MapDescription.Tile tile = new MapDescription.Tile(l, td);
                    tiles.add(tile);
                }
            }
        }
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
