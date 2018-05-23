/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import bot.Waypoint;
import client.Map;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import structures.Item;
import structures.Loc;
import structures.MapDescription;

/**
 *
 * @author Myzreal
 */
public class Minimap {

    public int xs, xe;
    public int ys, ye;
    public int width, height;

    public void configure(int xs, int ys, int width, int height) {
        this.xs = xs;
        this.ys = ys;
        this.xe = xs + width;
        this.ye = ys + height;
        this.width = width;
        this.height = height;
    }

    public BufferedImage draw(Loc view) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = xs; x < xe; x++) {
            for (int y = ys; y < ye; y++) {
                Loc location = new Loc(view.x + x, view.y + y, view.z);
                int rgb = 0;
                if (MainFrame.getInstance().showWaypointsBox.isSelected()) {
                    List<Waypoint> clone = new ArrayList<>(CavebotFrame.getInstance().getCurrentSettings().waypoints);
                    for (int i = 0; i < clone.size(); i++) {
                        Waypoint waypoint = clone.get(i);
                        if (waypoint.location == null) {
                            continue;
                        }
                        if (waypoint.location.equals(location)) {
                            if (CavebotFrame.getInstance().waypointsList.getSelectedIndex() == i) {
                                rgb = 0xFFD700;
                                break;
                            } else {
                                rgb = 0x00BFFF;
                            }
                        }
                    }
                }
                if (rgb == 0) {
                    MapDescription.Tile tile = Map.getInstance().at(location);
                    if (tile == null) {
                        rgb = 0x777777;
                    } else if (tile.creatures.size() > 0) {
                        if (Map.getInstance().getPlayer().location.equals(tile.location)) {
                            rgb = 0x00FF00;
                        } else {
                            rgb = 0xFF0000;
                        }
                    } else {
                        for (Item item : tile.items) {
                            if (item.id == 274 || item.id == 203 || Item.isWalkablePathblocker(item.id)) {
                                rgb = 0xFF00FF;
                                break;
                            }
                            if (!Item.isWalkable(item.id)) {
                                rgb = 0xFFFFFF;
                                break;
                            }
                        }
                    }
                }
                bi.setRGB(x - xs, y - ys, rgb);
            }
        }
        return bi;
    }
}
