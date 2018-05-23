/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.networking.PacketReceiver;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.newdawn.slick.util.pathfinding.*;
import structures.Creature;
import structures.Directions;
import structures.Loc;
import structures.MapDescription;

/**
 *
 * @author Moshe Revah
 */
public class Map implements TileBasedMap {

    private static final Logger logger = Logger.getLogger(PacketReceiver.class.getName());
    private static Map instance;
    private java.util.Map<Loc, MapDescription.Tile> tiles = new HashMap<>();
    private List<Entity> entities = new CopyOnWriteArrayList<>();
    private Entity player;
    private java.util.Map<Short, Rectangle> bounds = new HashMap<>();
    private Rectangle pfBounds;
    private Loc pfSrc, pfDst;
    private boolean pfUntilReach;

    public static Map getInstance() {
        return instance;
    }

    Map() {
        instance = this;
    }

    void update(MapDescription md) {
        for (MapDescription.Tile tile : md.tiles) {
            updateTile(tile);
        }
    }

    void updateTile(MapDescription.Tile tile) {
        Rectangle rect = bounds.get(tile.location.z);
        if (rect == null) {
            rect = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
            bounds.put(tile.location.z, rect);
        }
        rect.x = (int) Math.min(tile.location.x, rect.getMinX());
        rect.y = (int) Math.min(tile.location.y, rect.getMinY());
        rect.width = (int) (Math.max(tile.location.x, rect.getMaxX()) - rect.x + 1);
        rect.height = (int) (Math.max(tile.location.y, rect.getMaxY()) - rect.y + 1);

        for (Creature c : tile.creatures) {
            Entity e = getEntityByID(c.id);
            if (e != null) {
                e.update(e.location, c);
            } else {
                e = new Entity(tile.location, c);
                addOrUpdateEntity(e);
            }
        }
        MapDescription.Tile inList = tiles.get(tile.location);
        if (inList != null) {
            inList.items = tile.items;
        } else {
            tiles.put(tile.location, tile);
        }
    }

    /**
     * Adds an entity to entities and to the tile of it's location. If an entity
     * of the same ID already exists, it will be updated with the values of e.
     *
     * @param e
     * @return true if added, false if updated.
     */
    public boolean addOrUpdateEntity(Entity e) {
        Entity inList = getEntityByID(e.id);
        if (inList != null) {
            if (!e.location.equals(inList.location)) {
                MapDescription.Tile fromTile = at(inList.location);
                MapDescription.Tile toTile = at(e.location);
                for (int i = 0; i < fromTile.creatures.size(); i++) {
                    if (fromTile.creatures.get(i).id == e.id) {
                        fromTile.creatures.remove(i);
                        break;
                    }
                }
                inList.update(e.location, e);
                toTile.creatures.add(inList);
            } else {
                inList.update(e.location, e);
            }
            return false;
        }
        if (e.name.equals(Client.getInstance().getCharacter().name)) {
            player = e;
        }
        MapDescription.Tile tile = at(e.location);
        if (tile != null) {
            tile.creatures.add(e);
        }
        entities.add(e);
        return true;
    }

    void removeEntity(int id) {
        removeEntity(getEntityByID(id));
    }

    void removeEntity(Entity e) {
        Entity toRemove = null;
        for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
            Entity E = it.next();
            if (E.id == e.id) {
                toRemove = E;
                break;
            }
        }
        entities.remove(toRemove);

        MapDescription.Tile tile = at(e.location);
        if (tile != null) {
            for (int i = 0; i < tile.creatures.size(); i++) {
                if (tile.creatures.get(i).id == e.id) {
                    tile.creatures.remove(i);
                    break;
                }
            }
        }
    }

    void moveEntity(int id, short direction) {
        Entity e = getEntityByID(id);
        if (e == null) {
            return;
        }
        Entity clone = e.clone();
        clone.move(direction);
        if (id == player.id) {
            for (Entity E : entities) {
                if (!isVisible(E.location)) {
                    removeEntity(E);
                }
            }
        } else if (!isVisible(clone.location)) {
            removeEntity(e);
            return;
        }
        addOrUpdateEntity(clone);
    }

    public MapDescription.Tile at(int x, int y, short z) {
        return at(new Loc(x, y, z));
    }

    public MapDescription.Tile at(Loc l) {
        return tiles.get(l);
    }

    public Entity getEntityByID(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }

    public Entity getEntityByName(String s) {
        for (Entity e : entities) {
            if (e.name.equals(s)) {
                return e;
            }
        }
        return null;
    }

    public java.util.Map<Loc, MapDescription.Tile> getTiles() {
        return tiles;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getPlayer() {
        return player;
    }

    /**
     * Tells whether the given location is visible from the viewLocation of the
     * map or not.
     */
    public boolean isVisible(Loc l) {
        return l.isVisibleFrom(player.location);
    }

    public Rectangle getFloorBounds() {
        return getFloorBounds(player.location.z);
    }

    public Rectangle getFloorBounds(short z) {
        return bounds.get(z);
    }

    public short[] findPath(Loc to, boolean visibleOnly, boolean untilReach) {
        return findPath(player.location, to, visibleOnly, untilReach);
    }

    /**
     * Finds a path from
     * <code>from</code> to
     * <code>to</code>.
     *
     * @param src
     * @param dst
     * @param visibleOnly whether the tiles that are walked on in the path must
     * be visible from
     * <code>from</code>
     * @param untilReach whether finding a path to one of the 8 tiles
     * surrounding
     * <code>to</code> is enough
     * @return the path (array of directions) to be walked to get to
     * <code>to</code>.
     */
    public short[] findPath(Loc src, Loc dst, boolean visibleOnly, boolean untilReach) {
        if (src.z != dst.z) {
            return null;
        }
        if (visibleOnly) {
            int xs = src.x - 7, xe = src.x + 7;
            int ys = src.y - 5, ye = src.y + 5;
            if (xs < 0) {
                xs = 0;
            }
            if (ys < 0) {
                ys = 0;
            }
            pfBounds = new Rectangle(xs, ys, xe - xs + 1, ye - ys + 1);
        } else {
            int xs = src.x - 255, xe = src.x + 255;
            int ys = src.y - 255, ye = src.y + 255;
            if (xs < 0) {
                xs = 0;
            }
            if (ys < 0) {
                ys = 0;
            }
            pfBounds = new Rectangle(xs, ys, xe - xs + 1, ye - ys + 1);
        }
        if (!pfBounds.contains(dst.x, dst.y)) {
            return null;
        }
        pfSrc = src;
        pfDst = dst;
        pfUntilReach = untilReach;
        //logger.log(Level.INFO, "Finding a path from {0} to {1}\n", new Object[]{src, dst});
        long start = System.currentTimeMillis();
        int xs = (int) pfBounds.getMinX();
        int ys = (int) pfBounds.getMinY();
        PathFinder pf = new AStarPathFinder(this, 255, true);
        Path path = pf.findPath(null, src.x - xs, src.y - ys, dst.x - xs, dst.y - ys);
        long time = System.currentTimeMillis() - start;
        if (path == null) {
            //logger.log(Level.INFO, "No path found, spent {0}ms trying", time);
            return null;
        }
        String pathString = "A path was found within " + time + "ms:\n";
        short[] directions = new short[path.getLength() - 1];
        Loc prevLoc = null;
        for (int i = 0; i < path.getLength(); i++) {
            Step step = path.getStep(i);
            Loc loc = new Loc(step.getX(), step.getY(), src.z);
            if (prevLoc != null) {
                if (untilReach && i == path.getLength() - 1) {
                    break;
                }
                directions[i - 1] = loc.directionFrom(prevLoc);
                pathString += String.format("\t%s (%s)\n", Directions.toString(loc.directionFrom(prevLoc)), loc);
            }
            prevLoc = loc;
        }
        //logger.info(pathString);
        return directions;
    }

    @Override
    public int getWidthInTiles() {
        return pfBounds.width;
    }

    @Override
    public int getHeightInTiles() {
        return pfBounds.height;
    }

    @Override
    public void pathFinderVisited(int x, int y) {
        // doesn't seem to effect the path finder :O
    }

    @Override
    public boolean blocked(Mover mover, int x, int y) {
        x += pfBounds.getMinX();
        y += pfBounds.getMinY();
        MapDescription.Tile tile = at(x, y, pfSrc.z);
        if (tile == null) {
            return false;
        }
        if (x == pfDst.x && y == pfDst.y) {
            if (pfUntilReach || tile.isWalkable()) {
                return false;
            }
        }
        return tile.isPathblocker();
    }

    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
        if (sx - tx != 0 && sy - ty != 0) {
            // a diagonal!
            return 3;
        }
        return 1;
    }

    public boolean isReachable(Loc l) {
        return l.z == player.location.z
                && Math.abs(player.location.x - l.x) < 2
                && Math.abs(player.location.y - l.y) < 2;
    }
}
