/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import packets.RequestCloseContainerPacket;

/**
 *
 * @author Myzreal
 */
public class Container {

    public short id, volume;
    public String name;
    public List<Item> items = new CopyOnWriteArrayList<>();

    public Container(short id, String name, short volume, List<Item> items) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.items = items;
    }

    public void close() throws IOException {
        new RequestCloseContainerPacket(id).send(Client.getInstance().getOs());
    }

    public boolean isOpen() {
        List<Container> list = Client.getInstance().getOpenContainers();
        for (Container c : list) {
            if (c.id == id) {
                return true;
            }
        }
        return false;
    }

    public int emptySlots() {
        return volume - items.size();
    }

    /**
     * Finds all items of the given id in this container.
     *
     * @param id
     * @return items if found, otherwise null.
     */
    public List<Item> findItems(int id) {
        List<Item> matches = new ArrayList<>();
        for (Item item : items) {
            if (item.id == id) {
                matches.add(item);
            }
        }
        return (matches.size() > 0 ? matches : null);
    }

    public Item findItem(int id) {
        for (Item item : items) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }

    public static Container byID(int id) {
        for (Container c : Client.getInstance().getOpenContainers()) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }

    public static Container open(Item item, int timeout) throws IOException, InterruptedException {
        int before = Client.getInstance().getOpenContainers().size();
        item.use();
        long start = System.currentTimeMillis();
        while (before >= Client.getInstance().getOpenContainers().size()) {
            if(before > Client.getInstance().getOpenContainers().size()) {
                before--;
            }
            if (System.currentTimeMillis() - start > timeout) {
                return null;
            }
            Thread.sleep(1);
        }
        return Iterables.getLast(Client.getInstance().getOpenContainers());
    }
}
