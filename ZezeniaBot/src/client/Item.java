/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import packets.ItemMovePacket;
import packets.ItemUsePacket;
import packets.LookAtPacket;
import structures.Loc;

/**
 *
 * @author Moshe Revah
 */
public class Item extends structures.Item {

    private static List<Integer> foods = Arrays.asList(605, 611, 617, 626, 2014,
            2020, 2026, 2032, 2038, 2044, 2050, 2056, 2062);
    
    private static List<Integer> closedDoors = Arrays.asList(1073, 1075, 3991,
            3992, 3950, 3951, 3952, 3953, 3954, 3945, 333, 224);
            
    private static List<Integer> openDoors = Arrays.asList(1074, 1076, 3993,
            3994, 3943, 3944, 3945, 3946, 3947, 3948, 334, 225);
    
    public static boolean isFood(int id) {
        return foods.contains(id);
    }
    
    public static boolean isClosedDoor(int id) {
        return closedDoors.contains(id);
    }
    
    public static boolean isOpenDoor(int id) {
        return openDoors.contains(id);
    }
    
    public static boolean isDoor(int id) {
        return isClosedDoor(id) || isOpenDoor(id);
    }
    
    public ItemLoc location;

    public Item(ItemLoc location, int id, short count) {
        super(id, count);
        this.location = location;
    }

    public Item(ItemLoc location, structures.Item item) {
        super(item.id, item.count);
        this.location = location;
    }

    public void use() throws IOException {
        Loc l = location.toLocation();
        short n = 0;
        while (Container.byID(n) != null) {
            n++;
        }
        new ItemUsePacket(l, n, id).send(Client.getInstance().getOs());
    }

    public void move(ItemLoc l, short count) throws IOException {
        Loc from = location.toLocation();
        Loc to = l.toLocation();
        new ItemMovePacket(from, to, count).send(Client.getInstance().getOs());
    }

    public void move(ItemLoc l) throws IOException {
        move(l, count);
    }

    public void look() throws IOException {
        new LookAtPacket(location.toLocation()).send(Client.getInstance().getOs());
    }
}
