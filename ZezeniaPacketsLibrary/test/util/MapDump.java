/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.common.collect.ImmutableMap;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import structures.Creature;
import structures.Item;
import structures.MapDescription;

/**
 *
 * @author Moshe Revah
 */
public class MapDump {

    private Map<Integer, String> itemNames = new HashMap<Integer, String>() {

        {
            put(199, "box");
            put(200, "box");
            put(343, "wooden floor");
            put(2807, "ornate bow");
            put(912, "wall");
            put(120, "grass");
            put(62, "pavement");
            put(111, "grass");
            put(118, "grass");
            put(113, "grass");
            put(37, "grass");
            put(114, "grass");
            put(94, "sand with stones");
            put(206, "wall");
            put(1473, "(-1); -1) sand");
            put(36, "barel");
            put(530, "hatchet");
            put(624, "branch");
            put(1073, "door");
            put(1793, "broken wall");
            put(122, "wall");
            put(110, "grass");
            put(119, "grass");
            put(121, "grass");
            put(183, "flowers");
            put(116, "grass");
            put(175, "bush");
            put(78, "mud");
            put(301, "table");
            put(128, "wall with window");
            put(231, "purple ground");
            put(1255, "half wall");
            put(299, "deposit box");
            put(297, "deposit box");
            put(227, "marble tile");
            put(2195, "roof");
            put(226, "pillar");
            put(1876, "light marble floor");
            put(123, "wall");
            put(2809, "praying monk statue");
            put(1885, "dark marble floor");
            put(125, "wall");
            put(1457, "sand");
            put(172, "tree");
            put(117, "grass");
            put(112, "grass");
            put(1292, "rock");
            put(1521, "carpet");
            put(1522, "carpet");
            put(1523, "carpet");
            put(1524, "carpet");
            put(1525, "carpet");
            put(1526, "carpet");
            put(1527, "carpet");
            put(1528, "carpet");
            put(1529, "carpet");
            put(2955, "stone road");
            put(2914, "wall");
            put(434, "knife");
            put(326, "counter");
            put(293, "dustbin");
            put(2945, "wall");
            put(2884, "wall");
            put(2922, "roof");
            put(2905, "half wall");
            put(2934, "roof");
            put(2908, "wall");
            put(197, "bush");
            put(2221, "grass");
            put(2162, "entrance");
            put(2928, "(-1); -1) roof");
            put(2927, "roof");
            put(2197, "entrance");
            put(2187, "wall");
            put(2194, "roof");
            put(1401, "roof");
            put(1399, "entrance");
            put(1405, "roof");
            put(1533, "scroll");
            put(2145, "stairs");
            put(1254, "half wall");
            put(316, "water");
            put(132, "broken wall");
            put(2210, "roof border (-1); -1)");
            put(136, "mountain wall");
            put(138, "mountain wall");
            put(2237, "grass");
            put(137, "mountain wall");
            put(177, "tree");
            put(139, "mountain wall");
            put(115, "grass");
            put(141, "mountain wall (-1); -1)");
            put(2502, "tree");
            put(174, "high grass");
            put(170, "tree");
            put(1860, "wildflowers");
            put(2747, "tree");
            put(1859, "wildflowers");
            put(1852, "high grass");
            put(178, "sapin tree");
            put(1853, "high grass");
            put(416, "blood");
        }
    };
    private MapDescription map;
    private String name;

    public MapDump(MapDescription map, String name) {
        this.map = map;
        this.name = name;
    }

    public void dump(OutputStream os) {
        Map<Short, List<MapDescription.Tile>> floors = new HashMap<>();
        for (MapDescription.Tile tile : map.tiles) {
            List<MapDescription.Tile> tiles = floors.get(tile.location.z);
            if (tiles == null) {
                tiles = new ArrayList<>();
            }
            tiles.add(tile);
            floors.put(tile.location.z, tiles);
        }

        PrintStream ps = new PrintStream(os);
        ps.printf("<html><head><title>%s</title><style type=\"text/css\">body { font=family: arial; font-size: 12px }</style></head><body>", "Dump of map " + name);
        ps.printf("Size: %d/%d<br/>Tiles: %d<br/>Floors: %d<br/><br/>", map.width, map.height, map.tiles.size(), floors.size());
        for (Map.Entry<Short, List<MapDescription.Tile>> entry : floors.entrySet()) {
            short z = entry.getKey();
            List<MapDescription.Tile> tiles = entry.getValue();

            ps.printf("<h3>Floor %d (%d tiles)</h3>", z, tiles.size());
            int offset = map.view.z - z;
            int xs = map.view.x + offset;
            int ys = map.view.y + offset;
            int xe = xs + 18;
            int ye = ys + 14;
            for (int y = ys; y < ye; y++) {
                for (int x = xs; x < xe; x++) {
                    ps.printf("<div title=\"%d/%d/%d\" style=\"width: 96px; height: 96px; padding: 2px; border: solid 1px gray; float: left\">", x, y, z);
                    MapDescription.Tile tile = null;
                    for (MapDescription.Tile t : tiles) {
                        if (t.location.x == x && t.location.y == y) {
                            tile = t;
                            break;
                        }
                    }
                    if (tile != null) {
                        if (tile.items.isEmpty() && tile.creatures.isEmpty()) {
                            ps.print("?");
                        }
                        for (Item item : tile.items) {
                            Object o = itemNames.get(item.id);
                            if(o == null) {
                                o = item.id;
                            }
                            ps.printf("- %s<br/>", o);
                        }
                        for (Creature c : tile.creatures) {
                            ps.printf("{%s/%d%%/%d}<br/>", c.name, c.health, c.direction);
                        }
                    }
                    ps.print("</div>");
                    if (x == xe - 1) {
                        ps.print("<div style=\"clear: left\"></div>");
                    }
                }
            }
        }
        ps.print("</body></html>");
    }
}
